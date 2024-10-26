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
    public static final String NEGATIVE_VALUES = "You cannot create the two boards with negative or zero h , w";

    public PuzzleException(String message) {
        super(message);
    }
}
