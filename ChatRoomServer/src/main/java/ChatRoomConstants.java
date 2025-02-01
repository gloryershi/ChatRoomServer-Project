/**
 * A utility class that defines constants used in the chat room system. These constants represent
 * message types, server configuration, and other system-wide parameters.
 */
public class ChatRoomConstants {

  /**
   * Message type identifier for a connect message. This message is used by clients to establish a
   * connection to the server.
   */
  public static final int CONNECT_MESSAGE = 19;

  /**
   * Message type identifier for a connect response. This message is sent by the server to
   * acknowledge or reject a client's connection request.
   */
  public static final int CONNECT_RESPONSE = 20;

  /**
   * Message type identifier for a disconnect message. This message is used by clients to notify the
   * server of their intention to disconnect.
   */
  public static final int DISCONNECT_MESSAGE = 21;

  /**
   * Message type identifier for a query connected users request. This message is used by clients to
   * request a list of currently connected users.
   */
  public static final int QUERY_CONNECTED_USERS = 22;

  /**
   * Message type identifier for a query user response. This message is sent by the server in
   * response to a query connected users request.
   */
  public static final int QUERY_USER_RESPONSE = 23;

  /**
   * Message type identifier for a broadcast message. This message is sent by clients to broadcast a
   * message to all connected users.
   */
  public static final int BROADCAST_MESSAGE = 24;

  /**
   * Message type identifier for a direct message. This message is sent by a client to another
   * specific client.
   */
  public static final int DIRECT_MESSAGE = 25;

  /**
   * Message type identifier for a failed message. This message is sent by the server to notify a
   * client of an error or failure.
   */
  public static final int FAILED_MESSAGE = 26;

  /**
   * Message type identifier for sending an insult. This message is sent by a client to request the
   * server to send an insult to another user.
   */
  public static final int SEND_INSULT = 27;

  /**
   * The default port number used by the server to accept client connections.
   */
  public static int SERVER_PORT = 1234;

  /**
   * The maximum number of clients that can connect to the server simultaneously.
   */
  public static final int MAX_CLIENTS = 10;
}
