package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single wallet transfer (transaction).
 *
 * @param hash            the transaction hash
 * @param blockNumber     the block number
 * @param timestamp       the transaction timestamp (ISO 8601)
 * @param from            the sender address
 * @param to              the recipient address
 * @param value           the raw value transferred
 * @param valueFormatted  the human-readable formatted value
 * @param asset           the asset involved in the transfer
 * @param direction       the transfer direction ("in", "out", "self")
 * @param transferType    the transfer type ("native", "erc20", "nft", "internal")
 * @param valueUsdAtTime  the USD value at the time of transfer
 * @param fee             the transaction fee
 * @param feeUsd          the transaction fee in USD
 * @param status          the transaction status ("success", "failed")
 * @param chain           the chain identifier
 */
public record Transfer(
        String hash,
        @JsonProperty("block_number") long blockNumber,
        String timestamp,
        String from,
        String to,
        String value,
        @JsonProperty("value_formatted") String valueFormatted,
        TransferAsset asset,
        String direction,
        @JsonProperty("transfer_type") String transferType,
        @JsonProperty("value_usd_at_time") String valueUsdAtTime,
        String fee,
        @JsonProperty("fee_usd") String feeUsd,
        String status,
        String chain
) {
    /**
     * Nested record representing the asset involved in a transfer.
     *
     * @param symbol          the asset symbol (e.g., "ETH", "USDC")
     * @param name            the asset name
     * @param contractAddress the contract address (null for native assets)
     * @param decimals        the number of decimals
     * @param tokenId         the NFT token ID (null for fungible transfers)
     */
    public record TransferAsset(
            String symbol,
            String name,
            @JsonProperty("contract_address") String contractAddress,
            int decimals,
            @JsonProperty("token_id") String tokenId
    ) {}
}
