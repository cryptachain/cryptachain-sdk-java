package com.cryptachain.sdk.resource;

import com.cryptachain.sdk.http.HttpClient;
import com.cryptachain.sdk.model.BulkScreeningResponse;
import com.cryptachain.sdk.model.ScreeningResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Resource for address screening operations.
 *
 * <p>Provides sanctions screening, risk scoring, and label lookups for blockchain addresses.</p>
 */
public final class ScreeningResource {

    private final HttpClient httpClient;

    /**
     * Creates a new ScreeningResource.
     *
     * @param httpClient the HTTP client
     */
    public ScreeningResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Screens a single address for sanctions and risk.
     *
     * @param address the blockchain address to screen
     * @param chain   the chain identifier
     * @return the screening result
     */
    public ScreeningResult screenAddress(String address, String chain) {
        Map<String, String> params = Map.of("address", address, "chain", chain);
        return httpClient.get("/api/v1/screen/address", params, ScreeningResult.class);
    }

    /**
     * Asynchronously screens a single address.
     *
     * @param address the blockchain address
     * @param chain   the chain identifier
     * @return a CompletableFuture with the screening result
     */
    public CompletableFuture<ScreeningResult> screenAddressAsync(String address, String chain) {
        Map<String, String> params = Map.of("address", address, "chain", chain);
        return httpClient.getAsync("/api/v1/screen/address", params, ScreeningResult.class);
    }

    /**
     * Performs bulk address screening.
     *
     * @param addresses the list of addresses to screen
     * @param chain     the chain identifier
     * @return the bulk screening response
     */
    public BulkScreeningResponse screenBulk(List<String> addresses, String chain) {
        Map<String, Object> body = Map.of("addresses", addresses, "chain", chain);
        return httpClient.post("/api/v1/screen/bulk", body, BulkScreeningResponse.class);
    }

    /**
     * Asynchronously performs bulk address screening.
     *
     * @param addresses the list of addresses
     * @param chain     the chain identifier
     * @return a CompletableFuture with the bulk screening response
     */
    public CompletableFuture<BulkScreeningResponse> screenBulkAsync(List<String> addresses, String chain) {
        Map<String, Object> body = Map.of("addresses", addresses, "chain", chain);
        return httpClient.postAsync("/api/v1/screen/bulk", body, BulkScreeningResponse.class);
    }
}
