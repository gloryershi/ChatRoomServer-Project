import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandlerTest {

  private ClientHandler createClientHandler(String username, boolean connected) throws IOException {

    Socket s = new Socket();


    ChatRoomServer server = new ChatRoomServer(0);

    ClientHandler ch = new ClientHandler(s, server);

    try {
      var usernameField = ClientHandler.class.getDeclaredField("username");
      usernameField.setAccessible(true);
      usernameField.set(ch, username);

      var connectedField = ClientHandler.class.getDeclaredField("connected");
      connectedField.setAccessible(true);
      connectedField.setBoolean(ch, connected);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return ch;
  }


  @Test
  public void testEquals() throws IOException {
    ClientHandler c1 = createClientHandler("alice", true);
    ClientHandler c2 = createClientHandler("alice", true);
    ClientHandler c3 = createClientHandler("alice", true);
    ClientHandler c4 = createClientHandler("bob", true);
    ClientHandler c5 = createClientHandler("alice", false);

    Assertions.assertEquals(c1, c1);

    Assertions.assertNotEquals(c1, c4);

    Assertions.assertNotEquals(c1, c5);

    Assertions.assertNotEquals(c1, null);

    Assertions.assertNotEquals(c1, "some string");
  }

  @Test
  public void testHashCode() throws IOException {
    ClientHandler c1 = createClientHandler("alice", true);
    ClientHandler c3 = createClientHandler("bob", true);
    ClientHandler c4 = createClientHandler("alice", false);

    Assertions.assertNotEquals(c1.hashCode(), c3.hashCode());

    Assertions.assertNotEquals(c1.hashCode(), c4.hashCode());
  }

  @Test
  public void testToString() throws IOException {
    ClientHandler c = createClientHandler("alice", true);
    String str = c.toString();
    Assertions.assertNotNull(str);
    Assertions.assertTrue(str.contains("ClientHandler"));
    Assertions.assertTrue(str.contains("username='alice'"));
    Assertions.assertTrue(str.contains("connected=true"));

    ClientHandler c2 = createClientHandler("bob", false);
    String str2 = c2.toString();
    Assertions.assertTrue(str2.contains("username='bob'"));
    Assertions.assertTrue(str2.contains("connected=false"));
  }
}
