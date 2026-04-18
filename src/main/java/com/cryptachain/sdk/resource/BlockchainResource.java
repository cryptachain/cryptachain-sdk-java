package com.cryptachain.sdk.resource;

import com.cryptachain.sdk.http.HttpClient;
import com.cryptachain.sdk.model.Chain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Resource for blockchain-related operations.
 */
public final class BlockchainResource {

    private final HttpClient httpClient;

    /**
     * Creates a new BlockchainResource.
     *
     * @param httpClient the HTTP client
     */
    public BlockchainResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Lists all supported blockchains.
     *
     * @return the list of supported chains
     */
    public List<Chain> listChains() {
        return List.of(httpClient.get("/v1/chains", null, Chain[].class));
    }

    /**
     * Asynchronously lists all supported blockchains.
     *
     * @return a CompletableFuture with the list of supported chains
     */
    public CompletableFuture<List<Chain>> listChainsAsync() {
        return httpClient.getAsync("/v1/chains", null, Chain[].class)
                .thenApply(List::of);
    }
}
