package org.product_service.exception.wrapper;

public class InsufficientQuantityException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InsufficientQuantityException() {
        super();
    }

    public InsufficientQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientQuantityException(String message) {
        super(message);
    }

    public InsufficientQuantityException(Throwable cause) {
        super(cause);
    }
}
