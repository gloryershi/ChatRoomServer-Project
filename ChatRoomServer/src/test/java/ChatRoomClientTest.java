import org.junit.jupiter.api.*;

import java.io.*;

public class ChatRoomClientTest {

  @Test
  public void testPrintHelpMenu() {
    ChatRoomClient client = new ChatRoomClient();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    System.setOut(new PrintStream(bos));
    client.printHelpMenu();
    String help = bos.toString();
    Assertions.assertTrue(help.contains("Available commands:"));
  }

  @Test
  public void testDisconnectWithoutConnection() {
    ChatRoomClient client = new ChatRoomClient();
    client.disconnect();
    Assertions.assertFalse(client.isConnected);
  }

  @Test
  public void testEquals() {
    ChatRoomClient c1 = new ChatRoomClient();
    ChatRoomClient c2 = new ChatRoomClient();
    ChatRoomClient c3 = new ChatRoomClient();

    Assertions.assertEquals(c1, c1);

    Assertions.assertEquals(c1, c2);
    Assertions.assertEquals(c2, c3);
    Assertions.assertEquals(c1, c3);

    Assertions.assertNotEquals(c1, null);

    Assertions.assertNotEquals(c1, "some string");

    c2.isConnected = false;
    Assertions.assertNotEquals(c1, c2);
  }

  @Test
  public void testHashCode() {
    ChatRoomClient c1 = new ChatRoomClient();
    ChatRoomClient c2 = new ChatRoomClient();
    Assertions.assertEquals(c1.hashCode(), c2.hashCode());

    c2.isConnected = false;
    Assertions.assertNotEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  public void testToString() {
    ChatRoomClient c1 = new ChatRoomClient();
    String str = c1.toString();
    Assertions.assertNotNull(str);
    Assertions.assertTrue(str.contains("ChatRoomClient"));
    Assertions.assertTrue(str.contains("isConnected=true"));

    c1.isConnected = false;
    String str2 = c1.toString();
    Assertions.assertTrue(str2.contains("isConnected=false"));
  }
}
