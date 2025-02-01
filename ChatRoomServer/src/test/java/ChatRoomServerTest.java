import org.junit.jupiter.api.*;

import java.io.*;

public class ChatRoomServerTest {

  static ChatRoomServer server;
  static Thread serverThread;
  static int serverPort=1234;

  @BeforeAll
  public static void setUpServer() throws Exception {

    server = new ChatRoomServer(serverPort);
    serverThread = new Thread(server::startServer);
    serverThread.start();
    Thread.sleep(500);
  }

  @AfterAll
  public static void tearDownServer() throws Exception {
    server.serverSocket.close();
    serverThread.interrupt();
  }

  @Test
  public void testConnectionAndBroadcast() throws Exception {
    ChatRoomClient client1 = new ChatRoomClient();
    ChatRoomClient client2 = new ChatRoomClient();

    ByteArrayOutputStream client1Out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(client1Out));

    Thread t1 = new Thread(() -> {
      client1.connect("localhost", serverPort, "user1");
    });
    t1.start();

    Thread.sleep(500);

    ByteArrayOutputStream client2Out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(client2Out));
    Thread t2 = new Thread(() -> {
      client2.connect("localhost", serverPort, "user2");
    });
    t2.start();

    Thread.sleep(500);
    String inputCommands = "@all Hello everyone\nlogoff\n";
    System.setIn(new ByteArrayInputStream(inputCommands.getBytes()));
    System.setOut(new PrintStream(client1Out));

    Thread.sleep(2000);
    String c2Output = client2Out.toString();
    ByteArrayOutputStream client2Out2 = new ByteArrayOutputStream();
    System.setOut(new PrintStream(client2Out2));
    System.setIn(new ByteArrayInputStream("who\nlogoff\n".getBytes()));
    ChatRoomClient client3 = new ChatRoomClient();
    client3.connect("localhost", serverPort, "user3");
    Thread.sleep(2000);
    String c3Out = client2Out2.toString();
  }


}
