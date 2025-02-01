import java.io.*;
import java.util.Objects;

/**
 * Represents a broadcast message in the chat room system. A broadcast message is sent by one user
 * to all other connected users. This class extends {@link BaseMessage} and adds additional fields
 * to represent the sender and the content of the broadcast message. Instances of
 * {@code BroadcastMessage} can be serialized to and deserialized from a {@link DataOutputStream}
 * and {@link DataInputStream}, respectively, allowing them to be transmitted over a network.
 *
 * @see BaseMessage
 */
public class BroadcastMessage extends BaseMessage {

  private final String sender;
  private final String content;

  /**
   * Constructs a new {@code BroadcastMessage}.
   *
   * @param sender  the username of the sender
   * @param content the content of the message
   */
  public BroadcastMessage(String sender, String content) {
    super(ChatRoomConstants.BROADCAST_MESSAGE);
    this.sender = sender;
    this.content = content;
  }

  /**
   * Returns the username of the sender of this broadcast message.
   *
   * @return the sender's username
   */
  public String getSender() {
    return sender;
  }

  /**
   * Returns the content of this broadcast message.
   *
   * @return the message content
   */
  public String getContent() {
    return content;
  }

  /**
   * Writes this {@code BroadcastMessage} to the provided {@link DataOutputStream}. The method
   * writes the message type, sender, and content in sequence.
   *
   * @param out the {@link DataOutputStream} to write the message to
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  @Override
  public void writeMessage(DataOutputStream out) throws IOException {
    super.writeMessage(out);
    IOUtils.writeStringAsBytes(out, sender);
    IOUtils.writeStringAsBytes(out, content);
  }

  /**
   * Reads a {@code BroadcastMessage} from the provided {@link DataInputStream}. The method reads
   * the sender and content fields in sequence and constructs a {@code BroadcastMessage} object.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return the constructed {@code BroadcastMessage}
   * @throws IOException if an I/O error occurs while reading from the stream
   */
  public static BroadcastMessage readBroadcastMessage(DataInputStream in) throws IOException {
    String sender = IOUtils.readBytesAsString(in);
    String message = IOUtils.readBytesAsString(in);
    return new BroadcastMessage(sender, message);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    BroadcastMessage that = (BroadcastMessage) o;
    return Objects.equals(sender, that.sender) && Objects.equals(content,
        that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), sender, content);
  }

  @Override
  public String toString() {
    return "BroadcastMessage{" +
        "sender='" + sender + '\'' +
        ", content='" + content + '\'' +
        '}';
  }
}
