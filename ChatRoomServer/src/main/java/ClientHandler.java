import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Objects;

/**
 * The {@code ClientHandler} class represents a handler for an individual client in the chat room
 * system. It manages communication with the client, processes incoming messages, and ensures proper
 * message routing.
 * <p>
 * This class runs in its own thread to handle communication with a specific client. It interacts
 * with the {@link ChatRoomServer} to broadcast messages, handle direct messages, and manage user
 * connections.
 */
public class ClientHandler implements Runnable {

  private final Socket clientSocket;
  private final ChatRoomServer server;
  private DataInputStream in;
  private DataOutputStream out;
  private String username;
  private boolean connected = false;

  /**
   * Constructs a {@code ClientHandler} for a specific client socket and server.
   *
   * @param clientSocket the socket used to communicate with the client
   * @param server       the {@link ChatRoomServer} managing this client handler
   */
  public ClientHandler(Socket clientSocket, ChatRoomServer server) {
    this.clientSocket = clientSocket;
    this.server = server;
  }

  /**
   * Returns the username of the connected client.
   *
   * @return the client's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * The main execution method for the client handler.
   * <p>
   * This method handles the following: - Reads the initial {@link ConnectMessage} to establish the
   * client's username - Processes various message types (broadcast, direct message, disconnect,
   * etc.) - Ensures the client is cleaned up upon disconnection
   */
  @Override
  public void run() {
    try {
      in = new DataInputStream(clientSocket.getInputStream());
      out = new DataOutputStream(clientSocket.getOutputStream());

      // Expect a ConnectMessage first
      BaseMessage msg = BaseMessage.readMessage(in);
      if (msg instanceof ConnectMessage) {
        ConnectMessage cm = (ConnectMessage) msg;
        String user = cm.getUsername();
        if (user == null || user.trim().isEmpty() || server.isUserConnected(user)) {
          sendMessage(new ConnectResponse(false, "Invalid or already-taken username."));
          cleanup();
          return;
        }
        username = user;
        connected = true;
        int otherCount = server.getClientUsernames(username).size();
        sendMessage(new ConnectResponse(true,
            "Connected as " + username + ". There are " + otherCount
                + " other connected clients."));
        server.broadcastMessage(new BroadcastMessage("Server", username + " has joined the chat."));
      } else {
        sendMessage(new ConnectResponse(false, "No CONNECT_MESSAGE received."));
        cleanup();
        return;
      }

      while (connected) {
        BaseMessage message;
        try {
          message = BaseMessage.readMessage(in);
        } catch (IOException e) {
          break;
        }

        if (message instanceof DisconnectMessage) {
          DisconnectMessage dm = (DisconnectMessage) message;
          if (dm.getUsername().equalsIgnoreCase(username)) {
            sendMessage(new ConnectResponse(true, "You are no longer connected."));
            break;
          } else {
            sendMessage(new ConnectResponse(false, "Invalid user for disconnect."));
          }
        } else if (message instanceof BroadcastMessage) {
          server.broadcastMessage((BroadcastMessage) message);
        } else if (message instanceof DirectMessage) {
          server.sendDirectMessage((DirectMessage) message);
        } else if (message instanceof QueryConnectedUsers) {
          QueryConnectedUsers query = (QueryConnectedUsers) message;
          if (query.getUsername().equalsIgnoreCase(username)) {
            List<String> others = server.getClientUsernames(username);
            sendMessage(new QueryUserResponse(others));
          } else {
            sendMessage(new FailedMessage("User not recognized or not connected."));
          }
        } else if (message instanceof SendInsult) {
          server.sendInsult((SendInsult) message);
        } else {
          sendMessage(new FailedMessage("Unknown request."));
        }
      }
    } catch (IOException e) {
      System.err.println("Connection lost with " + username);
    } finally {
      cleanup();
    }
  }

  /**
   * Sends a {@link BaseMessage} to the client.
   *
   * @param message the message to send
   * @throws IOException if an I/O error occurs while sending the message
   */
  public void sendMessage(BaseMessage message) throws IOException {
    message.writeMessage(out);
  }

  /**
   * Cleans up the client handler by: - Marking the client as disconnected - Removing the client
   * from the server's list - Closing the socket
   */
  void cleanup() {
    connected = false;
    server.removeClient(this);
    try {
      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientHandler that = (ClientHandler) o;
    return connected == that.connected && Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, connected);
  }

  @Override
  public String toString() {
    return "ClientHandler{" +
        "connected=" + connected +
        ", username='" + username + '\'' +
        '}';
  }
}
