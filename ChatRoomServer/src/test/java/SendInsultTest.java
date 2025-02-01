import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class SendInsultTest {

  @Test
  public void testConstructorAndGetters() {
    SendInsult msg = new SendInsult("sender", "recipient");
    Assertions.assertEquals("sender", msg.getSender());
    Assertions.assertEquals("recipient", msg.getRecipient());
    Assertions.assertEquals(ChatRoomConstants.SEND_INSULT, msg.getMessageType());
  }

  @Test
  public void testWriteAndReadMessage() throws IOException {
    SendInsult original = new SendInsult("alice", "bob");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bos);
    original.writeMessage(out);
    out.flush();

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    DataInputStream in = new DataInputStream(bis);

    int type = in.readInt();
    Assertions.assertEquals(ChatRoomConstants.SEND_INSULT, type);

    SendInsult readMsg = SendInsult.readSendInsult(in);
    Assertions.assertEquals("alice", readMsg.getSender());
    Assertions.assertEquals("bob", readMsg.getRecipient());
  }

  @Test
  public void testEqualsHashCodeToString() {
    SendInsult m1 = new SendInsult("s", "r");
    SendInsult m2 = new SendInsult("s", "r");
    SendInsult m3 = new SendInsult("s", "x");

    Assertions.assertEquals(m1, m2);
    Assertions.assertNotEquals(m1, m3);
    Assertions.assertNotEquals(m1, null);
    Assertions.assertNotEquals(m1, new Object());

    Assertions.assertEquals(m1.hashCode(), m2.hashCode());

    String str = m1.toString();
  }
}
