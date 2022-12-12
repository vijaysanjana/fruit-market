import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


/**
 * User class contains the Username, Email, and Password associated with the User's account.
 *
 * @author Jack, Nathan, Sanjana, Tommy, Aadit
 * @version 11/14/2022
 */
public class User {

    private String username; //Username associated with account
    private String email; //Email associated with account
    private String password; //Password associated with account


    //Constructor
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    //Getters and Setters
    public synchronized String getUsername() {
        return username;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized String getEmail() {
        return email;
    }

    public synchronized void setEmail(String email) {
        this.email = email;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
    }

    public synchronized void pushToFile() {
        try {
            File f = new File("userData");
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            lines.add(String.format("U:%s;%s;%s", getUsername(), getEmail(), getPassword()));
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Equals
    @Override
    public synchronized boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return (username.equals(user.getUsername()) && email.equals(user.getEmail()) &&
                password.equals(user.getPassword()));
    }
}