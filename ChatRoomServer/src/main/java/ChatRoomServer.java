import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The {@code ChatRoomServer} class represents the server in a chat room system. It manages client
 * connections, broadcasts messages, handles direct messages, and processes other client
 * interactions.
 * <p>
 * This server supports multiple clients concurrently and ensures thread safety through the use of
 * {@link CopyOnWriteArrayList} for managing connected clients.
 * <p>
 * Features include: - Accepting client connections - Handling broadcast and direct messages -
 * Managing connected users - Sending failure notifications to clients
 * <p>
 * The server listens for incoming connections on a specified port and processes messages from
 * connected clients in separate threads.
 */
public class ChatRoomServer {

  ServerSocket serverSocket;
  /**
   * A thread-safe list of all currently connected clients.
   */
  protected final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

  private String invalidSender = "Invalid sender username.";
  private String invalidRecipient = "Invalid recipient username.";
  private String emptyMessage = "message cannot be empty.";
  private String broadcastFailed = "Failed to send message to client: ";
  private String directFailed = "Failed to send direct message to ";
  private String failedFailed = "Failed to send failed message to ";
  private String removeFailed = "Failed to broadcast leaving message.";

  /**
   * Constructs a {@code ChatRoomServer} and binds it to the specified port.
   *
   * @param port the port number to bind the server to
   * @throws IOException if an I/O error occurs while creating the server socket
   */
  public ChatRoomServer(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    System.out.println("Server started on port " + port);
  }

  /**
   * Starts the server, accepting client connections in an infinite loop. For each connection, a new
   * {@link ClientHandler} is created and started in a separate thread.
   * <p>
   * If the maximum number of clients is reached, new connections are rejected.
   */
  public void startServer() {
    while (true) {
      try {
        Socket clientSocket = serverSocket.accept();
        if (clients.size() < ChatRoomConstants.MAX_CLIENTS) {
          ClientHandler clientHandler = new ClientHandler(clientSocket, this);
          clients.add(clientHandler);
          new Thread(clientHandler).start();
        } else {
          System.out.println("Maximum client limit reached. Connection rejected.");
          clientSocket.close();
        }
      } catch (IOException e) {
        System.err.println("Error accepting client connection: " + e.getMessage());
      }
    }
  }

  /**
   * Checks if a user is currently connected to the server.
   *
   * @param username the username to check
   * @return {@code true} if the user is connected; {@code false} otherwise
   */
  public boolean isUserConnected(String username) {
    for (ClientHandler c : clients) {
      if (username.equalsIgnoreCase(c.getUsername())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieves the {@link ClientHandler} for the specified username.
   *
   * @param username the username of the client
   * @return the {@code ClientHandler} if the user is found; {@code null} otherwise
   */
  public ClientHandler getClientByUsername(String username) {
    for (ClientHandler c : clients) {
      if (username.equalsIgnoreCase(c.getUsername())) {
        return c;
      }
    }
    return null;
  }

  /**
   * Broadcasts a message to all connected clients.
   * <p>
   * If the sender is invalid or the message content is empty, a failure message is sent to the
   * sender instead.
   *
   * @param message the {@link BroadcastMessage} to broadcast
   */
  public void broadcastMessage(BroadcastMessage message) {
    if (!isUserConnected(message.getSender())) {
      sendFailedMessageTo(message.getSender(), invalidSender);
      return;
    }
    if (message.getContent().isEmpty()) {
      sendFailedMessageTo(message.getSender(), "Broadcast " + emptyMessage);
      return;
    }
    for (ClientHandler client : clients) {
      try {
        client.sendMessage(message);
      } catch (IOException e) {
        System.err.println(broadcastFailed + client.getUsername());
      }
    }
  }

  /**
   * Sends a direct message to a specific recipient.
   * <p>
   * If the sender or recipient is invalid, or if the message content is empty, a failure message is
   * sent to the sender instead.
   *
   * @param message the {@link DirectMessage} to send
   */
  public void sendDirectMessage(DirectMessage message) {
    if (!isUserConnected(message.getSender())) {
      sendFailedMessageTo(message.getSender(), invalidSender);
      return;
    }
    if (!isUserConnected(message.getRecipient())) {
      sendFailedMessageTo(message.getSender(), invalidRecipient);
      return;
    }
    if (message.getContent().isEmpty()) {
      sendFailedMessageTo(message.getSender(), "Direct " + emptyMessage);
      return;
    }

    ClientHandler recipientHandler = getClientByUsername(message.getRecipient());
    if (recipientHandler != null) {
      try {
        recipientHandler.sendMessage(message);
      } catch (IOException e) {
        System.err.println(directFailed + recipientHandler.getUsername());
      }
    }
  }

  /**
   * Sends an insult message to a specific recipient.
   * <p>
   * If the sender or recipient is invalid, a failure message is sent to the sender. Otherwise, a
   * random insult is generated and sent as a direct message.
   *
   * @param message the {@link SendInsult} message containing the sender and recipient
   */
  public void sendInsult(SendInsult message) {
    if (!isUserConnected(message.getSender())) {
      sendFailedMessageTo(message.getSender(), invalidSender);
      return;
    }
    if (!isUserConnected(message.getRecipient())) {
      sendFailedMessageTo(message.getSender(), invalidRecipient);
      return;
    }

    String insult = new RandomSentenceGenerator().generateInsult();
    DirectMessage dm = new DirectMessage(message.getSender(), message.getRecipient(), insult);
    sendDirectMessage(dm);
  }

  /**
   * Sends a failure message to a specific client.
   *
   * @param username the username of the client to notify
   * @param reason   the reason for the failure
   */
  public void sendFailedMessageTo(String username, String reason) {
    ClientHandler ch = getClientByUsername(username);
    if (ch != null) {
      try {
        ch.sendMessage(new FailedMessage(reason));
      } catch (IOException e) {
        System.err.println(failedFailed + username);
      }
    }
  }

  /**
   * Removes a client from the server and notifies other clients of the disconnection.
   *
   * @param clientHandler the {@link ClientHandler} of the client to remove
   */
  public void removeClient(ClientHandler clientHandler) {
    clients.remove(clientHandler);
    if (clientHandler.getUsername() != null) {
      BroadcastMessage leaveMsg = new BroadcastMessage("Server",
          clientHandler.getUsername() + " has left the chat.");
      for (ClientHandler c : clients) {
        try {
          c.sendMessage(leaveMsg);
        } catch (IOException e) {
          System.err.println(removeFailed);
        }
      }
    }
  }

  /**
   * Retrieves a list of usernames of all connected clients, excluding a specified user.
   *
   * @param excludeUser the username to exclude from the list
   * @return a list of usernames of all other connected users
   */
  public List<String> getClientUsernames(String excludeUser) {
    List<String> usernames = new ArrayList<>();
    for (ClientHandler client : clients) {
      String user = client.getUsername();
      if (user != null && !user.equalsIgnoreCase(excludeUser)) {
        usernames.add(user);
      }
    }
    return usernames;
  }

  /**
   * The main entry point for the chat room server. Initializes the server on the specified port and
   * starts accepting client connections.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    try {
      ChatRoomServer server = new ChatRoomServer(ChatRoomConstants.SERVER_PORT);
      server.startServer();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
