// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: small_chat/src/main/resources/message.proto

package com.example.message;

public interface MessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:chat.simple.Message)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.chat.simple.Message.MessageType messageType = 1;</code>
   * @return The enum numeric value on the wire for messageType.
   */
  int getMessageTypeValue();
  /**
   * <code>.chat.simple.Message.MessageType messageType = 1;</code>
   * @return The messageType.
   */
  com.example.message.Message.MessageType getMessageType();

  /**
   * <code>.chat.simple.LoginReq loginReq = 2;</code>
   * @return Whether the loginReq field is set.
   */
  boolean hasLoginReq();
  /**
   * <code>.chat.simple.LoginReq loginReq = 2;</code>
   * @return The loginReq.
   */
  com.example.message.LoginReq getLoginReq();
  /**
   * <code>.chat.simple.LoginReq loginReq = 2;</code>
   */
  com.example.message.LoginReqOrBuilder getLoginReqOrBuilder();

  /**
   * <code>.chat.simple.MsgReq msgReq = 3;</code>
   * @return Whether the msgReq field is set.
   */
  boolean hasMsgReq();
  /**
   * <code>.chat.simple.MsgReq msgReq = 3;</code>
   * @return The msgReq.
   */
  com.example.message.MsgReq getMsgReq();
  /**
   * <code>.chat.simple.MsgReq msgReq = 3;</code>
   */
  com.example.message.MsgReqOrBuilder getMsgReqOrBuilder();

  /**
   * <code>.chat.simple.MsgRes msgRes = 4;</code>
   * @return Whether the msgRes field is set.
   */
  boolean hasMsgRes();
  /**
   * <code>.chat.simple.MsgRes msgRes = 4;</code>
   * @return The msgRes.
   */
  com.example.message.MsgRes getMsgRes();
  /**
   * <code>.chat.simple.MsgRes msgRes = 4;</code>
   */
  com.example.message.MsgResOrBuilder getMsgResOrBuilder();

  /**
   * <code>.chat.simple.LoginRes loginRes = 5;</code>
   * @return Whether the loginRes field is set.
   */
  boolean hasLoginRes();
  /**
   * <code>.chat.simple.LoginRes loginRes = 5;</code>
   * @return The loginRes.
   */
  com.example.message.LoginRes getLoginRes();
  /**
   * <code>.chat.simple.LoginRes loginRes = 5;</code>
   */
  com.example.message.LoginResOrBuilder getLoginResOrBuilder();

  /**
   * <code>.chat.simple.MsgRX msgRX = 6;</code>
   * @return Whether the msgRX field is set.
   */
  boolean hasMsgRX();
  /**
   * <code>.chat.simple.MsgRX msgRX = 6;</code>
   * @return The msgRX.
   */
  com.example.message.MsgRX getMsgRX();
  /**
   * <code>.chat.simple.MsgRX msgRX = 6;</code>
   */
  com.example.message.MsgRXOrBuilder getMsgRXOrBuilder();

  /**
   * <code>.chat.simple.GroupCreateReq groupCreateReq = 7;</code>
   * @return Whether the groupCreateReq field is set.
   */
  boolean hasGroupCreateReq();
  /**
   * <code>.chat.simple.GroupCreateReq groupCreateReq = 7;</code>
   * @return The groupCreateReq.
   */
  com.example.message.GroupCreateReq getGroupCreateReq();
  /**
   * <code>.chat.simple.GroupCreateReq groupCreateReq = 7;</code>
   */
  com.example.message.GroupCreateReqOrBuilder getGroupCreateReqOrBuilder();

  /**
   * <code>.chat.simple.GroupJoinReq groupJoinReq = 8;</code>
   * @return Whether the groupJoinReq field is set.
   */
  boolean hasGroupJoinReq();
  /**
   * <code>.chat.simple.GroupJoinReq groupJoinReq = 8;</code>
   * @return The groupJoinReq.
   */
  com.example.message.GroupJoinReq getGroupJoinReq();
  /**
   * <code>.chat.simple.GroupJoinReq groupJoinReq = 8;</code>
   */
  com.example.message.GroupJoinReqOrBuilder getGroupJoinReqOrBuilder();

  /**
   * <code>.chat.simple.GroupQuitReq groupQuitReq = 9;</code>
   * @return Whether the groupQuitReq field is set.
   */
  boolean hasGroupQuitReq();
  /**
   * <code>.chat.simple.GroupQuitReq groupQuitReq = 9;</code>
   * @return The groupQuitReq.
   */
  com.example.message.GroupQuitReq getGroupQuitReq();
  /**
   * <code>.chat.simple.GroupQuitReq groupQuitReq = 9;</code>
   */
  com.example.message.GroupQuitReqOrBuilder getGroupQuitReqOrBuilder();

  /**
   * <code>.chat.simple.GroupMemberQueryReq groupMemberQueryReq = 10;</code>
   * @return Whether the groupMemberQueryReq field is set.
   */
  boolean hasGroupMemberQueryReq();
  /**
   * <code>.chat.simple.GroupMemberQueryReq groupMemberQueryReq = 10;</code>
   * @return The groupMemberQueryReq.
   */
  com.example.message.GroupMemberQueryReq getGroupMemberQueryReq();
  /**
   * <code>.chat.simple.GroupMemberQueryReq groupMemberQueryReq = 10;</code>
   */
  com.example.message.GroupMemberQueryReqOrBuilder getGroupMemberQueryReqOrBuilder();

  /**
   * <code>.chat.simple.GroupMemberQueryRes groupMemberQueryRes = 11;</code>
   * @return Whether the groupMemberQueryRes field is set.
   */
  boolean hasGroupMemberQueryRes();
  /**
   * <code>.chat.simple.GroupMemberQueryRes groupMemberQueryRes = 11;</code>
   * @return The groupMemberQueryRes.
   */
  com.example.message.GroupMemberQueryRes getGroupMemberQueryRes();
  /**
   * <code>.chat.simple.GroupMemberQueryRes groupMemberQueryRes = 11;</code>
   */
  com.example.message.GroupMemberQueryResOrBuilder getGroupMemberQueryResOrBuilder();

  /**
   * <code>.chat.simple.GroupMessageReq groupMessageReq = 12;</code>
   * @return Whether the groupMessageReq field is set.
   */
  boolean hasGroupMessageReq();
  /**
   * <code>.chat.simple.GroupMessageReq groupMessageReq = 12;</code>
   * @return The groupMessageReq.
   */
  com.example.message.GroupMessageReq getGroupMessageReq();
  /**
   * <code>.chat.simple.GroupMessageReq groupMessageReq = 12;</code>
   */
  com.example.message.GroupMessageReqOrBuilder getGroupMessageReqOrBuilder();

  /**
   * <code>.chat.simple.GroupMessageRX groupMessageRX = 13;</code>
   * @return Whether the groupMessageRX field is set.
   */
  boolean hasGroupMessageRX();
  /**
   * <code>.chat.simple.GroupMessageRX groupMessageRX = 13;</code>
   * @return The groupMessageRX.
   */
  com.example.message.GroupMessageRX getGroupMessageRX();
  /**
   * <code>.chat.simple.GroupMessageRX groupMessageRX = 13;</code>
   */
  com.example.message.GroupMessageRXOrBuilder getGroupMessageRXOrBuilder();

  /**
   * <code>.chat.simple.GroupRes groupRes = 14;</code>
   * @return Whether the groupRes field is set.
   */
  boolean hasGroupRes();
  /**
   * <code>.chat.simple.GroupRes groupRes = 14;</code>
   * @return The groupRes.
   */
  com.example.message.GroupRes getGroupRes();
  /**
   * <code>.chat.simple.GroupRes groupRes = 14;</code>
   */
  com.example.message.GroupResOrBuilder getGroupResOrBuilder();

  /**
   * <code>.chat.simple.GroupJoinedQueryReq groupQueryReq = 15;</code>
   * @return Whether the groupQueryReq field is set.
   */
  boolean hasGroupQueryReq();
  /**
   * <code>.chat.simple.GroupJoinedQueryReq groupQueryReq = 15;</code>
   * @return The groupQueryReq.
   */
  com.example.message.GroupJoinedQueryReq getGroupQueryReq();
  /**
   * <code>.chat.simple.GroupJoinedQueryReq groupQueryReq = 15;</code>
   */
  com.example.message.GroupJoinedQueryReqOrBuilder getGroupQueryReqOrBuilder();

  /**
   * <code>.chat.simple.GroupJoinedQueryRes groupQueryRes = 16;</code>
   * @return Whether the groupQueryRes field is set.
   */
  boolean hasGroupQueryRes();
  /**
   * <code>.chat.simple.GroupJoinedQueryRes groupQueryRes = 16;</code>
   * @return The groupQueryRes.
   */
  com.example.message.GroupJoinedQueryRes getGroupQueryRes();
  /**
   * <code>.chat.simple.GroupJoinedQueryRes groupQueryRes = 16;</code>
   */
  com.example.message.GroupJoinedQueryResOrBuilder getGroupQueryResOrBuilder();

  /**
   * <code>.chat.simple.GroupCreateRes groupCreateRes = 17;</code>
   * @return Whether the groupCreateRes field is set.
   */
  boolean hasGroupCreateRes();
  /**
   * <code>.chat.simple.GroupCreateRes groupCreateRes = 17;</code>
   * @return The groupCreateRes.
   */
  com.example.message.GroupCreateRes getGroupCreateRes();
  /**
   * <code>.chat.simple.GroupCreateRes groupCreateRes = 17;</code>
   */
  com.example.message.GroupCreateResOrBuilder getGroupCreateResOrBuilder();

  /**
   * <code>.chat.simple.GroupJoinRes groupJoinRes = 18;</code>
   * @return Whether the groupJoinRes field is set.
   */
  boolean hasGroupJoinRes();
  /**
   * <code>.chat.simple.GroupJoinRes groupJoinRes = 18;</code>
   * @return The groupJoinRes.
   */
  com.example.message.GroupJoinRes getGroupJoinRes();
  /**
   * <code>.chat.simple.GroupJoinRes groupJoinRes = 18;</code>
   */
  com.example.message.GroupJoinResOrBuilder getGroupJoinResOrBuilder();

  /**
   * <code>.chat.simple.GroupQuitRes groupQuitRes = 19;</code>
   * @return Whether the groupQuitRes field is set.
   */
  boolean hasGroupQuitRes();
  /**
   * <code>.chat.simple.GroupQuitRes groupQuitRes = 19;</code>
   * @return The groupQuitRes.
   */
  com.example.message.GroupQuitRes getGroupQuitRes();
  /**
   * <code>.chat.simple.GroupQuitRes groupQuitRes = 19;</code>
   */
  com.example.message.GroupQuitResOrBuilder getGroupQuitResOrBuilder();

  public com.example.message.Message.MessageBodyCase getMessageBodyCase();
}
