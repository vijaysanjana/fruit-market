import java.io.*;
import java.net.*;

// Server class
class ServerCore {
    public static void main(String[] args) {
        ServerSocket server = null;

        try {
            // server is listening on port 9999
            server = new ServerSocket(9999);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected");

                // create a new thread object
                ClientHandler clientSocket = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }





    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket client)
        {
            this.clientSocket = client;
        }

        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            try {

                //Client Input and Output
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String request = in.readLine();
                while (request != null) {

                    if (request.contains("[insert request here]")) {
                        //Interpret Request
                        //Update Data Classes
                        //Construct Necessary Data for Client in Response Format
                        //Send Response to ClientCore
                    }

                    request = in.readLine();
                }

                //REFERENCE CODE
                /*
                String line;
                while ((line = in.readLine()) != null) {

                    // writing the received message from
                    // client
                    System.out.printf(" Sent from the client: %s\n", line);
                    out.println(line);
                }
                */
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}