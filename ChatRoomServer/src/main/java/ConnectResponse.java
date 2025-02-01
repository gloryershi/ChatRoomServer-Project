import java.io.*;
import java.util.Objects;

/**
 * The {@code ConnectResponse} class represents a response from the server to a client's connection
 * request. This response indicates whether the connection was successful and provides an associated
 * message.
 *
 * <p>This class extends {@link BaseMessage} and adds fields for the success status
 * and a descriptive message. It provides methods for serialization and deserialization to
 * facilitate communication over a network.</p>
 */
public class ConnectResponse extends BaseMessage {

  private final boolean success;
  private final String message;

  /**
   * Constructs a new {@code ConnectResponse} with the specified success status and message.
   *
   * @param success {@code true} if the connection was successful; {@code false} otherwise
   * @param message a message describing the connection result
   */
  public ConnectResponse(boolean success, String message) {
    super(ChatRoomConstants.CONNECT_RESPONSE);
    this.success = success;
    this.message = message;
  }

  /**
   * Returns the message associated with the connection response.
   *
   * @return the connection response message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Serializes this {@code ConnectResponse} into the provided {@link DataOutputStream}.
   *
   * @param out the {@link DataOutputStream} to write the message to
   * @throws IOException if an I/O error occurs while writing the message
   */
  @Override
  public void writeMessage(DataOutputStream out) throws IOException {
    super.writeMessage(out);
    out.writeBoolean(success);
    IOUtils.writeStringAsBytes(out, message);
  }

  /**
   * Deserializes a {@code ConnectResponse} from the provided {@link DataInputStream}.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return a new {@code ConnectResponse} with the deserialized fields
   * @throws IOException if an I/O error occurs while reading the message
   */
  public static ConnectResponse readConnectResponse(DataInputStream in) throws IOException {
    boolean success = in.readBoolean();
    String msg = IOUtils.readBytesAsString(in);
    return new ConnectResponse(success, msg);
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
    ConnectResponse that = (ConnectResponse) o;
    return success == that.success && Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), success, message);
  }

  @Override
  public String toString() {
    return "ConnectResponse{" +
        "success=" + success +
        ", message='" + message + '\'' +
        '}';
  }
}
