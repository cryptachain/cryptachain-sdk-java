package com.cryptachain.sdk;

import com.cryptachain.sdk.http.RetryHandler;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Which status codes the retry loop treats as transient.
 *
 * <p>Asserts on the <b>number of attempts</b>, not on the exception that comes out: the caller sees
 * the same {@link CryptaChainException} either way, so a test that only checked the thrown type
 * could not tell a single attempt from four. That is exactly how 501 spent {@code maxRetries}
 * rounds of exponential backoff unnoticed.
 *
 * <p>Lives in {@code com.cryptachain.sdk} rather than beside {@code RetryHandler} because
 * {@code CryptaChainConfig.Builder}'s constructor is package-private, and widening production
 * visibility to suit a test is the wrong trade.
 */
class RetryHandlerTest {

    private static RetryHandler handler(int maxRetries) {
        CryptaChainConfig config = new CryptaChainConfig.Builder()
                .apiKey("test-key")
                .maxRetries(maxRetries)
                .retryBaseDelay(Duration.ofMillis(1))
                .build();
        return new RetryHandler(config);
    }

    private static int attemptsFor(int status, int maxRetries) {
        RetryHandler h = handler(maxRetries);
        AtomicInteger attempts = new AtomicInteger();
        assertThrows(CryptaChainException.class, () -> h.executeWithRetry(() -> {
            attempts.incrementAndGet();
            throw new CryptaChainException("boom", status, null, null);
        }));
        return attempts.get();
    }

    @Test
    void notImplemented501_isTriedExactlyOnce() {
        // 501 is a 5xx, and the transience heuristic is `statusCode >= 500`, so this used to run
        // 1 + maxRetries times. CHAIN_NOT_SERVED does not become served by waiting, and neither
        // do the reserved /approvals and /defi-positions routes.
        assertEquals(1, attemptsFor(501, 3));
    }

    @Test
    void transient5xx_stillRetries() {
        // The positive control. Without it, a NON_RETRYABLE_CODES set that swallowed every 5xx
        // would satisfy the assertion above and silently disable retries altogether.
        assertEquals(4, attemptsFor(500, 3), "1 attempt + 3 retries");
        assertEquals(4, attemptsFor(502, 3));
        assertEquals(4, attemptsFor(503, 3));
        assertEquals(4, attemptsFor(504, 3));
    }

    @Test
    void permanentClientErrors_areTriedExactlyOnce() {
        assertEquals(1, attemptsFor(401, 3));
        assertEquals(1, attemptsFor(402, 3));
        assertEquals(1, attemptsFor(403, 3));
        assertEquals(1, attemptsFor(404, 3));
    }

    @Test
    void success_isNotRetried() {
        AtomicInteger attempts = new AtomicInteger();
        String r = handler(3).executeWithRetry(() -> {
            attempts.incrementAndGet();
            return "ok";
        });
        assertEquals("ok", r);
        assertEquals(1, attempts.get());
    }
}
