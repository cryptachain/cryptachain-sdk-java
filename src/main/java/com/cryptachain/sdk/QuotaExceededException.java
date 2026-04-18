package com.cryptachain.sdk;

import java.time.Instant;

/**
 * Exception thrown when the API returns a 402 Payment Required response.
 *
 * <p>Indicates that the account has exceeded its compute unit (CU) quota.</p>
 */
public class QuotaExceededException extends CryptaChainException {

    private final long usedCU;
    private final long limitCU;
    private final Instant resetAt;

    /**
     * Creates a new QuotaExceededException.
     *
     * @param message   the error message
     * @param errorCode the API error code
     * @param requestId the request ID
     * @param usedCU    the number of compute units used
     * @param limitCU   the compute unit limit
     * @param resetAt   when the quota resets
     */
    public QuotaExceededException(String message, String errorCode, String requestId,
                                  long usedCU, long limitCU, Instant resetAt) {
        super(message, 402, errorCode, requestId);
        this.usedCU = usedCU;
        this.limitCU = limitCU;
        this.resetAt = resetAt;
    }

    /** Returns the number of compute units used. */
    public long usedCU() {
        return usedCU;
    }

    /** Returns the compute unit limit. */
    public long limitCU() {
        return limitCU;
    }

    /** Returns when the quota resets. */
    public Instant resetAt() {
        return resetAt;
    }
}
