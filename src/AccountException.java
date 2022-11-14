/**
 * Exception thrown when account system failed
 */
public class AccountException extends Exception {
    public AccountException(String message) {
        super("FATAL ERROR OCCURRED! " + message);
    }

    public AccountException() {
        super("FATAL ERROR OCCURRED! USER ACCOUNT SYSTEM FAILED!");
    }
}
