// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/resources/message.proto

package com.example.message;

public interface GroupMemberQueryResOrBuilder extends
    // @@protoc_insertion_point(interface_extends:chat.simple.GroupMemberQueryRes)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bool status = 1;</code>
   * @return The status.
   */
  boolean getStatus();

  /**
   * <code>string reason = 2;</code>
   * @return The reason.
   */
  java.lang.String getReason();
  /**
   * <code>string reason = 2;</code>
   * @return The bytes for reason.
   */
  com.google.protobuf.ByteString
      getReasonBytes();

  /**
   * <code>repeated string username = 3;</code>
   * @return A list containing the username.
   */
  java.util.List<java.lang.String>
      getUsernameList();
  /**
   * <code>repeated string username = 3;</code>
   * @return The count of username.
   */
  int getUsernameCount();
  /**
   * <code>repeated string username = 3;</code>
   * @param index The index of the element to return.
   * @return The username at the given index.
   */
  java.lang.String getUsername(int index);
  /**
   * <code>repeated string username = 3;</code>
   * @param index The index of the value to return.
   * @return The bytes of the username at the given index.
   */
  com.google.protobuf.ByteString
      getUsernameBytes(int index);
}
