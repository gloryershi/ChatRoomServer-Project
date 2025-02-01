import java.io.*;
import java.net.*;
import java.util.Objects;

/**
 * The {@code ChatRoomClient} class represents a client in a chat room system. It handles the
 * connection to a chat server, processes user commands, and manages communication with the server,
 * including sending and receiving messages.
 */
public class ChatRoomClient {

  /**
   * The socket used to connect to the server.
   */
  public Socket socket;
  /**
   * Input stream for reading messages from the server.
   */
  public DataInputStream in;
  /**
   * Output stream for sending messages to the server.
   */
  public DataOutputStream out;
  /**
   * Indicates whether the client is currently connected to the server.
   */
  public boolean isConnected = true;
  /**
   * Command to log off from the server.
   */
  public String logoffCommand = "logoff";
  /**
   * Command to send a broadcast message to all users.
   */
  public String allCommand = "@all";
  /**
   * Command to send a direct message to a specific user.
   */
  public String directCommand = "@";
  /**
   * Command to query the list of connected users.
   */
  public String whoCommand = "who";
  /**
   * Command to send an insult to a specific user.
   */
  public String insultCommand = "!";
  /**
   * Command to display the help menu.
   */
  public String helpCommand = "?";

  /**
   * Connects the client to the server using the specified IP, port, and username. Once connected,
   * it starts a thread to read messages from the server and handles user input from the console.
   *
   * @param serverIp   the IP address of the server
   * @param serverPort the port number of the server
   * @param username   the username for the client
   */
  public void connect(String serverIp, int serverPort, String username) {
    try {
      socket = new Socket(serverIp, serverPort);
      in = new DataInputStream(socket.getInputStream());
      out = new DataOutputStream(socket.getOutputStream());

      sendMessage(new ConnectMessage(username));
      BaseMessage response = BaseMessage.readMessage(in);
      if (response instanceof ConnectResponse) {
        ConnectResponse cr = (ConnectResponse) response;
        System.out.println(cr.getMessage());
      }

      new Thread(this::readMessages).start();

      handleUserInput(username);

    } catch (IOException e) {
      System.err.println("Failed to connect to the server: " + e.getMessage());
    } finally {
      if (!isConnected) {
        disconnect();
      }
    }
  }

  /**
   * Continuously reads messages from the server and processes them based on their type. Supported
   * message types include: {@code BroadcastMessage} {@code QueryUserResponse} {@code DirectMessage}
   * {@code FailedMessage} {@code ConnectResponse}
   * <p>
   * If an unknown message type is received or an {@link IOException} occurs, the client will
   * disconnect.
   */
  public void readMessages() {
    try {
      while (isConnected) {
        BaseMessage message = BaseMessage.readMessage(in);

        if (message instanceof BroadcastMessage) {
          BroadcastMessage broadcast = (BroadcastMessage) message;
          System.out.println(broadcast.getSender() + " -> all: " + broadcast.getContent());
        } else if (message instanceof QueryUserResponse) {
          QueryUserResponse response = (QueryUserResponse) message;
          if (response.getUsers().isEmpty()) {
            System.out.println("No other connected users.");
          } else {
            System.out.println("Connected users: " + String.join(", ", response.getUsers()));
          }
        } else if (message instanceof DirectMessage) {
          DirectMessage dm = (DirectMessage) message;
          System.out.println(dm.getSender() + " -> you (private): " + dm.getContent());
        } else if (message instanceof FailedMessage) {
          FailedMessage fm = (FailedMessage) message;
          System.out.println("Server Error: " + fm.getReason());
        } else if (message instanceof ConnectResponse) {
          // happen for disconnect
          ConnectResponse cr = (ConnectResponse) message;
          System.out.println(cr.getMessage());
          isConnected = false;
        } else {
          System.out.println("Unknown message type received from server.");
        }
      }
    } catch (IOException e) {
      if (isConnected) {
        System.out.println("--Error: Connection to the server was lost.");
      }
    } finally {
      disconnect();
    }
  }

  /**
   * Handles user input from the console and sends appropriate messages to the server.
   *
   * @param username the username of the client
   */
  public void handleUserInput(String username) {
    try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      while (isConnected && (input = console.readLine()) != null) {
        if (input.equalsIgnoreCase(logoffCommand)) {
          sendMessage(new DisconnectMessage(username));
          BaseMessage resp = BaseMessage.readMessage(in);
          if (resp instanceof ConnectResponse) {
            ConnectResponse cr = (ConnectResponse) resp;
            System.out.println(cr.getMessage());
          }
          isConnected = false;
          disconnect();
          break;
        } else if (input.startsWith(allCommand)) {
          String msg = input.substring(4).trim();
          sendMessage(new BroadcastMessage(username, msg));
        } else if (input.startsWith(directCommand)) {
          String[] parts = input.split(" ", 2);
          String recipient = parts[0].substring(1);
          String content = (parts.length < 2) ? "" : parts[1].trim();
          sendMessage(new DirectMessage(username, recipient, content));
        } else if (input.equalsIgnoreCase(whoCommand)) {
          sendMessage(new QueryConnectedUsers(username));
        } else if (input.startsWith(insultCommand)) {
          String recipient = input.substring(1).trim();
          if (recipient.isEmpty()) {
            recipient = ""; // Will cause failed message on server side
          }
          sendMessage(new SendInsult(username, recipient));
        } else if (input.equals(helpCommand)) {
          printHelpMenu();
        } else {
          System.out.println("--Error: Unknown command. Type '?' for help.");
        }
      }
    } catch (IOException e) {
      if (isConnected) {
        System.out.println("--Error reading input: " + e.getMessage());
      }
    }
  }

  /**
   * Sends a {@link BaseMessage} to the server.
   *
   * @param message the message to send
   * @throws IOException if an I/O error occurs while sending the message
   */
  public void sendMessage(BaseMessage message) throws IOException {
    message.writeMessage(out);
  }

  void disconnect() {
    try {
      isConnected = false;
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prints the help menu displaying available commands.
   */
  public void printHelpMenu() {
    System.out.println("Available commands:");
    System.out.println("? - Show this help menu");
    System.out.println("@username message - Send a direct message to a user");
    System.out.println("@all message - Broadcast a message to all users");
    System.out.println("who - List all connected users");
    System.out.println("logoff - Disconnect from the server");
    System.out.println("!username - Request the server to send an insult to a user");
  }

  private static Integer argsLength = 3;
  private static Integer ipIndex = 0;
  private static Integer portIndex = 1;
  private static Integer nameIndex = 2;

  /**
   * The main entry point for the {@code ChatRoomClient}. Takes server IP, port, and username as
   * command-line arguments to initiate a connection.
   *
   * @param args command-line arguments specifying server IP, port, and username
   */
  public static void main(String[] args) {
    if (args.length < argsLength) {
      System.out.println("Usage: <server_ip> <server_port> <username>");
      return;
    }

    String serverIp = args[ipIndex];
    int serverPort;
    try {
      serverPort = Integer.parseInt(args[portIndex]);
    } catch (NumberFormatException e) {
      System.out.println("Error: Invalid port number.");
      return;
    }

    String username = args[nameIndex];
    ChatRoomClient client = new ChatRoomClient();
    client.connect(serverIp, serverPort, username);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatRoomClient that = (ChatRoomClient) o;
    return isConnected == that.isConnected;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(isConnected);
  }

  @Override
  public String toString() {
    return "ChatRoomClient{" +
        "isConnected=" + isConnected +
        '}';
  }

}
