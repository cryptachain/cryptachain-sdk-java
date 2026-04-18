package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Metadata for a token.
 *
 * @param symbol      the token symbol
 * @param name        the token name
 * @param decimals    the number of decimals
 * @param logoUrl     the URL to the token logo
 * @param contracts   the list of contract deployments across chains
 * @param coingeckoId the CoinGecko identifier
 * @param category    the token category (e.g., "stablecoin", "defi")
 */
public record TokenMetadata(
        String symbol,
        String name,
        int decimals,
        @JsonProperty("logo_url") String logoUrl,
        List<TokenContract> contracts,
        @JsonProperty("coingecko_id") String coingeckoId,
        String category
) {
    /**
     * A token contract deployment on a specific chain.
     *
     * @param chain           the chain identifier
     * @param contractAddress the contract address
     * @param decimals        the decimals on this chain
     */
    public record TokenContract(
            String chain,
            @JsonProperty("contract_address") String contractAddress,
            int decimals
    ) {}
}
