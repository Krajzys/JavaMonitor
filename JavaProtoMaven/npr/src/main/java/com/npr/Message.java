// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Message.proto

package com.npr;

public final class Message {
  private Message() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface LamportOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.npr.Lamport)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>.com.npr.Lamport.MessageType message_type = 1;</code>
     * @return The enum numeric value on the wire for messageType.
     */
    int getMessageTypeValue();
    /**
     * <code>.com.npr.Lamport.MessageType message_type = 1;</code>
     * @return The messageType.
     */
    com.npr.Message.Lamport.MessageType getMessageType();

    /**
     * <code>int64 clock = 2;</code>
     * @return The clock.
     */
    long getClock();

    /**
     * <code>int32 id_sender = 3;</code>
     * @return The idSender.
     */
    int getIdSender();

    /**
     * <code>int32 id_receiver = 4;</code>
     * @return The idReceiver.
     */
    int getIdReceiver();
  }
  /**
   * Protobuf type {@code com.npr.Lamport}
   */
  public static final class Lamport extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.npr.Lamport)
      LamportOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Lamport.newBuilder() to construct.
    private Lamport(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Lamport() {
      messageType_ = 0;
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new Lamport();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Lamport(
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
            case 8: {
              int rawValue = input.readEnum();

              messageType_ = rawValue;
              break;
            }
            case 16: {

              clock_ = input.readInt64();
              break;
            }
            case 24: {

              idSender_ = input.readInt32();
              break;
            }
            case 32: {

              idReceiver_ = input.readInt32();
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
      return com.npr.Message.internal_static_com_npr_Lamport_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.npr.Message.internal_static_com_npr_Lamport_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.npr.Message.Lamport.class, com.npr.Message.Lamport.Builder.class);
    }

    /**
     * Protobuf enum {@code com.npr.Lamport.MessageType}
     */
    public enum MessageType
        implements com.google.protobuf.ProtocolMessageEnum {
      /**
       * <code>REQUEST_INITIAL = 0;</code>
       */
      REQUEST_INITIAL(0),
      /**
       * <code>REPLY_INITIAL = 1;</code>
       */
      REPLY_INITIAL(1),
      /**
       * <code>RELEASE_INITIAL = 2;</code>
       */
      RELEASE_INITIAL(2),
      /**
       * <code>REQUEST = 3;</code>
       */
      REQUEST(3),
      /**
       * <code>REPLY = 4;</code>
       */
      REPLY(4),
      /**
       * <code>RELEASE = 5;</code>
       */
      RELEASE(5),
      /**
       * <code>SLEEP = 6;</code>
       */
      SLEEP(6),
      /**
       * <code>WAKE = 7;</code>
       */
      WAKE(7),
      /**
       * <code>END_COMMUNICATION = 8;</code>
       */
      END_COMMUNICATION(8),
      UNRECOGNIZED(-1),
      ;

      /**
       * <code>REQUEST_INITIAL = 0;</code>
       */
      public static final int REQUEST_INITIAL_VALUE = 0;
      /**
       * <code>REPLY_INITIAL = 1;</code>
       */
      public static final int REPLY_INITIAL_VALUE = 1;
      /**
       * <code>RELEASE_INITIAL = 2;</code>
       */
      public static final int RELEASE_INITIAL_VALUE = 2;
      /**
       * <code>REQUEST = 3;</code>
       */
      public static final int REQUEST_VALUE = 3;
      /**
       * <code>REPLY = 4;</code>
       */
      public static final int REPLY_VALUE = 4;
      /**
       * <code>RELEASE = 5;</code>
       */
      public static final int RELEASE_VALUE = 5;
      /**
       * <code>SLEEP = 6;</code>
       */
      public static final int SLEEP_VALUE = 6;
      /**
       * <code>WAKE = 7;</code>
       */
      public static final int WAKE_VALUE = 7;
      /**
       * <code>END_COMMUNICATION = 8;</code>
       */
      public static final int END_COMMUNICATION_VALUE = 8;


      public final int getNumber() {
        if (this == UNRECOGNIZED) {
          throw new java.lang.IllegalArgumentException(
              "Can't get the number of an unknown enum value.");
        }
        return value;
      }

      /**
       * @param value The numeric wire value of the corresponding enum entry.
       * @return The enum associated with the given numeric wire value.
       * @deprecated Use {@link #forNumber(int)} instead.
       */
      @java.lang.Deprecated
      public static MessageType valueOf(int value) {
        return forNumber(value);
      }

      /**
       * @param value The numeric wire value of the corresponding enum entry.
       * @return The enum associated with the given numeric wire value.
       */
      public static MessageType forNumber(int value) {
        switch (value) {
          case 0: return REQUEST_INITIAL;
          case 1: return REPLY_INITIAL;
          case 2: return RELEASE_INITIAL;
          case 3: return REQUEST;
          case 4: return REPLY;
          case 5: return RELEASE;
          case 6: return SLEEP;
          case 7: return WAKE;
          case 8: return END_COMMUNICATION;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<MessageType>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static final com.google.protobuf.Internal.EnumLiteMap<
          MessageType> internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<MessageType>() {
              public MessageType findValueByNumber(int number) {
                return MessageType.forNumber(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        if (this == UNRECOGNIZED) {
          throw new java.lang.IllegalStateException(
              "Can't get the descriptor of an unrecognized enum value.");
        }
        return getDescriptor().getValues().get(ordinal());
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return com.npr.Message.Lamport.getDescriptor().getEnumTypes().get(0);
      }

      private static final MessageType[] VALUES = values();

      public static MessageType valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        if (desc.getIndex() == -1) {
          return UNRECOGNIZED;
        }
        return VALUES[desc.getIndex()];
      }

      private final int value;

      private MessageType(int value) {
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:com.npr.Lamport.MessageType)
    }

    public static final int MESSAGE_TYPE_FIELD_NUMBER = 1;
    private int messageType_;
    /**
     * <code>.com.npr.Lamport.MessageType message_type = 1;</code>
     * @return The enum numeric value on the wire for messageType.
     */
    @java.lang.Override public int getMessageTypeValue() {
      return messageType_;
    }
    /**
     * <code>.com.npr.Lamport.MessageType message_type = 1;</code>
     * @return The messageType.
     */
    @java.lang.Override public com.npr.Message.Lamport.MessageType getMessageType() {
      @SuppressWarnings("deprecation")
      com.npr.Message.Lamport.MessageType result = com.npr.Message.Lamport.MessageType.valueOf(messageType_);
      return result == null ? com.npr.Message.Lamport.MessageType.UNRECOGNIZED : result;
    }

    public static final int CLOCK_FIELD_NUMBER = 2;
    private long clock_;
    /**
     * <code>int64 clock = 2;</code>
     * @return The clock.
     */
    @java.lang.Override
    public long getClock() {
      return clock_;
    }

    public static final int ID_SENDER_FIELD_NUMBER = 3;
    private int idSender_;
    /**
     * <code>int32 id_sender = 3;</code>
     * @return The idSender.
     */
    @java.lang.Override
    public int getIdSender() {
      return idSender_;
    }

    public static final int ID_RECEIVER_FIELD_NUMBER = 4;
    private int idReceiver_;
    /**
     * <code>int32 id_receiver = 4;</code>
     * @return The idReceiver.
     */
    @java.lang.Override
    public int getIdReceiver() {
      return idReceiver_;
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
      if (messageType_ != com.npr.Message.Lamport.MessageType.REQUEST_INITIAL.getNumber()) {
        output.writeEnum(1, messageType_);
      }
      if (clock_ != 0L) {
        output.writeInt64(2, clock_);
      }
      if (idSender_ != 0) {
        output.writeInt32(3, idSender_);
      }
      if (idReceiver_ != 0) {
        output.writeInt32(4, idReceiver_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (messageType_ != com.npr.Message.Lamport.MessageType.REQUEST_INITIAL.getNumber()) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(1, messageType_);
      }
      if (clock_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, clock_);
      }
      if (idSender_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, idSender_);
      }
      if (idReceiver_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, idReceiver_);
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
      if (!(obj instanceof com.npr.Message.Lamport)) {
        return super.equals(obj);
      }
      com.npr.Message.Lamport other = (com.npr.Message.Lamport) obj;

      if (messageType_ != other.messageType_) return false;
      if (getClock()
          != other.getClock()) return false;
      if (getIdSender()
          != other.getIdSender()) return false;
      if (getIdReceiver()
          != other.getIdReceiver()) return false;
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
      hash = (37 * hash) + MESSAGE_TYPE_FIELD_NUMBER;
      hash = (53 * hash) + messageType_;
      hash = (37 * hash) + CLOCK_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getClock());
      hash = (37 * hash) + ID_SENDER_FIELD_NUMBER;
      hash = (53 * hash) + getIdSender();
      hash = (37 * hash) + ID_RECEIVER_FIELD_NUMBER;
      hash = (53 * hash) + getIdReceiver();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.npr.Message.Lamport parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.npr.Message.Lamport parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.npr.Message.Lamport parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.npr.Message.Lamport parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.npr.Message.Lamport parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.npr.Message.Lamport parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.npr.Message.Lamport parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.npr.Message.Lamport parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.npr.Message.Lamport parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.npr.Message.Lamport parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.npr.Message.Lamport parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.npr.Message.Lamport parseFrom(
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
    public static Builder newBuilder(com.npr.Message.Lamport prototype) {
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
     * Protobuf type {@code com.npr.Lamport}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.npr.Lamport)
        com.npr.Message.LamportOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.npr.Message.internal_static_com_npr_Lamport_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.npr.Message.internal_static_com_npr_Lamport_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.npr.Message.Lamport.class, com.npr.Message.Lamport.Builder.class);
      }

      // Construct using com.npr.Message.Lamport.newBuilder()
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
        messageType_ = 0;

        clock_ = 0L;

        idSender_ = 0;

        idReceiver_ = 0;

        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.npr.Message.internal_static_com_npr_Lamport_descriptor;
      }

      @java.lang.Override
      public com.npr.Message.Lamport getDefaultInstanceForType() {
        return com.npr.Message.Lamport.getDefaultInstance();
      }

      @java.lang.Override
      public com.npr.Message.Lamport build() {
        com.npr.Message.Lamport result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public com.npr.Message.Lamport buildPartial() {
        com.npr.Message.Lamport result = new com.npr.Message.Lamport(this);
        result.messageType_ = messageType_;
        result.clock_ = clock_;
        result.idSender_ = idSender_;
        result.idReceiver_ = idReceiver_;
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
        if (other instanceof com.npr.Message.Lamport) {
          return mergeFrom((com.npr.Message.Lamport)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.npr.Message.Lamport other) {
        if (other == com.npr.Message.Lamport.getDefaultInstance()) return this;
        if (other.messageType_ != 0) {
          setMessageTypeValue(other.getMessageTypeValue());
        }
        if (other.getClock() != 0L) {
          setClock(other.getClock());
        }
        if (other.getIdSender() != 0) {
          setIdSender(other.getIdSender());
        }
        if (other.getIdReceiver() != 0) {
          setIdReceiver(other.getIdReceiver());
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
        com.npr.Message.Lamport parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.npr.Message.Lamport) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int messageType_ = 0;
      /**
       * <code>.com.npr.Lamport.MessageType message_type = 1;</code>
       * @return The enum numeric value on the wire for messageType.
       */
      @java.lang.Override public int getMessageTypeValue() {
        return messageType_;
      }
      /**
       * <code>.com.npr.Lamport.MessageType message_type = 1;</code>
       * @param value The enum numeric value on the wire for messageType to set.
       * @return This builder for chaining.
       */
      public Builder setMessageTypeValue(int value) {
        
        messageType_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>.com.npr.Lamport.MessageType message_type = 1;</code>
       * @return The messageType.
       */
      @java.lang.Override
      public com.npr.Message.Lamport.MessageType getMessageType() {
        @SuppressWarnings("deprecation")
        com.npr.Message.Lamport.MessageType result = com.npr.Message.Lamport.MessageType.valueOf(messageType_);
        return result == null ? com.npr.Message.Lamport.MessageType.UNRECOGNIZED : result;
      }
      /**
       * <code>.com.npr.Lamport.MessageType message_type = 1;</code>
       * @param value The messageType to set.
       * @return This builder for chaining.
       */
      public Builder setMessageType(com.npr.Message.Lamport.MessageType value) {
        if (value == null) {
          throw new NullPointerException();
        }
        
        messageType_ = value.getNumber();
        onChanged();
        return this;
      }
      /**
       * <code>.com.npr.Lamport.MessageType message_type = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearMessageType() {
        
        messageType_ = 0;
        onChanged();
        return this;
      }

      private long clock_ ;
      /**
       * <code>int64 clock = 2;</code>
       * @return The clock.
       */
      @java.lang.Override
      public long getClock() {
        return clock_;
      }
      /**
       * <code>int64 clock = 2;</code>
       * @param value The clock to set.
       * @return This builder for chaining.
       */
      public Builder setClock(long value) {
        
        clock_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 clock = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearClock() {
        
        clock_ = 0L;
        onChanged();
        return this;
      }

      private int idSender_ ;
      /**
       * <code>int32 id_sender = 3;</code>
       * @return The idSender.
       */
      @java.lang.Override
      public int getIdSender() {
        return idSender_;
      }
      /**
       * <code>int32 id_sender = 3;</code>
       * @param value The idSender to set.
       * @return This builder for chaining.
       */
      public Builder setIdSender(int value) {
        
        idSender_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 id_sender = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearIdSender() {
        
        idSender_ = 0;
        onChanged();
        return this;
      }

      private int idReceiver_ ;
      /**
       * <code>int32 id_receiver = 4;</code>
       * @return The idReceiver.
       */
      @java.lang.Override
      public int getIdReceiver() {
        return idReceiver_;
      }
      /**
       * <code>int32 id_receiver = 4;</code>
       * @param value The idReceiver to set.
       * @return This builder for chaining.
       */
      public Builder setIdReceiver(int value) {
        
        idReceiver_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 id_receiver = 4;</code>
       * @return This builder for chaining.
       */
      public Builder clearIdReceiver() {
        
        idReceiver_ = 0;
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


      // @@protoc_insertion_point(builder_scope:com.npr.Lamport)
    }

    // @@protoc_insertion_point(class_scope:com.npr.Lamport)
    private static final com.npr.Message.Lamport DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.npr.Message.Lamport();
    }

    public static com.npr.Message.Lamport getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Lamport>
        PARSER = new com.google.protobuf.AbstractParser<Lamport>() {
      @java.lang.Override
      public Lamport parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Lamport(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Lamport> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Lamport> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public com.npr.Message.Lamport getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_npr_Lamport_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_npr_Lamport_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rMessage.proto\022\007com.npr\"\222\002\n\007Lamport\0222\n\014" +
      "message_type\030\001 \001(\0162\034.com.npr.Lamport.Mes" +
      "sageType\022\r\n\005clock\030\002 \001(\003\022\021\n\tid_sender\030\003 \001" +
      "(\005\022\023\n\013id_receiver\030\004 \001(\005\"\233\001\n\013MessageType\022" +
      "\023\n\017REQUEST_INITIAL\020\000\022\021\n\rREPLY_INITIAL\020\001\022" +
      "\023\n\017RELEASE_INITIAL\020\002\022\013\n\007REQUEST\020\003\022\t\n\005REP" +
      "LY\020\004\022\013\n\007RELEASE\020\005\022\t\n\005SLEEP\020\006\022\010\n\004WAKE\020\007\022\025" +
      "\n\021END_COMMUNICATION\020\010b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_com_npr_Lamport_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_npr_Lamport_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_npr_Lamport_descriptor,
        new java.lang.String[] { "MessageType", "Clock", "IdSender", "IdReceiver", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
