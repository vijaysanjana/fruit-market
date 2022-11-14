/**
 * Thrown when data read or write failed
 */
public class DataException extends Exception {
    public DataException(String message) {
        super("FATAL ERROR OCCURRED! " + message);
    }

    public DataException() {
        super("FATAL ERROR OCCURRED! DATA SYSTEM FAILED!");
    }
}
