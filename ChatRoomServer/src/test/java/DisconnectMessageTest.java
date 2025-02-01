import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.*;

public class DisconnectMessageTest {

  @Test
  public void testConstructorAndGetter() {
    DisconnectMessage dm = new DisconnectMessage("alice");
    Assertions.assertEquals("alice", dm.getUsername());
    Assertions.assertEquals(ChatRoomConstants.DISCONNECT_MESSAGE, dm.getMessageType());
  }

  @Test
  public void testWriteAndReadMessage() throws IOException {
    DisconnectMessage original = new DisconnectMessage("bob");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bos);
    original.writeMessage(out);
    out.flush();

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    DataInputStream in = new DataInputStream(bis);
    int type = in.readInt();
    Assertions.assertEquals(ChatRoomConstants.DISCONNECT_MESSAGE, type);

    DisconnectMessage readMsg = DisconnectMessage.readDisconnectMessage(in);
    Assertions.assertEquals("bob", readMsg.getUsername());
  }

  @Test
  public void testEqualsHashCodeToString() {
    DisconnectMessage m1 = new DisconnectMessage("u");
    DisconnectMessage m2 = new DisconnectMessage("u");
    DisconnectMessage m3 = new DisconnectMessage("x");

    Assertions.assertEquals(m1, m2);
    Assertions.assertNotEquals(m1, m3);
    Assertions.assertNotEquals(m1, null);
    Assertions.assertNotEquals(m1, new Object());

    Assertions.assertEquals(m1.hashCode(), m2.hashCode());

    String str = m1.toString();
    Assertions.assertTrue(str.contains("username='u'"));
  }
}
