package org.trypticon.pdn.nrbf.invocations.records;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.invocations.enums.MessageFlags;
import org.trypticon.pdn.nrbf.invocations.structs.ArrayOfValueWithCode;
import org.trypticon.pdn.nrbf.invocations.structs.StringValueWithCode;
import org.trypticon.pdn.nrbf.invocations.structs.ValueWithCode;

import java.io.IOException;
import java.util.Set;

/**
 * [MS-NRBF] 2.2.3.3 BinaryMethodReturn
 */
@SuppressWarnings("UnstableApiUsage")
public class BinaryMethodReturn extends NrbfRecord {
    private final Set<MessageFlags> messageEnum;
    private final ValueWithCode returnValue;
    private final StringValueWithCode callContext;
    private final ArrayOfValueWithCode args;

    public BinaryMethodReturn(Set<MessageFlags> messageEnum,
                              ValueWithCode returnValue,
                              StringValueWithCode callContext,
                              ArrayOfValueWithCode args) {

        super(RecordTypeEnumeration.BinaryMethodReturn);
        this.messageEnum = messageEnum;
        this.returnValue = returnValue;
        this.callContext = callContext;
        this.args = args;
    }

    public Set<MessageFlags> getMessageEnum() {
        return messageEnum;
    }

    public Object getReturnValue() {
        if (returnValue == null) {
            return null;
        }
        return returnValue.getValue().toJavaValue();
    }

    public String getCallContext() {
        if (callContext == null) {
            return null;
        }
        return callContext.getValue().asString();
    }

    public ImmutableList<Object> getArgs() {
        if (args == null) {
            return null;
        }
        return args.getListOfValueWithCode().stream()
                .map(ValueWithCode::getValue)
                .collect(ImmutableList.toImmutableList());
    }

    public static BinaryMethodReturn readFrom(LittleEndianDataInputStream stream) throws IOException {
        Set<MessageFlags> messageEnum = MessageFlags.readFrom(stream);

        ValueWithCode returnValue = null;
        if (messageEnum.contains(MessageFlags.ReturnValueInline)) {
            returnValue = ValueWithCode.readFrom(stream);
        }

        StringValueWithCode callContext = null;
        if (messageEnum.contains(MessageFlags.ContextInline)) {
            callContext = StringValueWithCode.readFrom(stream);
        }

        ArrayOfValueWithCode args = null;
        if (messageEnum.contains(MessageFlags.ArgsInline)) {
            args = ArrayOfValueWithCode.readFrom(stream);
        }

        return new BinaryMethodReturn(messageEnum, returnValue, callContext, args);
    }
}
