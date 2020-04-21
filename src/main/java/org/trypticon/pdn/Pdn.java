package org.trypticon.pdn;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import org.trypticon.pdn.nrbf.Nrbf;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;
import org.trypticon.pdn.nrbf.win32.INT16;
import org.trypticon.pdn.nrbf.win32.INT32;
import org.trypticon.pdn.paintdotnet.BitmapLayer;
import org.trypticon.pdn.paintdotnet.Document;
import org.trypticon.pdn.paintdotnet.MemoryBlock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

@SuppressWarnings("UnstableApiUsage")
public class Pdn {
    /**
     * Magic number 'PDN3' in little endian.
     */
    private static final int MAGIC = 0x334e4450;

    /**
     * Magic number in front of the data section.
     */
    private static final int DATA_MAGIC = 0x0100;

    private final String xmlMetadata;
    private final Nrbf nrbf;
    private final Document document;

    public Pdn(String xmlMetadata, Nrbf nrbf, Document document) {
        this.xmlMetadata = xmlMetadata;
        this.nrbf = nrbf;
        this.document = document;
    }

    public String getXmlMetadata() {
        return xmlMetadata;
    }

    public Nrbf getNrbf() {
        return nrbf;
    }

    public Document getDocument() {
        return document;
    }

    /**
     * Reads PDN from the given byte source.
     *
     * @param byteSource the byte source to read from.
     * @return the PDN model.
     * @throws IOException if an error occurs.
     */
    public static Pdn readFrom(ByteSource byteSource) throws IOException {
        try (InputStream stream = byteSource.openBufferedStream()) {
            return readFrom(stream);
        }
    }

    /**
     * Reads PDN from the given input stream. The stream should be buffered
     * if you expect good performance.
     *
     * @param stream the input stream.
     * @return the PDN model.
     * @throws IOException if an error occurs.
     */
    public static Pdn readFrom(InputStream stream) throws IOException {
        return readFrom(new LittleEndianDataInputStream(stream));
    }

    private static Pdn readFrom(LittleEndianDataInputStream stream) throws IOException {
        INT32 magic = INT32.readFrom(stream);
        if (magic.asInt() != MAGIC) {
            throw new PdnException("Not a Paint.NET file, magic " + magic + " != " + MAGIC);
        }

        // Then the weird UINT24 length in little endian. Might even be a varint,
        // who knows?
        byte b0 = stream.readByte();
        byte b1 = stream.readByte();
        byte b2 = stream.readByte();
        int length = Ints.fromBytes((byte) 0, b2, b1, b0);
        String xmlMetadata = new String(stream.readNBytes(length), StandardCharsets.UTF_8);

        if (INT16.readFrom(stream).asShort() != DATA_MAGIC) {
            throw new PdnException("Invalid magic for data section");
        }

        Nrbf nrbf = Nrbf.readFrom(stream);
        Document document = new Document((NrbfClassRecord) nrbf.getRoot());

        for (BitmapLayer layer : document.getLayers()) {
            MemoryBlock memoryBlock = layer.getSurface().getScan0();

            // TODO: Yeah, this will probably break at some point. We'll have to store it in some other way.
            int length64 = Ints.checkedCast(memoryBlock.getLength64());

            ByteBuffer data = ByteBuffer.allocate(length64);

            byte formatVersion = stream.readByte();
            int chunkSize = Integer.reverseBytes(stream.readInt());
            int chunkCount = IntMath.divide(length64, chunkSize, RoundingMode.UP);
            boolean[] chunksFound = new boolean[chunkCount];

            for (int i = 0; i < chunkCount; i++) {
                int chunkNumber = Integer.reverseBytes(stream.readInt());
                if (chunkNumber < 0 || chunkNumber >= chunkCount) {
                    throw new PdnException("Chunk number " + chunkNumber +
                            " out of bounds for chunk count " + chunkCount);
                }
                if (chunksFound[chunkNumber]) {
                    throw new PdnException("Chunk number " + chunkNumber + " already encountered");
                }
                chunksFound[chunkNumber] = true;
                int dataSize = Integer.reverseBytes(stream.readInt());
                int chunkOffset = chunkNumber * chunkSize;
                int actualChunkSize = Math.min(chunkSize, length64 - chunkOffset);
                byte[] chunkData = stream.readNBytes(dataSize);
                if (formatVersion == 0) {
                    try (InputStream gzipStream = new GZIPInputStream(new ByteArrayInputStream(chunkData))) {
                        chunkData = ByteStreams.toByteArray(gzipStream);
                    }
                }
                data.position(chunkOffset);
                data.put(chunkData, 0, actualChunkSize);
            }
            memoryBlock.setData(data);
        }

        return new Pdn(xmlMetadata, nrbf, document);
    }
}
