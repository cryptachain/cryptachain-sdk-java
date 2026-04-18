package com.cryptachain.sdk.http;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Extracts rate-limit information from HTTP response headers.
 *
 * <p>Parses standard rate-limit headers:</p>
 * <ul>
 *   <li>{@code Retry-After} — seconds to wait before retrying</li>
 *   <li>{@code X-RateLimit-Remaining} — remaining requests in current window</li>
 *   <li>{@code X-RateLimit-Reset} — Unix timestamp when the window resets</li>
 * </ul>
 */
public final class RateLimitHandler {

    private RateLimitHandler() {}

    /**
     * Extracts the Retry-After duration from the response headers.
     *
     * @param response the HTTP response
     * @return the retry-after duration, or a default of 1 second if not present
     */
    public static Duration getRetryAfter(HttpResponse<?> response) {
        return response.headers().firstValue("Retry-After")
                .map(value -> {
                    try {
                        long seconds = Long.parseLong(value);
                        return Duration.ofSeconds(seconds);
                    } catch (NumberFormatException e) {
                        return Duration.ofSeconds(1);
                    }
                })
                .orElse(Duration.ofSeconds(1));
    }

    /**
     * Extracts the remaining request count from the response headers.
     *
     * @param response the HTTP response
     * @return the remaining count, or empty if not present
     */
    public static OptionalInt getRemaining(HttpResponse<?> response) {
        return response.headers().firstValue("X-RateLimit-Remaining")
                .map(value -> {
                    try {
                        return OptionalInt.of(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        return OptionalInt.empty();
                    }
                })
                .orElse(OptionalInt.empty());
    }

    /**
     * Extracts the rate-limit reset timestamp from the response headers.
     *
     * @param response the HTTP response
     * @return the reset timestamp in epoch seconds, or empty if not present
     */
    public static OptionalLong getResetTimestamp(HttpResponse<?> response) {
        return response.headers().firstValue("X-RateLimit-Reset")
                .map(value -> {
                    try {
                        return OptionalLong.of(Long.parseLong(value));
                    } catch (NumberFormatException e) {
                        return OptionalLong.empty();
                    }
                })
                .orElse(OptionalLong.empty());
    }
}
