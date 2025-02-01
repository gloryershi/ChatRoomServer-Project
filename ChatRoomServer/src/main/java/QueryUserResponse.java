import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * The {@code QueryUserResponse} class represents a response sent by the server to a client's
 * request for the list of currently connected users in the chat system.
 *
 * <p>This class extends {@link BaseMessage} and includes a list of usernames representing
 * the connected users. It provides methods for serialization and deserialization to facilitate
 * communication over a network.</p>
 */
public class QueryUserResponse extends BaseMessage {

  private final List<String> users;

  /**
   * Constructs a new {@code QueryUserResponse} with the specified list of users.
   *
   * @param users the list of connected users
   */
  public QueryUserResponse(List<String> users) {
    super(ChatRoomConstants.QUERY_USER_RESPONSE);
    this.users = users;
  }

  /**
   * Returns the list of usernames of the connected users.
   *
   * @return the list of connected users
   */
  public List<String> getUsers() {
    return users;
  }

  /**
   * Serializes this {@code QueryUserResponse} into the provided {@link DataOutputStream}.
   *
   * <p>The message includes the base message type, the number of users, and the usernames.</p>
   *
   * @param out the {@link DataOutputStream} to write the message to
   * @throws IOException if an I/O error occurs while writing the message
   */
  @Override
  public void writeMessage(DataOutputStream out) throws IOException {
    super.writeMessage(out);
    out.writeInt(users.size());
    for (String user : users) {
      IOUtils.writeStringAsBytes(out, user);
    }
  }

  /**
   * Deserializes a {@code QueryUserResponse} message from the provided {@link DataInputStream}.
   *
   * @param in the {@link DataInputStream} to read the message from
   * @return a new {@code QueryUserResponse} instance with the deserialized list of users
   * @throws IOException if an I/O error occurs while reading the message
   */
  public static QueryUserResponse readQueryUserResponse(DataInputStream in) throws IOException {
    int size = in.readInt();
    List<String> users = new java.util.ArrayList<>();
    for (int i = 0; i < size; i++) {
      users.add(IOUtils.readBytesAsString(in));
    }
    return new QueryUserResponse(users);
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
    QueryUserResponse that = (QueryUserResponse) o;
    return Objects.equals(users, that.users);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), users);
  }

  @Override
  public String toString() {
    return "QueryUserResponse{" +
        "users=" + users +
        '}';
  }
}
