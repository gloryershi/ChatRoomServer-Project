import java.io.*;
import java.util.Objects;

/**
 * The {@code SendInsult} class represents a message sent by a client to request that the server
 * deliver an insult to a specified recipient.
 *
 * <p>This class extends {@link BaseMessage} and includes fields for the sender
 * and the recipient of the insult. It provides methods for serialization and deserialization to
 * facilitate communication over a network.</p>
 */
public class SendInsult extends BaseMessage {

  private final String sender;
  private final String recipient;

  /**
   * Constructs a new {@code SendInsult} message with the specified sender and recipient.
   *
   * @param sender    the username of the sender
   * @param recipient the username of the recipient
   */
  public SendInsult(String sender, String recipient) {
    super(ChatRoomConstants.SEND_INSULT);
    this.sender = sender;
    this.recipient = recipient;
  }

  /**
   * Returns the username of the sender of the insult.
   *
   * @return the sender's username
   */
  public String getSender() {
    return sender;
  }

  /**
   * Returns the username of the recipient of the insult.
   *
   * @return the recipient's username
   */
  public String getRecipient() {
    return recipient;
  }

  /**
   * Serializes this {@code SendInsult} message into the provided {@link DataOutputStream}. The
   * message includes the base message type, the sender's username, and the recipient's username.
   *
   * @param out the {@link DataOutputStream} to write the message to
   * @throws IOException if an I/O error occurs while writing the message
   */
  @Override
  public void writeMessage(DataOutputStream out) throws IOException {
    super.writeMessage(out);
    IOUtils.writeStringAsBytes(out, sender);
    IOUtils.writeStringAsBytes(out, recipient);
  }

  /**
   * Deserializes a {@code SendInsult} message from the provided {@link DataInputStream}.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return a new {@code SendInsult} instance with the deserialized sender and recipient
   * @throws IOException if an I/O error occurs while reading the message
   */
  public static SendInsult readSendInsult(DataInputStream in) throws IOException {
    String sender = IOUtils.readBytesAsString(in);
    String recipient = IOUtils.readBytesAsString(in);
    return new SendInsult(sender, recipient);
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
    SendInsult that = (SendInsult) o;
    return Objects.equals(sender, that.sender) && Objects.equals(recipient,
        that.recipient);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), sender, recipient);
  }
}
