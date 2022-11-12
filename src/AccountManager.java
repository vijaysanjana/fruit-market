import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class AccountManager {


    public static User login(String email, String password) {
        String username;
        String userType;
        try {
            File f = new File("userData");
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.toLowerCase().contains(email.toLowerCase())) {
                        if (line.contains(password)) {
                            username = line.substring(line.indexOf(":" + 1), line.indexOf(";"));
                            if (line.substring(0, 1).equals("S"))
                                return new Seller(username, email, password);
                            else if (line.substring(0, 1).equals("C"))
                                return new Customer(username, email, password);

                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User signup(String email, String username, String password, String userType) {
        try {
            File f = new File("userData");
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.toLowerCase().contains(email.toLowerCase()))
                        return null;
                    if (line.contains(username))
                        return null;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (userType.equals("customer")) {
            User newUser = new Customer(username, email, password);
            newUser.pushToFile("userData");
            return newUser;
        } else if (userType.equals("seller")) {
            User newUser = new Seller(username, email, password);
            newUser.pushToFile("userData");
            return newUser;
        }
        return null;
    }
}
