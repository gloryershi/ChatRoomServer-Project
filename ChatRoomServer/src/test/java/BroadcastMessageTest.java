import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.*;

public class BroadcastMessageTest {

  @Test
  public void testConstructorAndGetters() {
    BroadcastMessage msg = new BroadcastMessage("alice", "hello world");
    Assertions.assertEquals("alice", msg.getSender());
    Assertions.assertEquals("hello world", msg.getContent());
    Assertions.assertEquals(ChatRoomConstants.BROADCAST_MESSAGE, msg.getMessageType());
  }

  @Test
  public void testWriteAndReadMessage() throws IOException {
    BroadcastMessage original = new BroadcastMessage("bob", "broadcast test");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bos);
    original.writeMessage(out);
    out.flush();

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    DataInputStream in = new DataInputStream(bis);
    int type = in.readInt();
    Assertions.assertEquals(ChatRoomConstants.BROADCAST_MESSAGE, type);

    BroadcastMessage readMsg = BroadcastMessage.readBroadcastMessage(in);
    Assertions.assertEquals("bob", readMsg.getSender());
    Assertions.assertEquals("broadcast test", readMsg.getContent());
  }

  @Test
  public void testEqualsHashCodeToString() {
    BroadcastMessage m1 = new BroadcastMessage("charlie", "hi");
    BroadcastMessage m2 = new BroadcastMessage("charlie", "hi");
    BroadcastMessage m3 = new BroadcastMessage("charlie", "different");
    BroadcastMessage m4 = new BroadcastMessage("dave", "hi");

    Assertions.assertEquals(m1, m1);
    Assertions.assertEquals(m1, m2);
    Assertions.assertNotEquals(m1, m3);
    Assertions.assertNotEquals(m1, m4);
    Assertions.assertNotEquals(m1, null);
    Assertions.assertNotEquals(m1, new Object());

    Assertions.assertEquals(m1.hashCode(), m2.hashCode());
    Assertions.assertNotEquals(m1.hashCode(), m3.hashCode());

    String str = m1.toString();
    Assertions.assertTrue(str.contains("sender='charlie'"));
    Assertions.assertTrue(str.contains("content='hi'"));
  }
}
