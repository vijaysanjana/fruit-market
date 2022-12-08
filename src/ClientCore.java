import java.util.*;
import java.io.*;
import java.net.*;

// Client class
class ClientCore {

    public static final String separator = "---------------------------";

    // driver code
    public static void main(String[] args)
    {
        // establish a connection by providing host and port
        // number
        try (Socket socket = new Socket("localhost", 9999)) {
            Scanner sc = new Scanner(System.in); //Test Scanner TODO: Remove Terminal Input

            //Server Input and Output
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request = ""; //Request to be sent to ServerCore
            String response = null; //Response to be sent from ServerCore
            String location = "1.0"; //Address of GUI currently visible to user
            while (!request.equalsIgnoreCase("Quit")) {

                switch (location) {
                    case "1.0": //Login Page
                        String email;
                        String password;
                        String loginSignup;
                        String customerSeller;
                        String username;

                        System.out.println("Welcome to The Marketplace!");
                        System.out.println(separator);
                        System.out.println("Login or signup to use our service.");
                        System.out.println("Please enter: " + "\n[1] Login" + "\n[2] Signup");
                        loginSignup = sc.nextLine();
                        if (loginSignup.equalsIgnoreCase("1")) {
                            System.out.println(separator);
                            System.out.println("Please enter your email: ");
                            email = sc.nextLine();
                            System.out.println("Please enter your password: ");
                            password = sc.nextLine();

                            request = "{Login}" + email + "," + password + ",";
                            out.println(request);

                            response = in.readLine();
                            String[] responseData = response.substring(response.indexOf("}") + 1).split(",");
                            customerSeller = responseData[0];
                            username = responseData[1];
                            String store = responseData[2];
                            //customerSeller = response.substring((response.indexOf("}") + 1), response.indexOf(","));
                            //username = response.substring((response.indexOf(",") + 1), response.lastIndexOf(","));
                            System.out.println(separator);
                            System.out.println("Welcome customer: " + username);
                            System.out.println("Highlighed store: " + store);
                        }
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
