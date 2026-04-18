package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an internal transaction trace.
 *
 * @param transactionHash the parent transaction hash
 * @param traceAddress    the trace address path
 * @param from            the sender address
 * @param to              the recipient address
 * @param value           the value transferred
 * @param callType        the call type ("call", "delegatecall", "staticcall", "create")
 * @param gas             the gas provided
 * @param gasUsed         the gas used
 * @param input           the input data
 * @param output          the output data
 * @param error           the error message (null if successful)
 */
public record Trace(
        @JsonProperty("transaction_hash") String transactionHash,
        @JsonProperty("trace_address") String traceAddress,
        String from,
        String to,
        String value,
        @JsonProperty("call_type") String callType,
        String gas,
        @JsonProperty("gas_used") String gasUsed,
        String input,
        String output,
        String error
) {}
