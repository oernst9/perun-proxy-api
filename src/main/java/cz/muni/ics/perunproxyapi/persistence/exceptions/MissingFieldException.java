package cz.muni.ics.perunproxyapi.persistence.exceptions;

/**
 * Exception should be thrown at RpcMapper when a required field is missing
 *
 * @author Ondřej Ernst
 */
public class MissingFieldException extends RuntimeException {

    public MissingFieldException() {
        super();
    }

    public MissingFieldException(String message) {
        super(message);
    }

    public MissingFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingFieldException(Throwable cause) {
        super(cause);
    }

    protected MissingFieldException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
