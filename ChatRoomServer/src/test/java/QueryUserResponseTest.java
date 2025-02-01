import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

public class QueryUserResponseTest {

  @Test
  public void testConstructorAndGetter() {
    List<String> users = Arrays.asList("alice", "bob");
    QueryUserResponse msg = new QueryUserResponse(users);
    Assertions.assertEquals(users, msg.getUsers());
    Assertions.assertEquals(ChatRoomConstants.QUERY_USER_RESPONSE, msg.getMessageType());
  }

  @Test
  public void testWriteAndReadMessage() throws IOException {
    List<String> users = Arrays.asList("user1", "user2", "user3");
    QueryUserResponse original = new QueryUserResponse(users);

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bos);
    original.writeMessage(out);
    out.flush();

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    DataInputStream in = new DataInputStream(bis);

    int type = in.readInt();
    Assertions.assertEquals(ChatRoomConstants.QUERY_USER_RESPONSE, type);

    QueryUserResponse readMsg = QueryUserResponse.readQueryUserResponse(in);
    Assertions.assertEquals(users, readMsg.getUsers());
  }

  @Test
  public void testEqualsHashCodeToString() {
    List<String> list1 = Arrays.asList("a", "b");
    List<String> list2 = Arrays.asList("a", "b");
    List<String> list3 = Arrays.asList("x");

    QueryUserResponse m1 = new QueryUserResponse(list1);
    QueryUserResponse m2 = new QueryUserResponse(list2);
    QueryUserResponse m3 = new QueryUserResponse(list3);

    Assertions.assertEquals(m1, m2);
    Assertions.assertNotEquals(m1, m3);
    Assertions.assertNotEquals(m1, null);
    Assertions.assertNotEquals(m1, new Object());
    Assertions.assertEquals(m1.hashCode(), m2.hashCode());

    String str = m1.toString();
    Assertions.assertTrue(str.contains("users=[a, b]"));
  }
}
