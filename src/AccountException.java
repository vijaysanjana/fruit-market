/**
 * Exception thrown when account system failed
 * @author Jack, Nathan, Sanjana, Tommy, Aadit
 * @version 11/14/2022
 */
public class AccountException extends Exception {
    public AccountException(String message) {
        super("FATAL ERROR OCCURRED! " + message);
    }

    public AccountException() {
        super("FATAL ERROR OCCURRED! USER ACCOUNT SYSTEM FAILED!");
    }
}
