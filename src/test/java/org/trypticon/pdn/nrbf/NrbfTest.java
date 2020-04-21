package org.trypticon.pdn.nrbf;

import com.google.common.collect.ImmutableList;
import com.google.common.io.BaseEncoding;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.arrays.records.ArraySingleObject;
import org.trypticon.pdn.nrbf.classes.ClassMember;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;
import org.trypticon.pdn.nrbf.classes.records.ClassWithMembersAndTypes;
import org.trypticon.pdn.nrbf.common.enums.BinaryTypeEnumeration;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.invocations.enums.MessageFlags;
import org.trypticon.pdn.nrbf.invocations.records.BinaryMethodCall;
import org.trypticon.pdn.nrbf.invocations.records.BinaryMethodReturn;
import org.trypticon.pdn.nrbf.other.BinaryLibrary;
import org.trypticon.pdn.nrbf.other.MessageEnd;
import org.trypticon.pdn.nrbf.other.SerializationHeaderRecord;
import org.trypticon.pdn.nrbf.references.BinaryObjectString;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SuppressWarnings("UnstableApiUsage")
public class NrbfTest {
    @Test
    public void testExample1() throws Exception {
        byte[] data = BaseEncoding.base16().decode(
                "0001000000FFFFFFFF01000000000000001514000000120B53656E6441646472657373126F444F4A52656D6F74696E67" +
                        "4D657461646174612E4D795365727665722C20444F4A52656D6F74696E674D657461646174612C2056657273696F6E3D" +
                        "312E302E323632322E33313332362C2043756C747572653D6E65757472616C2C205075626C69634B6579546F6B656E3D" +
                        "6E756C6C10010000000100000009020000000C0300000051444F4A52656D6F74696E674D657461646174612C20566572" +
                        "73696F6E3D312E302E323632322E33313332362C2043756C747572653D6E65757472616C2C205075626C69634B657954" +
                        "6F6B656E3D6E756C6C05020000001B444F4A52656D6F74696E674D657461646174612E41646472657373040000000653" +
                        "74726565740443697479055374617465035A697001010101030000000604000000114F6E65204D6963726F736F667420" +
                        "5761790605000000075265646D6F6E64060600000002574106070000000539383035340B");

        Nrbf nrbf = Nrbf.readFrom(new LittleEndianDataInputStream(new ByteArrayInputStream(data)));
        List<NrbfRecord> records = nrbf.getRecords();
        assertThat(records, hasSize(6));

        SerializationHeaderRecord serializationHeaderRecord = (SerializationHeaderRecord) records.get(0);
        assertThat(serializationHeaderRecord.getRecordTypeEnum(), Matchers.is(RecordTypeEnumeration.SerializedStreamHeader));
        assertThat(serializationHeaderRecord.getRootId(), is(1));
        assertThat(serializationHeaderRecord.getHeaderId(), is(-1));
        assertThat(serializationHeaderRecord.getMajorVersion(), is(1));
        assertThat(serializationHeaderRecord.getMinorVersion(), is(0));

        BinaryMethodCall binaryMethodCall = (BinaryMethodCall) records.get(1);
        assertThat(binaryMethodCall.getRecordTypeEnum(), is(RecordTypeEnumeration.BinaryMethodCall));
        assertThat(binaryMethodCall.getMessageEnum(),
                Matchers.containsInAnyOrder(MessageFlags.ArgsIsArray, MessageFlags.NoContext));
        assertThat(binaryMethodCall.getMethodName(), is("SendAddress"));
        assertThat(binaryMethodCall.getTypeName(),
                is("DOJRemotingMetadata.MyServer, DOJRemotingMetadata, Version=1.0.2622.31326, " +
                        "Culture=neutral, PublicKeyToken=null"));
        assertThat(binaryMethodCall.getCallContext(), is(nullValue()));
        assertThat(binaryMethodCall.getArgs(), is(nullValue()));

        ArraySingleObject callArray = (ArraySingleObject) records.get(2);
        assertThat(callArray.getRecordTypeEnum(), is(RecordTypeEnumeration.ArraySingleObject));
        assertThat(callArray.getArrayInfo().getObjectId(), is(1));
        assertThat(callArray.getArrayInfo().getLength(), is(1));
        assertThat(callArray.getMembers(), hasSize(1));
        NrbfClassRecord member0 = (NrbfClassRecord) callArray.getMembers().get(0);
        assertThat(member0, is(sameInstance(records.get(4))));

        BinaryLibrary binaryLibrary = (BinaryLibrary) records.get(3);
        assertThat(binaryLibrary.getRecordTypeEnum(), is(RecordTypeEnumeration.BinaryLibrary));
        assertThat(binaryLibrary.getObjectId(), is(3));
        assertThat(binaryLibrary.getLibraryName(),
                is("DOJRemotingMetadata, Version=1.0.2622.31326, " +
                        "Culture=neutral, PublicKeyToken=null"));

        ClassWithMembersAndTypes classWithMembersAndTypes = (ClassWithMembersAndTypes) records.get(4);
        assertThat(classWithMembersAndTypes.getRecordTypeEnum(), is(RecordTypeEnumeration.ClassWithMembersAndTypes));
        assertThat(classWithMembersAndTypes.getClassInfo().getObjectId(), is(2));
        assertThat(classWithMembersAndTypes.getClassInfo().getName(), is("DOJRemotingMetadata.Address"));
        assertThat(classWithMembersAndTypes.getClassInfo().getMemberNames(), contains(
                "Street", "City", "State", "Zip"));
        assertThat(classWithMembersAndTypes.getMemberTypeInfo().getBinaryTypeEnums(), Matchers.contains(
                BinaryTypeEnumeration.String, BinaryTypeEnumeration.String,
                BinaryTypeEnumeration.String, BinaryTypeEnumeration.String));
        assertThat(classWithMembersAndTypes.getLibraryId(), is(3));
        ImmutableList<ClassMember> members = classWithMembersAndTypes.getClassMembers();
        assertThat(members, hasSize(4));

        assertThat(((BinaryObjectString) members.get(0).getValue()).getValue().asString(), is("One Microsoft Way"));
        assertThat(((BinaryObjectString) members.get(1).getValue()).getValue().asString(), is("Redmond"));
        assertThat(((BinaryObjectString) members.get(2).getValue()).getValue().asString(), is("WA"));
        assertThat(((BinaryObjectString) members.get(3).getValue()).getValue().asString(), is("98054"));

        MessageEnd messageEnd = (MessageEnd) records.get(5);
        assertThat(messageEnd.getRecordTypeEnum(), is(RecordTypeEnumeration.MessageEnd));
    }

