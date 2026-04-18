package com.cryptachain.sdk.resource;

import com.cryptachain.sdk.http.HttpClient;
import com.cryptachain.sdk.model.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Resource for price-related API operations.
 *
 * <p>Supports symbol-based, contract-based, batch, and point-in-time price lookups
 * following the v2.3 P11 response format.</p>
 */
public final class PriceResource {

    private final HttpClient httpClient;

    /**
     * Creates a new PriceResource.
     *
     * @param httpClient the HTTP client
     */
    public PriceResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Gets a price by token symbol.
     *
     * @param symbol   the token symbol (e.g., "BTC", "ETH")
     * @param date     the price date
     * @param currency the target currency (e.g., "USD", "EUR")
     * @return the price response
     */
    public PriceResponse bySymbol(String symbol, LocalDate date, String currency) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (date != null) params.put("date", date.toString());
        if (currency != null) params.put("currency", currency);
        return httpClient.get("/v1/prices/by-symbol", params, PriceResponse.class);
    }

    /**
     * Asynchronously gets a price by token symbol.
     *
     * @param symbol   the token symbol
     * @param date     the price date
     * @param currency the target currency
     * @return a CompletableFuture with the price response
     */
    public CompletableFuture<PriceResponse> bySymbolAsync(String symbol, LocalDate date, String currency) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (date != null) params.put("date", date.toString());
        if (currency != null) params.put("currency", currency);
        return httpClient.getAsync("/v1/prices/by-symbol", params, PriceResponse.class);
    }

    /**
     * Gets a price by contract address.
     *
     * @param chainId         the EVM chain ID
     * @param contractAddress the token contract address
     * @param date            the price date
     * @param currency        the target currency
     * @return the price response
     */
    public PriceResponse byContract(int chainId, String contractAddress, LocalDate date, String currency) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("chain_id", String.valueOf(chainId));
        params.put("address", contractAddress);
        if (date != null) params.put("date", date.toString());
        if (currency != null) params.put("currency", currency);
        return httpClient.get("/v1/prices/by-contract", params, PriceResponse.class);
    }

    /**
     * Asynchronously gets a price by contract address.
     *
     * @param chainId         the EVM chain ID
     * @param contractAddress the token contract address
     * @param date            the price date
     * @param currency        the target currency
     * @return a CompletableFuture with the price response
     */
    public CompletableFuture<PriceResponse> byContractAsync(int chainId, String contractAddress,
                                                             LocalDate date, String currency) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("chain_id", String.valueOf(chainId));
        params.put("address", contractAddress);
        if (date != null) params.put("date", date.toString());
        if (currency != null) params.put("currency", currency);
        return httpClient.getAsync("/v1/prices/by-contract", params, PriceResponse.class);
    }

    /**
     * Performs a batch price lookup (max 500 items).
     *
     * @param request the batch price request
     * @return the batch price response
     */
    public PriceBatchResponse batch(PriceBatchRequest request) {
        return httpClient.post("/v1/prices/batch", request, PriceBatchResponse.class);
    }

    /**
     * Asynchronously performs a batch price lookup.
     *
     * @param request the batch price request
     * @return a CompletableFuture with the batch price response
     */
    public CompletableFuture<PriceBatchResponse> batchAsync(PriceBatchRequest request) {
        return httpClient.postAsync("/v1/prices/batch", request, PriceBatchResponse.class);
    }

    /**
     * Gets a point-in-time price with 1-minute precision via ClickHouse.
     *
     * @param symbol    the token symbol
     * @param timestamp the Unix timestamp (seconds)
     * @param currency  the target currency
     * @return the point-in-time price response
     */
    public PriceAtResponse at(String symbol, long timestamp, String currency) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        params.put("timestamp", String.valueOf(timestamp));
        if (currency != null) params.put("currency", currency);
        return httpClient.get("/v1/prices/at", params, PriceAtResponse.class);
    }

    /**
     * Asynchronously gets a point-in-time price.
     *
     * @param symbol    the token symbol
     * @param timestamp the Unix timestamp (seconds)
     * @param currency  the target currency
     * @return a CompletableFuture with the price response
     */
    public CompletableFuture<PriceAtResponse> atAsync(String symbol, long timestamp, String currency) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        params.put("timestamp", String.valueOf(timestamp));
        if (currency != null) params.put("currency", currency);
        return httpClient.getAsync("/v1/prices/at", params, PriceAtResponse.class);
    }

    /**
     * Gets the pricing methodology documentation.
     *
     * @return the methodology description
     */
    public Methodology getMethodology() {
        return httpClient.get("/v1/methodology", null, Methodology.class);
    }

    /**
     * Asynchronously gets the pricing methodology documentation.
     *
     * @return a CompletableFuture with the methodology
     */
    public CompletableFuture<Methodology> getMethodologyAsync() {
        return httpClient.getAsync("/v1/methodology", null, Methodology.class);
    }
}
