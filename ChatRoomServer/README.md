# Chat Room Server and Client

## Entry Point

The entry point for the program is the ChatRoomServer and ChatRoomClient classes, which contain the main methods. 
### How to run?
First run ChatRoomServer in IntelliJ, after showing the port number (which is 1234 in our code), modify the configuration in ChatRoomClient: set the arguments in format <server_ip> <server_port> <username>. Ip could be your local ip or the string "localhost", port is the number 1234, followed with a name as you prefer.

### Key Classes and Methods
* **ChatRoomServer:**

  * The main class where the server starts. It initializes the server socket, listens for incoming client connections, and manages multiple client threads.
  * Key method: startServer() – Starts the server, listens for client connections, and handles communication with clients.
* **ChatRoomClient:**

  * The main class for client-side communication. It connects to the server, sends messages, and receives messages from other clients.
  * Key method: connectToServer() – Connects the client to the server, handles input and output streams for communication.

* **ClientHandler:**

  * Handles communication for individual clients on the server. Each connected client is managed in a separate thread to handle multiple clients simultaneously.
  * Key method: run() – Manages the input/output streams for a specific client and facilitates message transmission to and from the server.
* **BaseMessage:**

  * Represents the message format for communication between clients. It contains the necessary properties like sender's name, message content, and message type.
  * Key method: serialize() – Converts the message object into a string for transmission over the network.
  
## Assumptions

* Server Connection Failure: If the client cannot connect to the server, an error message will be displayed, and the client will be prompted to try connecting again or exit.
* Client Disconnection: If a client disconnects unexpectedly, the server will handle the situation gracefully by removing the client and notifying the remaining users of the disconnection.
* Invalid Input for Message Sending: If a user inputs invalid characters or empty messages, the client will display an error message and prompt the user for a valid message.
* Server Overload: If the server reaches the maximum allowed number 10 of connections, it will reject new clients and notify them that the server is full.

## Steps Taken to Ensure Correctness

* Error Handling: Comprehensive error handling ensures that invalid user inputs, connection issues, and server overload scenarios are properly managed. And after trying different inputs, we added error messages to guide the user through correcting any invalid inputs.
* Testing: The interactive components were tested to ensure appropriate responses to various user inputs.

## Running the Program

### From IntelliJ

1. Set up the run configuration for both the ChatServer and ChatClient classes
2. For ChatServer, start the server by running the main method. 
3. For ChatClient, provide the server address and port as arguments and run the client, like: localhost 1234 k. Also remember to check "multiple instances" in "modify options".
4. The client will prompt for user input and allow sending messages to the server.
5. The server will broadcast messages to all connected clients and display incoming messages.