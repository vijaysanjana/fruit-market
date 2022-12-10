import java.io.File;

/**
 * Thrown when data read or write failed
 * @author Jack, Nathan, Sanjana, Tommy, Aadit
 * @version 11/14/2022
 */
public class DataException extends Exception {
    public DataException(String stacktrace, String method, File file) {
        super("==== CATASTROPHIC ERROR ====" +
                "\nStackTrace: " + stacktrace +
                "\nMethod: " + method + " has failed" +
                "\nFile: " + file.getAbsolutePath() +
                "\n============================");
    }

    public DataException() {
        super("FATAL ERROR OCCURRED! DATA SYSTEM FAILED!");
    }
}
