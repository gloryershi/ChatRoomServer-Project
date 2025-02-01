import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.*;

public class ConnectMessageTest {

  @Test
  public void testConstructorAndGetUsername() {
    ConnectMessage msg = new ConnectMessage("alice");
    Assertions.assertEquals("alice", msg.getUsername());
    Assertions.assertEquals(ChatRoomConstants.CONNECT_MESSAGE, msg.getMessageType());
  }

  @Test
  public void testWriteAndReadMessage() throws IOException {
    ConnectMessage original = new ConnectMessage("bob");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bos);
    original.writeMessage(out);
    out.flush();

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    DataInputStream in = new DataInputStream(bis);
    int type = in.readInt();
    Assertions.assertEquals(ChatRoomConstants.CONNECT_MESSAGE, type);

    ConnectMessage readMsg = ConnectMessage.readConnectMessage(in);
    Assertions.assertEquals("bob", readMsg.getUsername());
  }

  @Test
  public void testEqualsHashCodeToString() {
    ConnectMessage m1 = new ConnectMessage("user");
    ConnectMessage m2 = new ConnectMessage("user");
    ConnectMessage m3 = new ConnectMessage("other");

    Assertions.assertEquals(m1, m1);
    Assertions.assertEquals(m1, m2);
    Assertions.assertNotEquals(m1, m3);
    Assertions.assertNotEquals(m1, null);
    Assertions.assertNotEquals(m1, new Object());

    Assertions.assertEquals(m1.hashCode(), m2.hashCode());
    Assertions.assertNotEquals(m1.hashCode(), m3.hashCode());

    String str = m1.toString();
    Assertions.assertTrue(str.contains("username='user'"));
  }
}
