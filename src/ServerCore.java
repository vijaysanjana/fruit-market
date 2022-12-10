import java.io.*;
import java.net.*;
import java.util.ArrayList;

// Server class
class ServerCore {

    private static MarketPlace mp = new MarketPlace();

    public static void main(String[] args) {
        ServerSocket server = null;
        //FileManager.loadAllStores(mp, true);
        DataManager.loadEverything(mp);


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
                System.out.println("New client connected"); //Test

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
        private User user;
        private ShoppingCart shoppingCart;

        // Constructor
        public ClientHandler(Socket client) {
            this.clientSocket = client;
        }

        public void run() {
            PrintWriter serverOut = null;
            BufferedReader clientIn = null;
            try {

                //Client Input and Output
                serverOut = new PrintWriter(clientSocket.getOutputStream(), true);
                clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                System.out.println("Server looking for request..."); //Test
                String[] request = interpretRequest(clientIn.readLine());
                System.out.println("Server interpreted request"); //Test
                String response;
                while (request != null) {
                    System.out.println("Request is not null"); //Test

                    switch (request[0]) {
                        case "{Login}":
                            response = loginRequest(request);
                            serverOut.println(response);
                            System.out.println("Response sent"); //Test
                            //Interpret Request
                            //Update Data Classes
                            //Construct Necessary Data for Client in Response Format
                            //Send Response to ClientCore
                            break;


                        case "{Signup}":
                            response = signupRequest(request);
                            serverOut.println(response);
                            break;


                        case "{getTotalHeldProducts}":
                            response = getTotalHeldProductsRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getUserBasicData}":
                            response = getUserBasicDataRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getAllStores}":
                            response = getAllStoresRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getStore}":
                            response = getStoreRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getAllProducts}":
                            response = getAllProductsRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getProduct}":
                            response = getProductRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getProducts}":
                            response = getProductsRequest(request);
                            serverOut.println(response);
                            break;

                        default:
                    }

                    DataManager.saveEverything(mp);
                    System.out.println("Server looking for request..."); //Test
                    request = interpretRequest(clientIn.readLine());
                    System.out.println("Server interpreted request"); //Test
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
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (serverOut != null) {
                        serverOut.close();
                    }
                    if (clientIn != null) {
                        clientIn.close();
                        clientSocket.close();
                    }
                    System.out.println("Closing client thread"); //Test
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public String[] interpretRequest(String request) {
            if (request == null) {
                return null;
            }
            return request.split(",");
        }

        public String loginRequest(String[] request) {
            System.out.println("Login request found"); //Test
            String email = request[1];
            String password = request[2];

            user = AccountManager.login(ServerCore.mp, email, password);
            if (user != null) {
                String userType = (user instanceof Customer) ? "C" : "S";
                String response = "{Login}," + userType + "," + user.getUsername();
                System.out.println("Response compiled"); //Test
                //System.out.println(mp.getCustomer(0).getUsername());
                //System.out.println(mp.getCustomer(1).getUsername());
                //System.out.println(mp.getCustomer(2).getUsername());
                return response;
            }
            return null;
        }

        public String signupRequest(String[] request) {
            System.out.println("Signup request found"); //Test
            String username = request[1];
            String email = request[2];
            String password = request[3];
            String customerSeller = request[4];

            if (AccountManager.signup(username, email, password, customerSeller) != null) {
                if (customerSeller.equals("customer")) {
                    mp.addCustomer(new Customer(username, email, password));
                } else {
                    mp.addSeller(new Seller(username, email, password));
                }
                return "{Signup}";
            }
            return null;
        }

        public String getTotalHeldProductsRequest(String[] request) {
            return "{getTotalHeldProducts}," + ((Customer) user).getShoppingCart().getTotalheldProducts();
        }

        //public String getStoreNamesRequest(String[] request) {

        //}

        //gets all store names of initialized stores
        public String getAllStoresRequest(String[] request) {
            String response = "";
            ArrayList<Store> list = mp.getStores();
            for (Store s : list) {
                if (list.indexOf(s) == list.size() - 1) response += s.getName();
                else response += s.getName() + "|";
            }
            return "{getAllStores}," + response;
        }

        //gets all product names of all initialized stores
        public String getAllProductsRequest(String[] request) {
            String response = "";
            ArrayList<Store> list = mp.getStores();
            for (Store s : list) {
                if (list.indexOf(s) == list.size() - 1) response += s.getName();
                else response += s.getName() + "|";
            }
            return "{getAllProducts}," + response;
        }

        //gets all products and their data of certain store
        public String getProductsRequest(String[] request) {
            String temp = "";
            String storeName = request[1];
            Store store = mp.getStore(storeName);
            for (Product p : store.getProducts()) {
                temp += p.getName() + "|" + p.getDescription() + "|" + String.format("%.2f", p.getPrice()) + "|" + p.getQuantity();
                if (store.getProducts().indexOf(p) != store.getProducts().size() - 1) // not last item in list
                    temp += ",";
            }
            return "{getProducts}," + temp;
        }

        //gets all info of certain store
        public String getStoreRequest(String[] request) {
            String products = "";
            String sales = "";
            String storeName = request[1];
            Store store = mp.getStore(storeName);
            for (Product p : store.getProducts()) {
                if (store.getProducts().indexOf(p) == store.getProducts().size() - 1) //last item in list
                    products += p.getName();
                else products += p.getName() + "|";
            }
            for (Sale s : store.getSales()) {
                if (store.getSales().indexOf(s) == store.getSales().size() - 1) //last item in list
                    sales += s.getName();
                else sales += s.getName() + "|";
            }
            return "{getStore}," + store.getName() + "," + store.getDescription() + "," + products + "," + sales + "," + store.getTotalSoldProducts();
        }

        //gets product and its data from store name and array index of Store array
        public String getProductRequest(String[] request) {
            Store store = mp.getStore(request[1]);
            Product p = store.getProduct(Integer.parseInt(request[2]));
            return "{getProduct}," + p.getName() + "," + p.getDescription() + "," + String.format("%.2f",
                    p.getPrice()) + "," + p.getQuantity();
        }

        public String getUserBasicDataRequest(String[] request) {
            return "{getUserBasicData}," + user.getEmail() + "," + user.getUsername();
        }

    }
}