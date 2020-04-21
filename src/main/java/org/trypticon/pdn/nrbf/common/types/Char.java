package org.trypticon.pdn.nrbf.common.types;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfException;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * [MS-NRBF] 2.1.1.1 Char
 */
@SuppressWarnings("UnstableApiUsage")
public class Char implements PrimitiveType {
    private final char value;

    public Char(char value) {
        this.value = value;
    }

    public char asChar() {
        return value;
    }

    public static Char readFrom(LittleEndianDataInputStream stream) throws IOException {
        byte[] buffer = new byte[6];
        ByteBuffer writeBuffer = ByteBuffer.wrap(buffer);
        CharBuffer resultBuffer = CharBuffer.allocate(1);
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        decoder.onMalformedInput(CodingErrorAction.REPORT);
        while (true) {
            byte b = stream.readByte();
            writeBuffer.put(b);
            ByteBuffer readBuffer = writeBuffer.duplicate().flip();
            CoderResult result = decoder.decode(readBuffer, resultBuffer, true);
            if (result.isOverflow()) {
                throw new NrbfException("Invalid char, longer than 4 bytes: " + Arrays.toString(buffer));
            } else if (result.isError()) {
                throw new NrbfException("Invalid char, malformed UTF-8: " + Arrays.toString(buffer));
            } else if (!result.isUnderflow()) {
                resultBuffer.flip();
                return new Char(resultBuffer.get());
            }
        }
    }

    @Override
    public Character toJavaValue() {
        return asChar();
    }

    @Override
    public String toString() {
        return String.valueOf(asChar());
    }
}
