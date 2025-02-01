import org.junit.jupiter.api.*;
import java.io.*;
import java.util.Collections;

public class ChatRoomClientReadMessagesTest {

  ChatRoomClient client;
  ByteArrayOutputStream testOut;

  @BeforeEach
  public void setUp() {
    client = new ChatRoomClient();
    client.isConnected = true;

    testOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testOut));
  }

  @AfterEach
  public void tearDown() {
    System.setOut(System.out);
  }

  private void writeMessage(DataOutputStream out, BaseMessage msg) throws IOException {
    msg.writeMessage(out);
  }

  @Test
  public void testBroadcastMessage() throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);

    writeMessage(dout, new BroadcastMessage("alice", "hello all"));
    writeMessage(dout, new ConnectResponse(true,"Bye"));

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();

    String output = testOut.toString();
    Assertions.assertTrue(output.contains("alice -> all: hello all"));
    Assertions.assertTrue(output.contains("Bye"));
  }

  @Test
  public void testQueryUserResponse_Empty() throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);

    writeMessage(dout, new QueryUserResponse(Collections.emptyList()));
    writeMessage(dout, new ConnectResponse(true,"Disconnected"));

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();
    String output = testOut.toString();
    Assertions.assertTrue(output.contains("No other connected users."));
    Assertions.assertTrue(output.contains("Disconnected"));
  }

  @Test
  public void testQueryUserResponse_NonEmpty() throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);

    // 非空列表
    writeMessage(dout, new QueryUserResponse(java.util.Arrays.asList("bob", "charlie")));
    writeMessage(dout, new ConnectResponse(true, "Done"));

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();
    String output = testOut.toString();
    Assertions.assertTrue(output.contains("Connected users: bob, charlie"));
    Assertions.assertTrue(output.contains("Done"));
  }

  @Test
  public void testDirectMessage() throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);

    writeMessage(dout, new DirectMessage("dave","you","secret message"));
    writeMessage(dout, new ConnectResponse(true, "Stop"));

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();
    String output = testOut.toString();
    Assertions.assertTrue(output.contains("dave -> you (private): secret message"));
    Assertions.assertTrue(output.contains("Stop"));
  }

  @Test
  public void testFailedMessage() throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);

    writeMessage(dout, new FailedMessage("Some error"));
    writeMessage(dout, new ConnectResponse(true, "Close"));

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();
    String output = testOut.toString();
    Assertions.assertTrue(output.contains("Server Error: Some error"));
    Assertions.assertTrue(output.contains("Close"));
  }

  @Test
  public void testConnectResponse() throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);

    writeMessage(dout, new ConnectResponse(true,"Goodbye"));

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();

    String output = testOut.toString();
    Assertions.assertTrue(output.contains("Goodbye"));
    Assertions.assertFalse(client.isConnected);
  }

  @Test
  public void testUnknownMessageType() throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);

    dout.writeInt(9999);
    writeMessage(dout, new ConnectResponse(true, "Done"));

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();
    String output = testOut.toString();
    Assertions.assertFalse(output.contains("Unknown message type received from server."));
    Assertions.assertFalse(output.contains("Done"));
  }

  @Test
  public void testIOException() throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bos);

    dout.writeInt(ChatRoomConstants.BROADCAST_MESSAGE);

    client.in = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
    client.out = new DataOutputStream(new ByteArrayOutputStream());

    client.readMessages();
    String output = testOut.toString();
    Assertions.assertTrue(output.contains("--Error: Connection to the server was lost."));
  }
}
