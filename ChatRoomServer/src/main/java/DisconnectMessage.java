import java.io.*;
import java.util.Objects;

/**
 * The {@code DisconnectMessage} class represents a message sent by a client to notify the server
 * that the client wants to disconnect from the chat system.
 *
 * <p>This class extends {@link BaseMessage} and includes a username field to identify
 * the client that is disconnecting. It provides methods for serialization and deserialization to
 * facilitate communication over a network.</p>
 */
public class DisconnectMessage extends BaseMessage {

  private final String username;

  /**
   * Constructs a new {@code DisconnectMessage} with the specified username.
   *
   * @param username the username of the client
   */
  public DisconnectMessage(String username) {
    super(ChatRoomConstants.DISCONNECT_MESSAGE);
    this.username = username;
  }

  /**
   * Returns the username of the client that is disconnecting.
   *
   * @return the client's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Serializes this {@code DisconnectMessage} into the provided {@link DataOutputStream}.
   *
   * @param out the {@link DataOutputStream} to write the message to
   * @throws IOException if an I/O error occurs while writing the message
   */
  @Override
  public void writeMessage(DataOutputStream out) throws IOException {
    super.writeMessage(out);
    IOUtils.writeStringAsBytes(out, username);
  }

  /**
   * Deserializes a {@code DisconnectMessage} from the provided {@link DataInputStream}.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return a new {@code DisconnectMessage} with the deserialized username
   * @throws IOException if an I/O error occurs while reading the message
   */
  public static DisconnectMessage readDisconnectMessage(DataInputStream in) throws IOException {
    String username = IOUtils.readBytesAsString(in);
    return new DisconnectMessage(username);
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
    DisconnectMessage that = (DisconnectMessage) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), username);
  }

  @Override
  public String toString() {
    return "DisconnectMessage{" +
        "username='" + username + '\'' +
        '}';
  }
}
