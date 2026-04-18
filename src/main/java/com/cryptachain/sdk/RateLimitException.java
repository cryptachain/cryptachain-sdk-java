package com.cryptachain.sdk;

import java.time.Duration;

/**
 * Exception thrown when the API returns a 429 Too Many Requests response.
 *
 * <p>Contains information about when the rate limit resets and how many requests remain.</p>
 */
public class RateLimitException extends CryptaChainException {

    private final Duration retryAfter;
    private final int remaining;

    /**
     * Creates a new RateLimitException.
     *
     * @param message    the error message
     * @param errorCode  the API error code
     * @param requestId  the request ID
     * @param retryAfter how long to wait before retrying
     * @param remaining  the number of remaining requests in the current window
     */
    public RateLimitException(String message, String errorCode, String requestId,
                              Duration retryAfter, int remaining) {
        super(message, 429, errorCode, requestId);
        this.retryAfter = retryAfter;
        this.remaining = remaining;
    }

    /** Returns the duration to wait before retrying. */
    public Duration retryAfter() {
        return retryAfter;
    }

    /** Returns the number of remaining requests in the current rate-limit window. */
    public int remaining() {
        return remaining;
    }
}
