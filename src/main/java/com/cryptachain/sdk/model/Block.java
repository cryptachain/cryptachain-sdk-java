package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a blockchain block.
 *
 * @param number           the block number
 * @param hash             the block hash
 * @param parentHash       the parent block hash
 * @param timestamp        the block timestamp (ISO 8601)
 * @param transactionCount the number of transactions in the block
 * @param gasUsed          the gas used
 * @param gasLimit         the gas limit
 * @param baseFeePerGas    the base fee per gas (EIP-1559)
 * @param miner            the miner/validator address
 * @param chain            the chain identifier
 */
public record Block(
        long number,
        String hash,
        @JsonProperty("parent_hash") String parentHash,
        String timestamp,
        @JsonProperty("transaction_count") int transactionCount,
        @JsonProperty("gas_used") String gasUsed,
        @JsonProperty("gas_limit") String gasLimit,
        @JsonProperty("base_fee_per_gas") String baseFeePerGas,
        String miner,
        String chain
) {}
