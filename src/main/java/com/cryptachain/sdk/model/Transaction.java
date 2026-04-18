package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a blockchain transaction.
 *
 * @param hash             the transaction hash
 * @param blockNumber      the block number
 * @param timestamp        the transaction timestamp
 * @param from             the sender address
 * @param to               the recipient address
 * @param value            the value transferred (in wei/native units)
 * @param gas              the gas limit
 * @param gasPrice         the gas price
 * @param gasUsed          the gas used
 * @param nonce            the sender nonce
 * @param input            the transaction input data
 * @param status           the transaction status (1 = success, 0 = failed)
 * @param chain            the chain identifier
 * @param methodId         the method selector (first 4 bytes of input)
 * @param contractAddress  the created contract address (for contract creation txs)
 */
public record Transaction(
        String hash,
        @JsonProperty("block_number") long blockNumber,
        String timestamp,
        String from,
        String to,
        String value,
        String gas,
        @JsonProperty("gas_price") String gasPrice,
        @JsonProperty("gas_used") String gasUsed,
        long nonce,
        String input,
        int status,
        String chain,
        @JsonProperty("method_id") String methodId,
        @JsonProperty("contract_address") String contractAddress
) {}
