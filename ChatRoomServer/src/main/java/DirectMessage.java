import java.io.*;
import java.util.Objects;

/**
 * The {@code DirectMessage} class represents a message sent directly from one client to another in
 * the chat system. This message contains the sender's username, the recipient's username, and the
 * content of the message.
 *
 * <p>This class extends {@link BaseMessage} and provides serialization and deserialization
 * methods to facilitate transmission over a network.</p>
 */
public class DirectMessage extends BaseMessage {

  private final String sender;
  private final String recipient;
  private final String content;

  /**
   * Constructs a new {@code DirectMessage} with the specified sender, recipient, and content.
   *
   * @param sender    the username of the sender
   * @param recipient the username of the recipient
   * @param content   the content of the message
   */
  public DirectMessage(String sender, String recipient, String content) {
    super(ChatRoomConstants.DIRECT_MESSAGE);
    this.sender = sender;
    this.recipient = recipient;
    this.content = content;
  }

  /**
   * Returns the username of the sender.
   *
   * @return the sender's username
   */
  public String getSender() {
    return sender;
  }

  /**
   * Returns the username of the recipient.
   *
   * @return the recipient's username
   */
  public String getRecipient() {
    return recipient;
  }

  /**
   * Returns the content of the message.
   *
   * @return the message content
   */
  public String getContent() {
    return content;
  }

  /**
   * Serializes this {@code DirectMessage} into the provided {@link DataOutputStream}.
   *
   * @param out the {@link DataOutputStream} to write the message to
   * @throws IOException if an I/O error occurs while writing the message
   */
  @Override
  public void writeMessage(DataOutputStream out) throws IOException {
    super.writeMessage(out);
    IOUtils.writeStringAsBytes(out, sender);
    IOUtils.writeStringAsBytes(out, recipient);
    IOUtils.writeStringAsBytes(out, content);
  }

  /**
   * Deserializes a {@code DirectMessage} from the provided {@link DataInputStream}.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return a new {@code DirectMessage} with the deserialized fields
   * @throws IOException if an I/O error occurs while reading the message
   */
  public static DirectMessage readDirectMessage(DataInputStream in) throws IOException {
    String sender = IOUtils.readBytesAsString(in);
    String recipient = IOUtils.readBytesAsString(in);
    String content = IOUtils.readBytesAsString(in);
    return new DirectMessage(sender, recipient, content);
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
    DirectMessage that = (DirectMessage) o;
    return Objects.equals(sender, that.sender) && Objects.equals(recipient,
        that.recipient) && Objects.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), sender, recipient, content);
  }

  @Override
  public String toString() {
    return "DirectMessage{" +
        "sender='" + sender + '\'' +
        ", recipient='" + recipient + '\'' +
        ", content='" + content + '\'' +
        '}';
  }
}
