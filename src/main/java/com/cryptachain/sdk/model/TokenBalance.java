package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a token balance for a wallet address.
 *
 * @param symbol          the token symbol
 * @param name            the token name
 * @param contractAddress the token contract address
 * @param balance         the raw balance
 * @param balanceFormatted the human-readable formatted balance
 * @param decimals        the token decimals
 * @param priceUsd        the current price in USD
 * @param valueUsd        the total value in USD
 * @param chain           the chain identifier
 */
public record TokenBalance(
        String symbol,
        String name,
        @JsonProperty("contract_address") String contractAddress,
        String balance,
        @JsonProperty("balance_formatted") String balanceFormatted,
        int decimals,
        @JsonProperty("price_usd") String priceUsd,
        @JsonProperty("value_usd") String valueUsd,
        String chain
) {}
