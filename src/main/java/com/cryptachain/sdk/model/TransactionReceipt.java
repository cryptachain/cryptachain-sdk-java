package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a transaction receipt with logs.
 *
 * @param transactionHash  the transaction hash
 * @param blockNumber      the block number
 * @param status           the status (1 = success, 0 = failed)
 * @param gasUsed          the gas used
 * @param cumulativeGasUsed the cumulative gas used in the block
 * @param contractAddress  the created contract address (if applicable)
 * @param logs             the event logs emitted
 * @param chain            the chain identifier
 */
public record TransactionReceipt(
        @JsonProperty("transaction_hash") String transactionHash,
        @JsonProperty("block_number") long blockNumber,
        int status,
        @JsonProperty("gas_used") String gasUsed,
        @JsonProperty("cumulative_gas_used") String cumulativeGasUsed,
        @JsonProperty("contract_address") String contractAddress,
        List<Log> logs,
        String chain
) {}
