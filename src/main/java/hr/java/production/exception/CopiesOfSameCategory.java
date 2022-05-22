package hr.java.production.exception;

/**
 * thrown when a copy of the category already exists
 */
public class CopiesOfSameCategory extends RuntimeException {

    /**
     * CopiesOfSameCategory constructor with only message
     *
     * @param message error message
     */
    public CopiesOfSameCategory(String message) {
        super(message);
    }

    /**
     * CopiesOfSameCategory constructor with message and cause
     *
     * @param message error message
     * @param cause   cause of error
     */

    public CopiesOfSameCategory(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * CopiesOfSameCategory constructor with only message
     *
     * @param cause cause of error
     */
    public CopiesOfSameCategory(Throwable cause) {
        super(cause);
    }
}
