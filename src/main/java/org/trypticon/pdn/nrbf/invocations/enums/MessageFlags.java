package org.trypticon.pdn.nrbf.invocations.enums;

import com.google.common.io.LittleEndianDataInputStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * [MS-NRBF] 2.2.1.1 MessageFlags
 */
@SuppressWarnings("UnstableApiUsage")
public enum MessageFlags {

    /**
     * The record contains no arguments. It is in the Arg category.
     */
    NoArgs(0x00000001),

    /**
     * The Arguments Array is in the Args field of the Method record. It is in the Arg category.
     */
    ArgsInline(0x00000002),

    /**
     * Each argument is an item in a separate Call Array record. It is in the Arg category.
     */
    ArgsIsArray(0x00000004),

    /**
     * The Arguments Array is an item in a separate Call Array record. It is in the Arg category.
     */
    ArgsInArray(0x00000008),

    /**
     * The record does not contain a Call Context value. It is in the Context category.
     */
    NoContext(0x00000010),

    /**
     * Call Context contains only a Logical Call ID value and is in the CallContext field of the Method record. It is in the Context category.
     */
    ContextInline(0x00000020),

    /**
     * CallContext values are contained in an array that is contained in the Call Array record. It is in the Context category.
     */
    ContextInArray(0x00000040),

    /**
     * The Method Signature is contained in the Call Array record. It is in the Signature category.
     */
    MethodSignatureInArray(0x00000080),

    /**
     * Message Properties is contained in the Call Array record. It is in the Property category.
     */
    PropertiesInArray(0x00000100),

    /**
     * The Return Value is a Null object. It is in the Return category.
     */
    NoReturnValue(0x00000200),

    /**
     * The method has no Return Value. It is in the Return category.
     */
    ReturnValueVoid(0x00000400),

    /**
     * The Return Value is in the ReturnValue field of the MethodReturnCallArray record. It is in the Return category.
     */
    ReturnValueInline(0x00000800),

    /**
     * The Return Value is contained in the MethodReturnCallArray record. It is in the Return category.
     */
    ReturnValueInArray(0x00001000),

    /**
     * An Exception is contained in the MethodReturnCallArray record. It is in the Exception category.
     */
    ExceptionInArray(0x00002000),

    /**
     * The Remote Method is generic and the actual Remoting Types for the Generic Arguments are contained in the Call Array. It is in the Generic category.<2>
     */
    GenericMethod(0x00008000),

    ;

    private final int value;

    MessageFlags(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Set<MessageFlags> readFrom(LittleEndianDataInputStream stream) throws IOException {
        int rawBits = stream.readInt();
        return Arrays.stream(MessageFlags.values())
                .filter(messageFlag -> (messageFlag.value & rawBits) != 0)
                .collect(Collectors.toSet());
    }
}
