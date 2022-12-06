/**
 * Thrown when data read or write failed
 * @author Jack, Nathan, Sanjana, Tommy, Aadit
 * @version 11/14/2022
 */
public class DataException extends Exception {
    public DataException(String message) {
        super("FATAL ERROR OCCURRED! " + message);
    }

    public DataException() {
        super("FATAL ERROR OCCURRED! DATA SYSTEM FAILED!");
    }
}
