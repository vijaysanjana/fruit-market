import java.util.*;
import java.io.*;
import java.net.*;

// Client class
class ClientCore {

    // driver code
    public static void main(String[] args)
    {
        // establish a connection by providing host and port
        // number
        try (Socket socket = new Socket("localhost", 9999)) {

            //Server Input and Output
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request = null; //Request to be sent to ServerCore
            String location = "1.0"; //Address of GUI currently visible to user
            while (!request.equalsIgnoreCase("Quit")) {

                switch (location) {
                    case "1.0":
                        //Open Login GUI
                        //Read User Input
                        //Close Login GUI
                        //Store User Input
                        //Send User Input as Request to ServerCore
                        //Receive Server Response
                        //Update Location
                        break;
                }

                //REFERENCE CODE
                /*
                // reading from user
                //request = sc.nextLine();

                // sending the user input to server
                out.println(request);
                out.flush();

                // displaying server reply
                System.out.println("Server replied " + in.readLine());
                */
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
