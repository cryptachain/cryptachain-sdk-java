package com.cryptachain.sdk;

/**
 * Base exception for all CryptaChain SDK errors.
 *
 * <p>Contains the HTTP status code, an API error code, and the request ID for debugging.</p>
 */
public class CryptaChainException extends RuntimeException {

    private final int statusCode;
    private final String errorCode;
    private final String requestId;

    /**
     * Creates a new CryptaChainException.
     *
     * @param message    the error message
     * @param statusCode the HTTP status code
     * @param errorCode  the API error code (may be null)
     * @param requestId  the request ID for debugging (may be null)
     */
    public CryptaChainException(String message, int statusCode, String errorCode, String requestId) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.requestId = requestId;
    }

    /**
     * Creates a new CryptaChainException wrapping a cause.
     *
     * @param message the error message
     * @param cause   the underlying cause
     */
    public CryptaChainException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
        this.errorCode = null;
        this.requestId = null;
    }

    /** Returns the HTTP status code, or 0 if not applicable. */
    public int statusCode() {
        return statusCode;
    }

    /** Returns the API error code, or null if not available. */
    public String errorCode() {
        return errorCode;
    }

    /** Returns the request ID for debugging, or null if not available. */
    public String requestId() {
        return requestId;
    }
}
