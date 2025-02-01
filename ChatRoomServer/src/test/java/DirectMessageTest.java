import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.*;

public class DirectMessageTest {

  @Test
  public void testConstructorGetters() {
    DirectMessage dm = new DirectMessage("alice", "bob", "secret");
    Assertions.assertEquals("alice", dm.getSender());
    Assertions.assertEquals("bob", dm.getRecipient());
    Assertions.assertEquals("secret", dm.getContent());
    Assertions.assertEquals(ChatRoomConstants.DIRECT_MESSAGE, dm.getMessageType());
  }

  @Test
  public void testWriteAndReadMessage() throws IOException {
    DirectMessage original = new DirectMessage("sender", "receiver", "hello");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bos);
    original.writeMessage(out);
    out.flush();

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    DataInputStream in = new DataInputStream(bis);
    int type = in.readInt();
    Assertions.assertEquals(ChatRoomConstants.DIRECT_MESSAGE, type);

    DirectMessage readMsg = DirectMessage.readDirectMessage(in);
    Assertions.assertEquals("sender", readMsg.getSender());
    Assertions.assertEquals("receiver", readMsg.getRecipient());
    Assertions.assertEquals("hello", readMsg.getContent());
  }

  @Test
  public void testEqualsHashCodeToString() {
    DirectMessage m1 = new DirectMessage("a", "b", "msg");
    DirectMessage m2 = new DirectMessage("a", "b", "msg");
    DirectMessage m3 = new DirectMessage("a", "b", "diff");
    DirectMessage m4 = new DirectMessage("a", "c", "msg");

    Assertions.assertEquals(m1, m2);
    Assertions.assertNotEquals(m1, m3);
    Assertions.assertNotEquals(m1, m4);
    Assertions.assertNotEquals(m1, null);
    Assertions.assertNotEquals(m1, new Object());

    Assertions.assertEquals(m1.hashCode(), m2.hashCode());

    String str = m1.toString();
    Assertions.assertTrue(str.contains("sender='a'"));
    Assertions.assertTrue(str.contains("recipient='b'"));
    Assertions.assertTrue(str.contains("content='msg'"));
  }
}
