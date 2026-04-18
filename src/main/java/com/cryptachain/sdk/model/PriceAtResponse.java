package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response for a point-in-time price lookup with 1-minute precision.
 *
 * @param symbol    the asset symbol
 * @param timestamp the exact timestamp
 * @param currency  the target currency
 * @param price     the price at the given timestamp
 * @param priceUsd  the price in USD
 * @param source    the data source
 */
public record PriceAtResponse(
        String symbol,
        String timestamp,
        String currency,
        String price,
        @JsonProperty("price_usd") String priceUsd,
        String source
) {}
