package com.ozonehis.camel.frappe.sdk.api;

import java.io.Serial;

public class FrappeClientException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * This constructor creates a new FrappeClientException instance.
     *
     * @param message The exception message.
     */
    public FrappeClientException(String message) {
        super(message);
    }

    /**
     * This constructor creates a new FrappeClientException instance.
     *
     * @param message The exception message.
     * @param cause The exception cause.
     */
    public FrappeClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * This constructor creates a new FrappeClientException instance.
     *
     * @param cause The exception cause.
     */
    public FrappeClientException(Throwable cause) {
        super(cause);
    }
}
