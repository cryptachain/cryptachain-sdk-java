package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request body for batch price lookups.
 *
 * @param requests the list of individual price requests (max 500)
 * @param currency the target currency for all prices
 */
public record PriceBatchRequest(
        List<PriceItem> requests,
        String currency
) {
    /**
     * A single price request item within a batch.
     *
     * @param symbol          the asset symbol (for symbol-based lookup)
     * @param chainId         the chain ID (for contract-based lookup)
     * @param contractAddress the contract address (for contract-based lookup)
     * @param date            the price date (YYYY-MM-DD)
     */
    public record PriceItem(
            String symbol,
            @JsonProperty("chain_id") Integer chainId,
            @JsonProperty("contract_address") String contractAddress,
            String date
    ) {}
}
