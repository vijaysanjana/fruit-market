import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class AccountManager {


    public User login(String email, String password) {
        String username;
        String userType;
        try {
            File f = new File("userData");
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            int index = 0;
            String emailLine = String.format("Email: %s", email);
            String passwordLine = String.format("Password: %s", password);
            for (String line : lines) {
                if (line.equals(emailLine)) {
                    if (lines.get(index + 1).equals(passwordLine)) {
                        username = lines.get(index - 2).substring(10); //starts after "Username: "
                        userType = lines.get(index - 1).substring(11); //starts after "User Type: "
                        if (userType.equals("Customer"))
                            return new Customer(username, email, password);
                        else if (userType.equals("Seller"))
                            return new Seller(username, email, password);
                    }
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User signup(String email, String username, String password, String userType) {
        try {
            File f = new File("userData");
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            String emailLine = String.format("Email: %s", email);
            for (String line : lines) {
                if (line.equals(emailLine)) {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (userType.equals("Customer")) {
            User newUser = new Customer(username, email, password);
            newUser.pushToFile("userData");
            return newUser;
        } else if (userType.equals("Seller")) {
            User newUser = new Seller(username, email, password);
            newUser.pushToFile("userData");
            return newUser;
        }
        return null;
    }

}
