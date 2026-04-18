package com.cryptachain.sdk;

/**
 * Exception thrown when the API returns a 401 Unauthorized response.
 *
 * <p>This typically indicates an invalid or expired API key.</p>
 */
public class AuthenticationException extends CryptaChainException {

    /**
     * Creates a new AuthenticationException.
     *
     * @param message   the error message
     * @param errorCode the API error code
     * @param requestId the request ID
     */
    public AuthenticationException(String message, String errorCode, String requestId) {
        super(message, 401, errorCode, requestId);
    }
}
