package com.cryptachain.sdk.resource;

import com.cryptachain.sdk.http.HttpClient;
import com.cryptachain.sdk.model.TokenMetadata;

import java.util.concurrent.CompletableFuture;

/**
 * Resource for token metadata operations.
 */
public final class TokenResource {

    private final HttpClient httpClient;

    /**
     * Creates a new TokenResource.
     *
     * @param httpClient the HTTP client
     */
    public TokenResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Gets metadata for a token by symbol.
     *
     * @param symbol the token symbol (e.g., "BTC", "ETH", "USDC")
     * @return the token metadata
     */
    public TokenMetadata getBySymbol(String symbol) {
        return httpClient.get("/v1/tokens/" + symbol, null, TokenMetadata.class);
    }

    /**
     * Asynchronously gets metadata for a token by symbol.
     *
     * @param symbol the token symbol
     * @return a CompletableFuture with the token metadata
     */
    public CompletableFuture<TokenMetadata> getBySymbolAsync(String symbol) {
        return httpClient.getAsync("/v1/tokens/" + symbol, null, TokenMetadata.class);
    }
}
