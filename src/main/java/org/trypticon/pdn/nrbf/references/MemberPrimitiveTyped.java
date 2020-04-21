package org.trypticon.pdn.nrbf.references;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.PrimitiveType;
import org.trypticon.pdn.nrbf.common.enums.PrimitiveTypeEnumeration;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;

import java.io.IOException;

/**
 * [MS-NRBF] 2.5.1 MemberPrimitiveTyped
 */
@SuppressWarnings("UnstableApiUsage")
public class MemberPrimitiveTyped extends NrbfRecord {
    private final PrimitiveTypeEnumeration primitiveTypeEnum;
    private final PrimitiveType value;

    public MemberPrimitiveTyped(PrimitiveTypeEnumeration primitiveTypeEnum,
                                PrimitiveType value) {

        super(RecordTypeEnumeration.MemberPrimitiveTyped);
        this.primitiveTypeEnum = primitiveTypeEnum;
        this.value = value;
    }

    public PrimitiveTypeEnumeration getPrimitiveTypeEnum() {
        return primitiveTypeEnum;
    }

    public PrimitiveType getValue() {
        return value;
    }

    public static MemberPrimitiveTyped readFrom(LittleEndianDataInputStream stream) throws IOException {
        PrimitiveTypeEnumeration primitiveTypeEnum = PrimitiveTypeEnumeration.readFrom(stream);
        PrimitiveType value = MemberPrimitiveUnTyped.readFrom(stream, primitiveTypeEnum).getValue();
        return new MemberPrimitiveTyped(primitiveTypeEnum, value);
    }

    @Override
    public String toString() {
        return "(" + getPrimitiveTypeEnum() + ") " + getValue().toString();
    }
}
