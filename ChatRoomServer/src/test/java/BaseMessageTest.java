import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class BaseMessageTest {

  @Test
  public void testWriteAndReadMessage() throws IOException {

    BaseMessage m = new BaseMessage(ChatRoomConstants.CONNECT_MESSAGE){};

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);
    m.writeMessage(dout);
    dout.flush();

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    DataInputStream din = new DataInputStream(bis);

    int type = din.readInt();
    Assertions.assertEquals(ChatRoomConstants.CONNECT_MESSAGE, type);
  }

  @Test
  public void testReadUnknownMessageType() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bos);
    out.writeInt(-999); //未知类型

    DataInputStream in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    Assertions.assertThrows(IOException.class, () -> BaseMessage.readMessage(in));
  }

  @Test
  public void testEqualsHashCodeToString() {
    BaseMessage m1 = new BaseMessage(ChatRoomConstants.CONNECT_MESSAGE) {};
    BaseMessage m2 = new BaseMessage(ChatRoomConstants.CONNECT_MESSAGE) {};
    BaseMessage m3 = new BaseMessage(ChatRoomConstants.BROADCAST_MESSAGE) {};

    Assertions.assertEquals(m1, m1);
    Assertions.assertNotEquals(m1, m3);
    Assertions.assertNotEquals(m1, null);
    Assertions.assertTrue(m1.toString().contains("messageType="));
    Assertions.assertEquals(m1.hashCode(), m2.hashCode());
  }
}
