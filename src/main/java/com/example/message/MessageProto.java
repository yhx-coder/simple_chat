// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: small_chat/src/main/resources/message.proto

package com.example.message;

public final class MessageProto {
  private MessageProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_Message_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_Message_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_LoginReq_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_LoginReq_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_LoginRes_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_LoginRes_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_MsgReq_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_MsgReq_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_MsgRes_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_MsgRes_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_MsgRX_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_MsgRX_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupCreateReq_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupCreateReq_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupJoinReq_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupJoinReq_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupQuitReq_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupQuitReq_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupMemberQueryReq_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupMemberQueryReq_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupMemberQueryRes_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupMemberQueryRes_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupJoinedQueryReq_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupJoinedQueryReq_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupJoinedQueryRes_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupJoinedQueryRes_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupMessageReq_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupMessageReq_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupMessageRX_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupMessageRX_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_chat_simple_GroupRes_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_chat_simple_GroupRes_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n+small_chat/src/main/resources/message." +
      "proto\022\013chat.simple\"\210\t\n\007Message\0225\n\013messag" +
      "eType\030\001 \001(\0162 .chat.simple.Message.Messag" +
      "eType\022)\n\010loginReq\030\002 \001(\0132\025.chat.simple.Lo" +
      "ginReqH\000\022%\n\006msgReq\030\003 \001(\0132\023.chat.simple.M" +
      "sgReqH\000\022%\n\006msgRes\030\004 \001(\0132\023.chat.simple.Ms" +
      "gResH\000\022)\n\010loginRes\030\005 \001(\0132\025.chat.simple.L" +
      "oginResH\000\022#\n\005msgRX\030\006 \001(\0132\022.chat.simple.M" +
      "sgRXH\000\0225\n\016groupCreateReq\030\007 \001(\0132\033.chat.si" +
      "mple.GroupCreateReqH\000\0221\n\014groupJoinReq\030\010 " +
      "\001(\0132\031.chat.simple.GroupJoinReqH\000\0221\n\014grou" +
      "pQuitReq\030\t \001(\0132\031.chat.simple.GroupQuitRe" +
      "qH\000\022?\n\023groupMemberQueryReq\030\n \001(\0132 .chat." +
      "simple.GroupMemberQueryReqH\000\022?\n\023groupMem" +
      "berQueryRes\030\013 \001(\0132 .chat.simple.GroupMem" +
      "berQueryResH\000\0227\n\017groupMessageReq\030\014 \001(\0132\034" +
      ".chat.simple.GroupMessageReqH\000\0225\n\016groupM" +
      "essageRX\030\r \001(\0132\033.chat.simple.GroupMessag" +
      "eRXH\000\022)\n\010groupRes\030\016 \001(\0132\025.chat.simple.Gr" +
      "oupResH\000\0229\n\rgroupQueryReq\030\017 \001(\0132 .chat.s" +
      "imple.GroupJoinedQueryReqH\000\0229\n\rgroupQuer" +
      "yRes\030\020 \001(\0132 .chat.simple.GroupJoinedQuer" +
      "yResH\000\"\275\002\n\013MessageType\022\r\n\tLOGIN_REQ\020\000\022\r\n" +
      "\tLOGIN_RES\020\001\022\013\n\007MSG_REQ\020\002\022\013\n\007MSG_RES\020\003\022\n" +
      "\n\006MSG_RX\020\004\022\024\n\020GROUP_CREATE_REQ\020\005\022\022\n\016GROU" +
      "P_JOIN_REQ\020\006\022\022\n\016GROUP_QUIT_REQ\020\007\022\032\n\026GROU" +
      "P_MEMBER_QUERY_REQ\020\010\022\032\n\026GROUP_MEMBER_QUE" +
      "RY_RES\020\t\022\021\n\rGROUP_MSG_REQ\020\n\022\020\n\014GROUP_MSG" +
      "_RX\020\013\022\r\n\tGROUP_RES\020\014\022\032\n\026GROUP_JOINED_QUE" +
      "RY_REQ\020\r\022\032\n\026GROUP_JOINED_QUERY_RES\020\016\022\010\n\004" +
      "PING\020\017B\r\n\013messageBody\".\n\010LoginReq\022\020\n\010use" +
      "rname\030\001 \001(\t\022\020\n\010password\030\002 \001(\t\"\205\001\n\010LoginR" +
      "es\0221\n\006status\030\001 \001(\0162!.chat.simple.LoginRe" +
      "s.LoginStatus\022\020\n\010response\030\002 \001(\t\022\016\n\006userI" +
      "d\030\003 \001(\005\"$\n\013LoginStatus\022\013\n\007SUCCESS\020\000\022\010\n\004F" +
      "AIL\020\001\"7\n\006MsgReq\022\017\n\007sUserId\030\001 \001(\005\022\017\n\007dUse" +
      "rId\030\002 \001(\005\022\013\n\003msg\030\003 \001(\t\"g\n\006MsgRes\022*\n\006stat" +
      "us\030\001 \001(\0162\032.chat.simple.MsgRes.Status\022\020\n\010" +
      "response\030\002 \001(\t\"\037\n\006Status\022\013\n\007SUCCESS\020\000\022\010\n" +
      "\004FAIL\020\001\")\n\005MsgRX\022\017\n\007content\030\001 \001(\t\022\017\n\007sUs" +
      "erId\030\002 \001(\005\"1\n\016GroupCreateReq\022\017\n\007groupId\030" +
      "\001 \001(\005\022\016\n\006userId\030\002 \001(\005\".\n\014GroupJoinReq\022\016\n" +
      "\006joinId\030\001 \001(\005\022\016\n\006userId\030\002 \001(\005\"/\n\014GroupQu" +
      "itReq\022\017\n\007groupId\030\001 \001(\005\022\016\n\006userId\030\002 \001(\005\"&" +
      "\n\023GroupMemberQueryReq\022\017\n\007groupId\030\001 \001(\005\"E" +
      "\n\023GroupMemberQueryRes\022\016\n\006status\030\001 \001(\010\022\016\n" +
      "\006reason\030\002 \001(\t\022\016\n\006userId\030\003 \003(\005\"%\n\023GroupJo" +
      "inedQueryReq\022\016\n\006userId\030\001 \001(\005\"F\n\023GroupJoi" +
      "nedQueryRes\022\016\n\006status\030\001 \001(\010\022\016\n\006reason\030\002 " +
      "\001(\t\022\017\n\007groupId\030\003 \003(\005\"D\n\017GroupMessageReq\022" +
      "\017\n\007groupId\030\001 \001(\005\022\017\n\007sUserId\030\002 \001(\005\022\017\n\007mes" +
      "sage\030\003 \001(\t\"C\n\016GroupMessageRX\022\017\n\007sUserId\030" +
      "\001 \001(\005\022\017\n\007groupId\030\002 \001(\005\022\017\n\007message\030\003 \001(\t\"" +
      "*\n\010GroupRes\022\016\n\006status\030\001 \001(\010\022\016\n\006reason\030\002 " +
      "\001(\tB%\n\023com.example.messageB\014MessageProto" +
      "P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_chat_simple_Message_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_chat_simple_Message_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_Message_descriptor,
        new java.lang.String[] { "MessageType", "LoginReq", "MsgReq", "MsgRes", "LoginRes", "MsgRX", "GroupCreateReq", "GroupJoinReq", "GroupQuitReq", "GroupMemberQueryReq", "GroupMemberQueryRes", "GroupMessageReq", "GroupMessageRX", "GroupRes", "GroupQueryReq", "GroupQueryRes", "MessageBody", });
    internal_static_chat_simple_LoginReq_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_chat_simple_LoginReq_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_LoginReq_descriptor,
        new java.lang.String[] { "Username", "Password", });
    internal_static_chat_simple_LoginRes_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_chat_simple_LoginRes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_LoginRes_descriptor,
        new java.lang.String[] { "Status", "Response", "UserId", });
    internal_static_chat_simple_MsgReq_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_chat_simple_MsgReq_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_MsgReq_descriptor,
        new java.lang.String[] { "SUserId", "DUserId", "Msg", });
    internal_static_chat_simple_MsgRes_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_chat_simple_MsgRes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_MsgRes_descriptor,
        new java.lang.String[] { "Status", "Response", });
    internal_static_chat_simple_MsgRX_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_chat_simple_MsgRX_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_MsgRX_descriptor,
        new java.lang.String[] { "Content", "SUserId", });
    internal_static_chat_simple_GroupCreateReq_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_chat_simple_GroupCreateReq_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupCreateReq_descriptor,
        new java.lang.String[] { "GroupId", "UserId", });
    internal_static_chat_simple_GroupJoinReq_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_chat_simple_GroupJoinReq_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupJoinReq_descriptor,
        new java.lang.String[] { "JoinId", "UserId", });
    internal_static_chat_simple_GroupQuitReq_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_chat_simple_GroupQuitReq_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupQuitReq_descriptor,
        new java.lang.String[] { "GroupId", "UserId", });
    internal_static_chat_simple_GroupMemberQueryReq_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_chat_simple_GroupMemberQueryReq_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupMemberQueryReq_descriptor,
        new java.lang.String[] { "GroupId", });
    internal_static_chat_simple_GroupMemberQueryRes_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_chat_simple_GroupMemberQueryRes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupMemberQueryRes_descriptor,
        new java.lang.String[] { "Status", "Reason", "UserId", });
    internal_static_chat_simple_GroupJoinedQueryReq_descriptor =
      getDescriptor().getMessageTypes().get(11);
    internal_static_chat_simple_GroupJoinedQueryReq_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupJoinedQueryReq_descriptor,
        new java.lang.String[] { "UserId", });
    internal_static_chat_simple_GroupJoinedQueryRes_descriptor =
      getDescriptor().getMessageTypes().get(12);
    internal_static_chat_simple_GroupJoinedQueryRes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupJoinedQueryRes_descriptor,
        new java.lang.String[] { "Status", "Reason", "GroupId", });
    internal_static_chat_simple_GroupMessageReq_descriptor =
      getDescriptor().getMessageTypes().get(13);
    internal_static_chat_simple_GroupMessageReq_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupMessageReq_descriptor,
        new java.lang.String[] { "GroupId", "SUserId", "Message", });
    internal_static_chat_simple_GroupMessageRX_descriptor =
      getDescriptor().getMessageTypes().get(14);
    internal_static_chat_simple_GroupMessageRX_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupMessageRX_descriptor,
        new java.lang.String[] { "SUserId", "GroupId", "Message", });
    internal_static_chat_simple_GroupRes_descriptor =
      getDescriptor().getMessageTypes().get(15);
    internal_static_chat_simple_GroupRes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_chat_simple_GroupRes_descriptor,
        new java.lang.String[] { "Status", "Reason", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
