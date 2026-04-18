package com.cryptachain.sdk.resource;

import com.cryptachain.sdk.http.HttpClient;
import com.cryptachain.sdk.model.ChainStatus;
import com.cryptachain.sdk.model.SystemStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Resource for system health and status operations.
 */
public final class HealthResource {

    private final HttpClient httpClient;

    /**
     * Creates a new HealthResource.
     *
     * @param httpClient the HTTP client
     */
    public HealthResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Gets the overall system status including service health.
     *
     * @return the system status
     */
    public SystemStatus getServiceStatus() {
        return httpClient.get("/v1/status/services", null, SystemStatus.class);
    }

    /**
     * Asynchronously gets the overall system status.
     *
     * @return a CompletableFuture with the system status
     */
    public CompletableFuture<SystemStatus> getServiceStatusAsync() {
        return httpClient.getAsync("/v1/status/services", null, SystemStatus.class);
    }

    /**
     * Gets the sync status for all supported chains.
     *
     * @return the list of chain statuses
     */
    public List<ChainStatus> getChainStatuses() {
        return List.of(httpClient.get("/v1/status/chains", null, ChainStatus[].class));
    }

    /**
     * Asynchronously gets the sync status for all supported chains.
     *
     * @return a CompletableFuture with the list of chain statuses
     */
    public CompletableFuture<List<ChainStatus>> getChainStatusesAsync() {
        return httpClient.getAsync("/v1/status/chains", null, ChainStatus[].class)
                .thenApply(List::of);
    }
}
