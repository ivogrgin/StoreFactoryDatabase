package hr.java.production.exception;


/**
 * thrown when a copy of an article is already found in Factory or Store
 */
public class CopiesOfSameArticleInFactoryOrStore extends Exception {

    /**
     * CopiesOfSameArticleInFactoryOrStore constructor with message
     *
     * @param message error message
     */
    public CopiesOfSameArticleInFactoryOrStore(String message) {
        super(message);
    }


    /**
     * CopiesOfSameArticleInFactoryOrStore constructor with cause
     *
     * @param cause cause of error
     */
    public CopiesOfSameArticleInFactoryOrStore(Throwable cause) {
        super(cause);
    }

    /**
     * CopiesOfSameArticleInFactoryOrStore constructor with message and cause
     *
     * @param message error message
     * @param cause   cause of error
     */
    public CopiesOfSameArticleInFactoryOrStore(String message, Throwable cause) {
        super(message, cause);
    }

}
