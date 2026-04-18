package com.cryptachain.sdk;

/**
 * Exception thrown when the API returns a 404 for a chain-related resource.
 */
public class ChainNotFoundException extends CryptaChainException {

    /**
     * Creates a new ChainNotFoundException.
     *
     * @param message   the error message
     * @param errorCode the API error code
     * @param requestId the request ID
     */
    public ChainNotFoundException(String message, String errorCode, String requestId) {
        super(message, 404, errorCode, requestId);
    }
}
