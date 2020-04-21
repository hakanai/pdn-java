package org.trypticon.pdn.nrbf.common.types;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * [MS-NRBF] 2.1.1.4 TimeSpan
 */
@SuppressWarnings("UnstableApiUsage")
public class TimeSpan implements PrimitiveType {
    private final long value;

    public TimeSpan(long value) {
        this.value = value;
    }

    public Duration asDuration() {
        return Duration.of(value / 10, ChronoUnit.MICROS)
                .plus(Duration.of((value % 10) * 1000, ChronoUnit.NANOS));
    }

    public static TimeSpan readFrom(LittleEndianDataInputStream stream) throws IOException {
        long value = stream.readLong();
        return new TimeSpan(value);
    }

    @Override
    public Duration toJavaValue() {
        return asDuration();
    }

    @Override
    public String toString() {
        return asDuration().toString();
    }
}
