// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/resources/message.proto

package com.example.message;

public interface GroupMessageReqOrBuilder extends
    // @@protoc_insertion_point(interface_extends:chat.simple.GroupMessageReq)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 groupId = 1;</code>
   * @return The groupId.
   */
  int getGroupId();

  /**
   * <code>int32 sUserId = 2;</code>
   * @return The sUserId.
   */
  int getSUserId();

  /**
   * <code>string message = 3;</code>
   * @return The message.
   */
  java.lang.String getMessage();
  /**
   * <code>string message = 3;</code>
   * @return The bytes for message.
   */
  com.google.protobuf.ByteString
      getMessageBytes();
}