    @Test
    public void testExample2() throws Exception {
        byte[] data = BaseEncoding.base16().decode(
                "0000000000000000000100000000000000161108000012104164647265737320" +
                        "72656365697665640B");

        Nrbf nrbf = Nrbf.readFrom(new LittleEndianDataInputStream(new ByteArrayInputStream(data)));
        List<NrbfRecord> records = nrbf.getRecords();
        assertThat(records, hasSize(3));

        SerializationHeaderRecord serializationHeaderRecord = (SerializationHeaderRecord) records.get(0);
        assertThat(serializationHeaderRecord.getRecordTypeEnum(), is(RecordTypeEnumeration.SerializedStreamHeader));
        assertThat(serializationHeaderRecord.getRootId(), is(0));
        assertThat(serializationHeaderRecord.getHeaderId(), is(0));
        assertThat(serializationHeaderRecord.getMajorVersion(), is(1));
        assertThat(serializationHeaderRecord.getMinorVersion(), is(0));

        BinaryMethodReturn binaryMethodReturn = (BinaryMethodReturn) records.get(1);
        assertThat(binaryMethodReturn.getRecordTypeEnum(), is(RecordTypeEnumeration.BinaryMethodReturn));
        assertThat(binaryMethodReturn.getMessageEnum(), containsInAnyOrder(
                MessageFlags.NoArgs, MessageFlags.NoContext, MessageFlags.ReturnValueInline));
        assertThat(binaryMethodReturn.getReturnValue(), is("Address received"));

        MessageEnd messageEnd = (MessageEnd) records.get(2);
        assertThat(messageEnd.getRecordTypeEnum(), is(RecordTypeEnumeration.MessageEnd));
    }
}
