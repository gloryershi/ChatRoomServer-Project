import java.io.*;
import java.util.Objects;

/**
 * The {@code FailedMessage} class represents a message sent by the server to notify a client of an
 * error or failure. This message contains a reason describing why the failure occurred.
 *
 * <p>This class extends {@link BaseMessage} and provides serialization and deserialization
 * methods for transmitting the message over a network.</p>
 */
public class FailedMessage extends BaseMessage {

  private final String reason;

  /**
   * Constructs a new {@code FailedMessage} with the specified reason.
   *
   * @param reason a description of the failure
   */
  public FailedMessage(String reason) {
    super(ChatRoomConstants.FAILED_MESSAGE);
    this.reason = reason;
  }

  /**
   * Returns the reason for the failure.
   *
   * @return the failure reason
   */
  public String getReason() {
    return reason;
  }

  /**
   * Serializes this {@code FailedMessage} into the provided {@link DataOutputStream}.
   *
   * @param out the {@link DataOutputStream} to write the message to
   * @throws IOException if an I/O error occurs while writing the message
   */
  @Override
  public void writeMessage(DataOutputStream out) throws IOException {
    super.writeMessage(out);
    IOUtils.writeStringAsBytes(out, reason);
  }

  /**
   * Deserializes a {@code FailedMessage} from the provided {@link DataInputStream}.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return a new {@code FailedMessage} with the deserialized reason
   * @throws IOException if an I/O error occurs while reading the message
   */
  public static FailedMessage readFailedMessage(DataInputStream in) throws IOException {
    String reason = IOUtils.readBytesAsString(in);
    return new FailedMessage(reason);
  }

  @Override
  public String toString() {
    return "FailedMessage{" +
        "reason='" + reason + '\'' +
        '}';
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
    FailedMessage that = (FailedMessage) o;
    return Objects.equals(reason, that.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), reason);
  }
}
