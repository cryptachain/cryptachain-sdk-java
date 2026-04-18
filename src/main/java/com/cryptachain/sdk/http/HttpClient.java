package com.cryptachain.sdk.http;

import com.cryptachain.sdk.*;
import com.cryptachain.sdk.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * HTTP client wrapper for the CryptaChain API.
 *
 * <p>Wraps {@link java.net.http.HttpClient} and provides methods for making authenticated
 * requests, parsing JSON responses, and mapping errors to typed exceptions.</p>
 */
public final class HttpClient {

    private final java.net.http.HttpClient httpClient;
    private final CryptaChainConfig config;
    private final ObjectMapper mapper;
    private final RetryHandler retryHandler;

    /**
     * Creates a new HttpClient with the given configuration.
     *
     * @param config the SDK configuration
     */
    public HttpClient(CryptaChainConfig config) {
        this.config = config;
        this.mapper = JsonUtils.mapper();
        this.retryHandler = new RetryHandler(config);
        this.httpClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(config.connectTimeout())
                .build();
    }

    /**
     * Performs a synchronous GET request and deserializes the response.
     *
     * @param <T>          the response type
     * @param path         the API path (e.g., "/v1/chains")
     * @param queryParams  query parameters (may be null or empty)
     * @param responseType the class to deserialize the response into
     * @return the deserialized response
     * @throws CryptaChainException on any error
     */
    public <T> T get(String path, Map<String, String> queryParams, Class<T> responseType) {
        return retryHandler.executeWithRetry(() -> doGet(path, queryParams, responseType));
    }

    /**
     * Performs an asynchronous GET request and deserializes the response.
     *
     * @param <T>          the response type
     * @param path         the API path
     * @param queryParams  query parameters (may be null or empty)
     * @param responseType the class to deserialize the response into
     * @return a CompletableFuture with the deserialized response
     */
    public <T> CompletableFuture<T> getAsync(String path, Map<String, String> queryParams, Class<T> responseType) {
        return retryHandler.executeWithRetryAsync(() -> doGetAsync(path, queryParams, responseType));
    }

    /**
     * Performs a synchronous POST request with a JSON body and deserializes the response.
     *
     * @param <T>          the response type
     * @param path         the API path
     * @param body         the request body object (will be serialized to JSON)
     * @param responseType the class to deserialize the response into
     * @return the deserialized response
     * @throws CryptaChainException on any error
     */
    public <T> T post(String path, Object body, Class<T> responseType) {
        return retryHandler.executeWithRetry(() -> doPost(path, body, responseType));
    }

    /**
     * Performs an asynchronous POST request with a JSON body.
     *
     * @param <T>          the response type
     * @param path         the API path
     * @param body         the request body object
     * @param responseType the class to deserialize the response into
     * @return a CompletableFuture with the deserialized response
     */
    public <T> CompletableFuture<T> postAsync(String path, Object body, Class<T> responseType) {
        return retryHandler.executeWithRetryAsync(() -> doPostAsync(path, body, responseType));
    }

    private <T> T doGet(String path, Map<String, String> queryParams, Class<T> responseType) {
        HttpRequest request = buildGetRequest(path, queryParams);
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return handleResponse(response, responseType);
        } catch (CryptaChainException e) {
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CryptaChainException("Request interrupted", e);
        } catch (IOException e) {
            throw new CryptaChainException("Request failed: " + e.getMessage(), e);
        }
    }

    private <T> CompletableFuture<T> doGetAsync(String path, Map<String, String> queryParams, Class<T> responseType) {
        HttpRequest request = buildGetRequest(path, queryParams);
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, responseType));
    }

    private <T> T doPost(String path, Object body, Class<T> responseType) {
        HttpRequest request = buildPostRequest(path, body);
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return handleResponse(response, responseType);
        } catch (CryptaChainException e) {
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CryptaChainException("Request interrupted", e);
        } catch (IOException e) {
            throw new CryptaChainException("Request failed: " + e.getMessage(), e);
        }
    }

    private <T> CompletableFuture<T> doPostAsync(String path, Object body, Class<T> responseType) {
        HttpRequest request = buildPostRequest(path, body);
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, responseType));
    }

    private HttpRequest buildGetRequest(String path, Map<String, String> queryParams) {
        String url = buildUrl(path, queryParams);
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("X-API-Key", config.apiKey())
                .header("Accept", "application/json")
                .header("User-Agent", "cryptachain-sdk-java/0.1.0")
                .timeout(config.requestTimeout())
                .build();
    }

    private HttpRequest buildPostRequest(String path, Object body) {
        String url = buildUrl(path, null);
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new CryptaChainException("Failed to serialize request body", e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("X-API-Key", config.apiKey())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("User-Agent", "cryptachain-sdk-java/0.1.0")
                .timeout(config.requestTimeout())
                .build();
    }

    private String buildUrl(String path, Map<String, String> queryParams) {
        StringBuilder sb = new StringBuilder(config.baseUrl());
        sb.append(path);
        if (queryParams != null && !queryParams.isEmpty()) {
            String query = queryParams.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "="
                            + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            if (!query.isEmpty()) {
                sb.append("?").append(query);
            }
        }
        return sb.toString();
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) {
        int status = response.statusCode();
        String body = response.body();
        String requestId = response.headers().firstValue("X-Request-Id").orElse(null);

        if (status >= 200 && status < 300) {
            try {
                return mapper.readValue(body, responseType);
            } catch (JsonProcessingException e) {
                throw new CryptaChainException("Failed to parse response: " + e.getMessage(), e);
            }
        }

        String errorMessage = "API error";
        String errorCode = null;
        try {
            JsonNode node = mapper.readTree(body);
            if (node.has("message")) {
                errorMessage = node.get("message").asText();
            }
            if (node.has("error")) {
                errorCode = node.get("error").asText();
            }
        } catch (JsonProcessingException ignored) {
            errorMessage = body != null ? body : "HTTP " + status;
        }

        switch (status) {
            case 401 -> throw new AuthenticationException(errorMessage, errorCode, requestId);
            case 402 -> {
                long usedCU = 0;
                long limitCU = 0;
                Instant resetAt = null;
                try {
                    JsonNode node = mapper.readTree(body);
                    if (node.has("used_cu")) usedCU = node.get("used_cu").asLong();
                    if (node.has("limit_cu")) limitCU = node.get("limit_cu").asLong();
                    if (node.has("reset_at")) resetAt = Instant.parse(node.get("reset_at").asText());
                } catch (Exception ignored) {}
                throw new QuotaExceededException(errorMessage, errorCode, requestId, usedCU, limitCU, resetAt);
            }
            case 404 -> throw new ChainNotFoundException(errorMessage, errorCode, requestId);
            case 429 -> {
                Duration retryAfter = RateLimitHandler.getRetryAfter(response);
                int remaining = RateLimitHandler.getRemaining(response).orElse(0);
                throw new RateLimitException(errorMessage, errorCode, requestId, retryAfter, remaining);
            }
            default -> throw new CryptaChainException(errorMessage, status, errorCode, requestId);
        }
    }
}
