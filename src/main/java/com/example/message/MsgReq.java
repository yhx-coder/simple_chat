// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/resources/message.proto

package com.example.message;

/**
 * <pre>
 * 消息发送请求
 * </pre>
 *
 * Protobuf type {@code chat.simple.MsgReq}
 */
public final class MsgReq extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:chat.simple.MsgReq)
    MsgReqOrBuilder {
private static final long serialVersionUID = 0L;
  // Use MsgReq.newBuilder() to construct.
  private MsgReq(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MsgReq() {
    dUsername_ = "";
    msg_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new MsgReq();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private MsgReq(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            dUsername_ = s;
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            msg_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.example.message.MessageProto.internal_static_chat_simple_MsgReq_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.example.message.MessageProto.internal_static_chat_simple_MsgReq_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.example.message.MsgReq.class, com.example.message.MsgReq.Builder.class);
  }

  public static final int DUSERNAME_FIELD_NUMBER = 2;
  private volatile java.lang.Object dUsername_;
  /**
   * <code>string dUsername = 2;</code>
   * @return The dUsername.
   */
  @java.lang.Override
  public java.lang.String getDUsername() {
    java.lang.Object ref = dUsername_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      dUsername_ = s;
      return s;
    }
  }
  /**
   * <code>string dUsername = 2;</code>
   * @return The bytes for dUsername.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getDUsernameBytes() {
    java.lang.Object ref = dUsername_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      dUsername_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int MSG_FIELD_NUMBER = 3;
  private volatile java.lang.Object msg_;
  /**
   * <code>string msg = 3;</code>
   * @return The msg.
   */
  @java.lang.Override
  public java.lang.String getMsg() {
    java.lang.Object ref = msg_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      msg_ = s;
      return s;
    }
  }
  /**
   * <code>string msg = 3;</code>
   * @return The bytes for msg.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getMsgBytes() {
    java.lang.Object ref = msg_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      msg_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getDUsernameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, dUsername_);
    }
    if (!getMsgBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, msg_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getDUsernameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, dUsername_);
    }
    if (!getMsgBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, msg_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.example.message.MsgReq)) {
      return super.equals(obj);
    }
    com.example.message.MsgReq other = (com.example.message.MsgReq) obj;

    if (!getDUsername()
        .equals(other.getDUsername())) return false;
    if (!getMsg()
        .equals(other.getMsg())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + DUSERNAME_FIELD_NUMBER;
    hash = (53 * hash) + getDUsername().hashCode();
    hash = (37 * hash) + MSG_FIELD_NUMBER;
    hash = (53 * hash) + getMsg().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.example.message.MsgReq parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.message.MsgReq parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.message.MsgReq parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.message.MsgReq parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.message.MsgReq parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.message.MsgReq parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.message.MsgReq parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.message.MsgReq parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.message.MsgReq parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.example.message.MsgReq parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.message.MsgReq parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.message.MsgReq parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.example.message.MsgReq prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * 消息发送请求
   * </pre>
   *
   * Protobuf type {@code chat.simple.MsgReq}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:chat.simple.MsgReq)
      com.example.message.MsgReqOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.example.message.MessageProto.internal_static_chat_simple_MsgReq_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.example.message.MessageProto.internal_static_chat_simple_MsgReq_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.example.message.MsgReq.class, com.example.message.MsgReq.Builder.class);
    }

    // Construct using com.example.message.MsgReq.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      dUsername_ = "";

      msg_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.example.message.MessageProto.internal_static_chat_simple_MsgReq_descriptor;
    }

    @java.lang.Override
    public com.example.message.MsgReq getDefaultInstanceForType() {
      return com.example.message.MsgReq.getDefaultInstance();
    }

    @java.lang.Override
    public com.example.message.MsgReq build() {
      com.example.message.MsgReq result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.example.message.MsgReq buildPartial() {
      com.example.message.MsgReq result = new com.example.message.MsgReq(this);
      result.dUsername_ = dUsername_;
      result.msg_ = msg_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.example.message.MsgReq) {
        return mergeFrom((com.example.message.MsgReq)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.example.message.MsgReq other) {
      if (other == com.example.message.MsgReq.getDefaultInstance()) return this;
      if (!other.getDUsername().isEmpty()) {
        dUsername_ = other.dUsername_;
        onChanged();
      }
      if (!other.getMsg().isEmpty()) {
        msg_ = other.msg_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.example.message.MsgReq parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.example.message.MsgReq) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object dUsername_ = "";
    /**
     * <code>string dUsername = 2;</code>
     * @return The dUsername.
     */
    public java.lang.String getDUsername() {
      java.lang.Object ref = dUsername_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        dUsername_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string dUsername = 2;</code>
     * @return The bytes for dUsername.
     */
    public com.google.protobuf.ByteString
        getDUsernameBytes() {
      java.lang.Object ref = dUsername_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        dUsername_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string dUsername = 2;</code>
     * @param value The dUsername to set.
     * @return This builder for chaining.
     */
    public Builder setDUsername(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      dUsername_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string dUsername = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearDUsername() {
      
      dUsername_ = getDefaultInstance().getDUsername();
      onChanged();
      return this;
    }
    /**
     * <code>string dUsername = 2;</code>
     * @param value The bytes for dUsername to set.
     * @return This builder for chaining.
     */
    public Builder setDUsernameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      dUsername_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object msg_ = "";
    /**
     * <code>string msg = 3;</code>
     * @return The msg.
     */
    public java.lang.String getMsg() {
      java.lang.Object ref = msg_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        msg_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string msg = 3;</code>
     * @return The bytes for msg.
     */
    public com.google.protobuf.ByteString
        getMsgBytes() {
      java.lang.Object ref = msg_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        msg_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string msg = 3;</code>
     * @param value The msg to set.
     * @return This builder for chaining.
     */
    public Builder setMsg(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      msg_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string msg = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearMsg() {
      
      msg_ = getDefaultInstance().getMsg();
      onChanged();
      return this;
    }
    /**
     * <code>string msg = 3;</code>
     * @param value The bytes for msg to set.
     * @return This builder for chaining.
     */
    public Builder setMsgBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      msg_ = value;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:chat.simple.MsgReq)
  }

  // @@protoc_insertion_point(class_scope:chat.simple.MsgReq)
  private static final com.example.message.MsgReq DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.example.message.MsgReq();
  }

  public static com.example.message.MsgReq getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MsgReq>
      PARSER = new com.google.protobuf.AbstractParser<MsgReq>() {
    @java.lang.Override
    public MsgReq parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new MsgReq(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<MsgReq> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MsgReq> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.example.message.MsgReq getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

