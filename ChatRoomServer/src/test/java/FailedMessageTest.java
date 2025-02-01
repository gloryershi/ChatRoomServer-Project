import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.*;

public class FailedMessageTest {

  @Test
  public void testConstructorAndGetter() {
    FailedMessage fm = new FailedMessage("error reason");
    Assertions.assertEquals("error reason", fm.getReason());
    Assertions.assertEquals(ChatRoomConstants.FAILED_MESSAGE, fm.getMessageType());
  }

  @Test
  public void testWriteAndReadMessage() throws IOException {
    FailedMessage original = new FailedMessage("some failure");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bos);
    original.writeMessage(out);
    out.flush();

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    DataInputStream in = new DataInputStream(bis);
    int type = in.readInt();
    Assertions.assertEquals(ChatRoomConstants.FAILED_MESSAGE, type);

    FailedMessage readMsg = FailedMessage.readFailedMessage(in);
    Assertions.assertEquals("some failure", readMsg.getReason());
  }

  @Test
  public void testEqualsHashCodeToString() {
    FailedMessage m1 = new FailedMessage("r1");
    FailedMessage m2 = new FailedMessage("r1");
    FailedMessage m3 = new FailedMessage("r2");

    Assertions.assertEquals(m1, m2);
    Assertions.assertNotEquals(m1, m3);
    Assertions.assertNotEquals(m1, null);
    Assertions.assertNotEquals(m1, new Object());

    Assertions.assertEquals(m1.hashCode(), m2.hashCode());

    String str = m1.toString();
    Assertions.assertTrue(str.contains("reason='r1'"));
  }
}
