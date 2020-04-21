package org.trypticon.pdn.nrbf.invocations.records;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.common.types.LengthPrefixedString;
import org.trypticon.pdn.nrbf.invocations.enums.MessageFlags;
import org.trypticon.pdn.nrbf.invocations.structs.ArrayOfValueWithCode;
import org.trypticon.pdn.nrbf.invocations.structs.StringValueWithCode;
import org.trypticon.pdn.nrbf.invocations.structs.ValueWithCode;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * [MS-NRBF] 2.2.3.1 BinaryMethodCall
 */
@SuppressWarnings("UnstableApiUsage")
public class BinaryMethodCall extends NrbfRecord {
    private final Set<MessageFlags> messageEnum;
    private final StringValueWithCode methodName;
    private final StringValueWithCode typeName;
    private final StringValueWithCode callContext;
    private final ArrayOfValueWithCode args;

    public BinaryMethodCall(Set<MessageFlags> messageEnum,
                            StringValueWithCode methodName,
                            StringValueWithCode typeName,
                            StringValueWithCode callContext,
                            ArrayOfValueWithCode args) {

        super(RecordTypeEnumeration.BinaryMethodCall);
        this.messageEnum = messageEnum;
        this.methodName = methodName;
        this.typeName = typeName;
        this.callContext = callContext;
        this.args = args;
    }

    public Set<MessageFlags> getMessageEnum() {
        return messageEnum;
    }

    public String getMethodName() {
        return ((LengthPrefixedString) methodName.getValue()).asString();
    }

    public String getTypeName() {
        return ((LengthPrefixedString) typeName.getValue()).asString();
    }

    public String getCallContext() {
        if (callContext == null) {
            return null;
        }
        return ((LengthPrefixedString) callContext.getValue()).asString();
    }

    public List<Object> getArgs() {
        if (args == null) {
            return null;
        }
        return args.getListOfValueWithCode().stream()
                .map(ValueWithCode::getValue)
                .collect(ImmutableList.toImmutableList());
    }

    public static BinaryMethodCall readFrom(LittleEndianDataInputStream stream) throws IOException {
        Set<MessageFlags> messageEnum = MessageFlags.readFrom(stream);
        StringValueWithCode methodName = StringValueWithCode.readFrom(stream);
        StringValueWithCode typeName = StringValueWithCode.readFrom(stream);
        StringValueWithCode callContext = null;
        if (messageEnum.contains(MessageFlags.ContextInline)) {
            callContext = StringValueWithCode.readFrom(stream);
        }
        ArrayOfValueWithCode args = null;
        if (messageEnum.contains(MessageFlags.ArgsInline)) {
            args = ArrayOfValueWithCode.readFrom(stream);
        }
        return new BinaryMethodCall(messageEnum, methodName, typeName, callContext, args);
    }
}
