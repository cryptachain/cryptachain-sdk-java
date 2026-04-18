package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents an event log emitted by a smart contract.
 *
 * @param address          the contract address that emitted the log
 * @param topics           the indexed topics
 * @param data             the non-indexed data
 * @param blockNumber      the block number
 * @param transactionHash  the transaction hash
 * @param logIndex         the log index within the block
 * @param transactionIndex the transaction index within the block
 * @param removed          whether the log was removed due to a chain reorganization
 */
public record Log(
        String address,
        List<String> topics,
        String data,
        @JsonProperty("block_number") long blockNumber,
        @JsonProperty("transaction_hash") String transactionHash,
        @JsonProperty("log_index") int logIndex,
        @JsonProperty("transaction_index") int transactionIndex,
        boolean removed
) {}
