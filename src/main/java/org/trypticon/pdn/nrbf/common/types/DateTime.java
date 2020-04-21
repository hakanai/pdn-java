package org.trypticon.pdn.nrbf.common.types;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * [MS-NRBF] 2.1.1.5 DateTime
 */
@SuppressWarnings("UnstableApiUsage")
public class DateTime implements PrimitiveType {
    private static final ZonedDateTime EPOCH = LocalDateTime.of(1, 1, 1, 0, 0)
        .atZone(ZoneOffset.UTC);

    private static final long TICKS_SHIFT = 2;
    private static final int KIND_SHIFT = 62;

    private final long value;

    public DateTime(long value) {
        this.value = value;
    }

    /**
     * A 62-bit signed-integer value that specifies the number of 100 nanoseconds that have elapsed since 12:00:00,
     * January 1, 0001.
     * The value can represent time instants in a granularity of 100 nanoseconds until
     * 23:59:59.9999999, December 31, 9999.
     *
     * However as one should know, ticks only make sense for UTC or time zones with no daylight savings.
     * Once daylight savings is introduced, ticks become a useless notion around the time when the clock
     * changes forwards or backwards.
     *
     * @return the number of ticks.
     */
    public long getTicks() {
        // Deliberately extending the sign bit
        return (value << TICKS_SHIFT) >>> TICKS_SHIFT;
    }

    public Kind getKind() {
        return Kind.values()[(int) (value >> KIND_SHIFT)];
    }


    public static DateTime readFrom(LittleEndianDataInputStream stream) throws IOException {
        long value = stream.readLong();
        return new DateTime(value);
    }

    public enum Kind {
        UNSPECIFIED,
        UTC,
        LOCAL
    }

    @Override
    public Temporal toJavaValue() {
        switch (getKind()) {
            case UTC:
                return EPOCH
                        .plus(getTicks() / 10, ChronoUnit.MILLIS)
                        .plus((getTicks() % 10) * 1000, ChronoUnit.NANOS);
            default:
                // Presumably has to return LocalDateTime with no knowledledge of zone,
                // but it's weird because they store it as an instant, which doesn't
                // really make sense.
                throw new UnsupportedOperationException("TODO");
        }
    }

    @Override
    public String toString() {
        return toJavaValue().toString();
    }
}
