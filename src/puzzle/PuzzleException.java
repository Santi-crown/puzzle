package puzzle;

/**
 * This class implements a custom exception for handling errors within the Puzzle application.
 * It extends the base Exception class to provide specific error handling functionality.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class PuzzleException extends Exception {
    public static final String INVALID_HW_NEGATIVE_VALUES = "You cannot create either board with a negative or zero value for h or w.";
    public static final String INVALID_HW_GREATER_VALUES = "You cannot create a board with h or w values greater than 500.";

    public PuzzleException(String message) {
        super(message);
    }
}
