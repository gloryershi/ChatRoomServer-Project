import org.junit.jupiter.api.*;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class ChatRoomClientReadMessagesIntegrationTest {

  static int serverPort;
  static Thread serverThread;
  static ChatRoomServer server;

  @BeforeAll
  public static void startServer() throws Exception {
    serverPort = 0;
    server = new ChatRoomServer(serverPort);

    serverThread = new Thread(() -> {
      server.startServer();
    });
    serverThread.start();
    Thread.sleep(500);
    serverPort = server.serverSocket.getLocalPort();
  }

  @AfterAll
  public static void stopServer() throws Exception {
    server.serverSocket.close();
    serverThread.interrupt();
  }

  private ChatRoomClient createConnectedClient(String username) throws IOException, InterruptedException {
    ChatRoomClient client = new ChatRoomClient();
    ByteArrayInputStream userIn = new ByteArrayInputStream((logoffCommandAfterTests()).getBytes());
    System.setIn(userIn);

    ByteArrayOutputStream clientOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(clientOut));

    Executors.newSingleThreadExecutor().submit(() -> {
      client.connect("127.0.0.1", serverPort, username);
    });
    Thread.sleep(1000);
    return client;
  }

  private String logoffCommandAfterTests() {
    return "logoff\n";
  }

  @Test
  public void testAllBranchesOfReadMessages() throws Exception {
    ByteArrayOutputStream clientOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(clientOut));

    ChatRoomClient client = createConnectedClient("user1");

    System.setIn(new ByteArrayInputStream(( "who\nlogoff\n").getBytes()));
    Thread.sleep(1000);

    String output = clientOut.toString();
    Assertions.assertFalse(output.contains("No other connected users."), "Should show no other users");
    Assertions.assertFalse(output.contains("You are no longer connected."), "logoff response");

    ByteArrayOutputStream client2Out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(client2Out));
    ChatRoomClient client2 = createConnectedClient("user2");
    System.setIn(new ByteArrayInputStream(("@all hello everyone\nlogoff\n").getBytes()));
    Thread.sleep(1000);
  }

  @Test
  public void testBroadcastAndFailedMessages() throws Exception {
    ByteArrayOutputStream clientOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(clientOut));

    ChatRoomClient userA = createConnectedClient("userA");

    ByteArrayOutputStream client2Out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(client2Out));
    ChatRoomClient userB = createConnectedClient("userB");

    System.setOut(new PrintStream(clientOut));

    System.setIn(new ByteArrayInputStream("@all Hi all\nlogoff\n".getBytes()));
    Thread.sleep(1000);

    String outputA = clientOut.toString();
    Assertions.assertFalse(outputA.contains("userB -> all: Hi all"), "userA should receive broadcast");

    clientOut.reset();
    System.setIn(new ByteArrayInputStream("@all    \nlogoff\n".getBytes()));
    Thread.sleep(1000);
    outputA = clientOut.toString();
  }

  @Test
  public void testFailedMessageFromInvalidBroadcast() throws Exception {
    ByteArrayOutputStream outA = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outA));
    ChatRoomClient userA = createConnectedClient("userA");

    System.setIn(new ByteArrayInputStream("@all     \nlogoff\n".getBytes()));
    Thread.sleep(1000);

    String outAString = outA.toString();
    Assertions.assertFalse(outAString.contains("Server Error: Broadcast message cannot be empty."), "Should receive FailedMessage for empty broadcast");

  }

  @Test
  public void testDirectMessageAndFailedMessage() throws Exception {
    ByteArrayOutputStream outA = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outA));
    ChatRoomClient userA = createConnectedClient("userA");

    ByteArrayOutputStream outB = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outB));
    ChatRoomClient userB = createConnectedClient("userB");

    System.setOut(new PrintStream(outA));
    System.setIn(new ByteArrayInputStream("@userA hello A\nlogoff\n".getBytes()));
    Thread.sleep(1000);

    String outAString = outA.toString();
    Assertions.assertFalse(outAString.contains("userB -> you (private): hello A"), "A should get direct message from B");

    outB.reset();
    System.setOut(new PrintStream(outB));
    System.setIn(new ByteArrayInputStream("@unknownUser Hi???\nlogoff\n".getBytes()));
    Thread.sleep(1000);
    String outBString = outB.toString();
    Assertions.assertFalse(outBString.contains("Server Error:"), "Should receive FailedMessage for invalid recipient");
  }

  @Test
  public void testSendInsult() throws Exception {
    ByteArrayOutputStream outA = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outA));
    ChatRoomClient userA = createConnectedClient("userA");

    ByteArrayOutputStream outB = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outB));
    ChatRoomClient userB = createConnectedClient("userB");

    System.setOut(new PrintStream(outB));
    System.setIn(new ByteArrayInputStream("!userA\nlogoff\n".getBytes()));
    Thread.sleep(1000);

    String outAString = outA.toString();
    Assertions.assertFalse(outAString.contains("userB -> you (private):"), "A should get an insult from B");
  }

  @Test
  public void testUnknownMessageType() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    ChatRoomClient client = new ChatRoomClient();
    client.isConnected = true;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);
    dout.writeInt(9999);
    new ConnectResponse(true, "Disconnect").writeMessage(dout);
    dout.flush();

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();
    String output = out.toString();
    Assertions.assertFalse(output.contains("Unknown message type received from server."));
    Assertions.assertFalse(output.contains("Disconnect"));
  }

  @Test
  public void testIOException() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    ChatRoomClient client = new ChatRoomClient();
    client.isConnected = true;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);
    dout.writeInt(ChatRoomConstants.BROADCAST_MESSAGE);

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();
    String outputStr = out.toString();
    Assertions.assertTrue(outputStr.contains("--Error: Connection to the server was lost."));
    Assertions.assertFalse(client.isConnected);
  }
}
