package com.cryptachain.sdk.http;

import com.cryptachain.sdk.CryptaChainConfig;
import com.cryptachain.sdk.CryptaChainException;
import com.cryptachain.sdk.RateLimitException;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Handles retry logic with exponential backoff for failed HTTP requests.
 *
 * <p>Retries are performed for server errors (5xx) and rate-limit responses (429).
 * Authentication (401), quota (402), forbidden (403), not-found (404) and
 * not-implemented (501) errors are never retried.</p>
 */
public final class RetryHandler {

    /**
     * Status codes that are permanent for the request as sent — retrying cannot change the answer.
     *
     * <p><b>501 is here despite being a 5xx.</b> The {@code >= 500} rule below is a heuristic about
     * <em>transience</em>: 500/502/503/504 mean the server failed or was momentarily unable, so the
     * same request may succeed later. 501 Not Implemented means the server does not implement the
     * functionality at all — for CryptaChain, {@code CHAIN_NOT_SERVED} on a chain we advertise but
     * do not index, and the reserved {@code /approvals} and {@code /defi-positions} routes. None of
     * those become true by waiting. Retrying them burned {@code maxRetries} attempts on exponential
     * backoff (capped at 30s), which delayed the caller's own failure handling by up to a minute
     * and, worse, presented a permanent structural refusal as a transient outage.</p>
     */
    private static final Set<Integer> NON_RETRYABLE_CODES = Set.of(401, 402, 403, 404, 501);

    private final int maxRetries;
    private final Duration baseDelay;
    private final boolean autoRetryRateLimit;

    /**
     * Creates a RetryHandler from the given configuration.
     *
     * @param config the SDK configuration
     */
    public RetryHandler(CryptaChainConfig config) {
        this.maxRetries = config.maxRetries();
        this.baseDelay = config.retryBaseDelay();
        this.autoRetryRateLimit = config.autoRetryRateLimit();
    }

    /**
     * Executes the given operation with retry logic.
     *
     * @param <T>       the result type
     * @param operation the operation to execute
     * @return the result
     * @throws CryptaChainException if all retries are exhausted
     */
    public <T> T executeWithRetry(Supplier<T> operation) {
        CryptaChainException lastException = null;
        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                return operation.get();
            } catch (CryptaChainException e) {
                lastException = e;
                if (!shouldRetry(e, attempt)) {
                    throw e;
                }
                Duration delay = computeDelay(e, attempt);
                sleep(delay);
            }
        }
        throw lastException;
    }

    /**
     * Executes the given async operation with retry logic.
     *
     * @param <T>       the result type
     * @param operation the async operation to execute
     * @return a CompletableFuture with the result
     */
    public <T> CompletableFuture<T> executeWithRetryAsync(Supplier<CompletableFuture<T>> operation) {
        return executeWithRetryAsync(operation, 0);
    }

    private <T> CompletableFuture<T> executeWithRetryAsync(Supplier<CompletableFuture<T>> operation, int attempt) {
        return operation.get().exceptionallyCompose(throwable -> {
            Throwable cause = throwable instanceof java.util.concurrent.CompletionException
                    ? throwable.getCause() : throwable;
            if (cause instanceof CryptaChainException e && shouldRetry(e, attempt)) {
                Duration delay = computeDelay(e, attempt);
                return CompletableFuture.supplyAsync(() -> null,
                                CompletableFuture.delayedExecutor(delay.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS))
                        .thenCompose(ignored -> executeWithRetryAsync(operation, attempt + 1));
            }
            return CompletableFuture.failedFuture(cause);
        });
    }

    private boolean shouldRetry(CryptaChainException e, int attempt) {
        if (attempt >= maxRetries) {
            return false;
        }
        if (NON_RETRYABLE_CODES.contains(e.statusCode())) {
            return false;
        }
        if (e instanceof RateLimitException) {
            return autoRetryRateLimit;
        }
        return e.statusCode() >= 500 || e.statusCode() == 0;
    }

    private Duration computeDelay(CryptaChainException e, int attempt) {
        if (e instanceof RateLimitException rle && rle.retryAfter() != null) {
            return rle.retryAfter();
        }
        long delayMs = baseDelay.toMillis() * (1L << attempt);
        return Duration.ofMillis(Math.min(delayMs, 30_000));
    }

    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new CryptaChainException("Request interrupted during retry backoff", ie);
        }
    }
}
