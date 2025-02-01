import java.io.*;
import java.util.Objects;

/**
 * The {@code ConnectMessage} class represents a message used by a client to request a connection to
 * the chat server. This message contains the username of the client attempting to connect.
 *
 * <p>This class extends {@link BaseMessage} and adds the username field. It also
 * provides serialization and deserialization methods for transmitting the message over a
 * network.</p>
 */
public class ConnectMessage extends BaseMessage {

  private final String username;

  /**
   * Constructs a new {@code ConnectMessage} with the specified username.
   *
   * @param username the username of the client
   */
  public ConnectMessage(String username) {
    super(ChatRoomConstants.CONNECT_MESSAGE);
    this.username = username;
  }

  /**
   * Returns the username of the client.
   *
   * @return the username of the client
   */
  public String getUsername() {
    return username;
  }

  /**
   * Serializes this {@code ConnectMessage} into the provided {@link DataOutputStream}.
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
   * Deserializes a {@code ConnectMessage} from the provided {@link DataInputStream}.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return a new {@code ConnectMessage} with the deserialized username
   * @throws IOException if an I/O error occurs while reading the message
   */
  public static ConnectMessage readConnectMessage(DataInputStream in) throws IOException {
    String username = IOUtils.readBytesAsString(in);
    return new ConnectMessage(username);
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
    ConnectMessage that = (ConnectMessage) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), username);
  }

  @Override
  public String toString() {
    return "ConnectMessage{" +
        "username='" + username + '\'' +
        '}';
  }
}
