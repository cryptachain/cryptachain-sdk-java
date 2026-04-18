package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Information about a supported fiat currency.
 *
 * @param code        the ISO 4217 currency code
 * @param name        the currency name
 * @param source      the FX rate source
 * @param peggedToUsd whether the currency is pegged to USD
 * @param pegRatio    the peg ratio (null if not pegged)
 */
public record CurrencyInfo(
        String code,
        String name,
        String source,
        @JsonProperty("pegged_to_usd") boolean peggedToUsd,
        @JsonProperty("peg_ratio") String pegRatio
) {}
