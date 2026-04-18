package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Native token balance for a wallet address.
 *
 * @param balance          the raw balance
 * @param balanceFormatted the human-readable formatted balance
 * @param symbol           the native token symbol (e.g., "ETH")
 * @param priceUsd         the current price in USD
 * @param valueUsd         the total value in USD
 * @param chain            the chain identifier
 * @param address          the wallet address
 */
public record NativeBalance(
        String balance,
        @JsonProperty("balance_formatted") String balanceFormatted,
        String symbol,
        @JsonProperty("price_usd") String priceUsd,
        @JsonProperty("value_usd") String valueUsd,
        String chain,
        String address
) {}
