package org.trypticon.pdn.nrbf.common.types;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfException;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * [MS-NRBF] 2.1.1.6 LengthPrefixedString
 */
@SuppressWarnings("UnstableApiUsage")
public class LengthPrefixedString implements PrimitiveType {
    private final int length;
    private final byte[] bytes;

    public LengthPrefixedString(int length, byte[] bytes) {
        this.length = length;
        this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    public int getLength() {
        return length;
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    public String asString() {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static LengthPrefixedString readFrom(LittleEndianDataInputStream stream) throws IOException {
        int length = 0;
        boolean success = false;

        // Each bit range is 7 bits long with a maximum of 5 bytes
        for (int bitRange = 0; bitRange < 5 * 7; bitRange += 7) {
            byte b = stream.readByte();

            // Remove the last bit from the length (used to indicate if there is another byte to come)
            // Then shift the number to the appropriate bit range and add it
            length += (b & 0b1111111) << bitRange;

            // Check MSB and if it is zero, this is the last length byte and we are ready to read string
            if ((b & 0b10000000) == 0) {
                success = true;
                break;
            }
        }

        if (!success) {
            throw new NrbfException("LengthPrefixedString overflow");
        }

        return new LengthPrefixedString(length, stream.readNBytes(length));
    }

    @Override
    public Object toJavaValue() {
        return asString();
    }

    @Override
    public String toString() {
        return asString();
    }
}
