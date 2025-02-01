import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class QueryConnectedUsersTest {

  @Test
  public void testConstructorAndGetter() {
    QueryConnectedUsers msg = new QueryConnectedUsers("alice");
    Assertions.assertEquals("alice", msg.getUsername());
    Assertions.assertEquals(ChatRoomConstants.QUERY_CONNECTED_USERS, msg.getMessageType());
  }

  @Test
  public void testWriteAndReadMessage() throws IOException {
    QueryConnectedUsers original = new QueryConnectedUsers("bob");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bos);
    original.writeMessage(out);
    out.flush();

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    DataInputStream in = new DataInputStream(bis);

    int type = in.readInt();
    Assertions.assertEquals(ChatRoomConstants.QUERY_CONNECTED_USERS, type);

    QueryConnectedUsers readMsg = QueryConnectedUsers.readQueryConnectedUsers(in);
    Assertions.assertEquals("bob", readMsg.getUsername());
  }

  @Test
  public void testEqualsHashCodeToString() {
    QueryConnectedUsers m1 = new QueryConnectedUsers("user");
    QueryConnectedUsers m2 = new QueryConnectedUsers("user");
    QueryConnectedUsers m3 = new QueryConnectedUsers("other");

    Assertions.assertEquals(m1, m2);
    Assertions.assertNotEquals(m1, m3);
    Assertions.assertNotEquals(m1, null);
    Assertions.assertNotEquals(m1, new Object());
    Assertions.assertEquals(m1.hashCode(), m2.hashCode());

    String str = m1.toString();
    Assertions.assertTrue(str.contains("username='user'"));
  }
}
