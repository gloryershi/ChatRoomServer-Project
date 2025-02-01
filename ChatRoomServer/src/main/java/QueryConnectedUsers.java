import java.io.*;
import java.util.Objects;

/**
 * The {@code QueryConnectedUsers} class represents a message sent by a client to request the list
 * of currently connected users in the chat system.
 *
 * <p>This class extends {@link BaseMessage} and includes a username field to
 * identify the client making the request. It provides methods for serialization and deserialization
 * to facilitate communication over a network.</p>
 */
public class QueryConnectedUsers extends BaseMessage {

  private final String username;

  /**
   * Constructs a new {@code QueryConnectedUsers} message with the specified username.
   *
   * @param username the username of the client making the query
   */
  public QueryConnectedUsers(String username) {
    super(ChatRoomConstants.QUERY_CONNECTED_USERS);
    this.username = username;
  }

  /**
   * Returns the username of the client making the query.
   *
   * @return the username of the client
   */
  public String getUsername() {
    return username;
  }

  /**
   * Serializes this {@code QueryConnectedUsers} message into the provided
   * {@link DataOutputStream}.
   *
   * <p>The message includes the base message type and the username.</p>
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
   * Deserializes a {@code QueryConnectedUsers} message from the provided {@link DataInputStream}.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return a new {@code QueryConnectedUsers} instance with the deserialized username
   * @throws IOException if an I/O error occurs while reading the message
   */
  public static QueryConnectedUsers readQueryConnectedUsers(DataInputStream in) throws IOException {
    String username = IOUtils.readBytesAsString(in);
    return new QueryConnectedUsers(username);
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
    QueryConnectedUsers that = (QueryConnectedUsers) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), username);
  }

  @Override
  public String toString() {
    return "QueryConnectedUsers{" +
        "username='" + username + '\'' +
        '}';
  }
}
