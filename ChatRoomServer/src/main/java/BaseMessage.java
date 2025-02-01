import java.io.*;
import java.util.Objects;

/**
 * The {@code BaseMessage} class serves as an abstract base for all message types used in the chat
 * room system. It encapsulates a message type identifier and provides serialization and
 * deserialization capabilities for message objects. Subclasses of {@code BaseMessage} should define
 * their specific message type constants in {@code ChatRoomConstants} and implement the necessary
 * logic for writing and reading message-specific data. {@code BaseMessage} is responsible for:
 * Defining the common interface and functionality for all message types Providing a type-safe
 * mechanism for reading message objects from a data stream Overriding common utility methods such
 * as {@code equals}, {@code hashCode}, and {@code toString}
 */
public abstract class BaseMessage {

  private final int messageType;

  /**
   * Constructs a {@code BaseMessage} with the specified message type.
   *
   * @param messageType the type identifier for this message
   */
  public BaseMessage(int messageType) {
    this.messageType = messageType;
  }

  /**
   * Retrieves the message type of this {@code BaseMessage}.
   *
   * @return the message type as an integer
   */
  public int getMessageType() {
    return messageType;
  }

  /**
   * Writes the message type to the provided {@link DataOutputStream}. Subclasses should override
   * this method to serialize their specific data.
   *
   * @param out the {@link DataOutputStream} to write the message to
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public void writeMessage(DataOutputStream out) throws IOException {
    out.writeInt(messageType);
  }

  /**
   * Reads a {@code BaseMessage} object from the provided {@link DataInputStream}. The message type
   * is read first, and the method delegates the construction of the specific message object to the
   * corresponding subclass based on the message type.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return the constructed {@code BaseMessage} object
   * @throws IOException if an I/O error occurs or if the message type is unknown
   */
  public static BaseMessage readMessage(DataInputStream in) throws IOException {
    int messageType = in.readInt();
    switch (messageType) {
      case ChatRoomConstants.CONNECT_MESSAGE:
        return ConnectMessage.readConnectMessage(in);
      case ChatRoomConstants.CONNECT_RESPONSE:
        return ConnectResponse.readConnectResponse(in);
      case ChatRoomConstants.DISCONNECT_MESSAGE:
        return DisconnectMessage.readDisconnectMessage(in);
      case ChatRoomConstants.QUERY_CONNECTED_USERS:
        return QueryConnectedUsers.readQueryConnectedUsers(in);
      case ChatRoomConstants.QUERY_USER_RESPONSE:
        return QueryUserResponse.readQueryUserResponse(in);
      case ChatRoomConstants.BROADCAST_MESSAGE:
        return BroadcastMessage.readBroadcastMessage(in);
      case ChatRoomConstants.DIRECT_MESSAGE:
        return DirectMessage.readDirectMessage(in);
      case ChatRoomConstants.FAILED_MESSAGE:
        return FailedMessage.readFailedMessage(in);
      case ChatRoomConstants.SEND_INSULT:
        return SendInsult.readSendInsult(in);
      default:
        throw new IOException("Unknown message type: " + messageType);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseMessage that = (BaseMessage) o;
    return messageType == that.messageType;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(messageType);
  }

  @Override
  public String toString() {
    return "BaseMessage{" +
        "messageType=" + messageType +
        '}';
  }
}
