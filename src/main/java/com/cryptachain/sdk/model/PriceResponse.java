package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Price response following the v2.3 P11 shape.
 *
 * <p>All numeric values are represented as strings to preserve precision.
 * Consumers should parse them as {@link java.math.BigDecimal} as needed.</p>
 *
 * @param symbol             the asset symbol
 * @param date               the price date (YYYY-MM-DD)
 * @param currency           the target currency
 * @param price              the price in the target currency
 * @param priceUsd           the price in USD
 * @param fxRate             the FX rate used for conversion
 * @param fxSource           the FX rate source
 * @param fxDate             the FX rate date
 * @param source             the price data source
 * @param qualityScore       the quality score (0-100)
 * @param fairValueLevel     the IFRS fair value hierarchy level (1, 2, or 3)
 * @param sourceCount        the number of price sources aggregated
 * @param methodologyVersion the pricing methodology version
 * @param stablecoinPegged   whether the asset is a stablecoin pegged to a fiat currency
 * @param manualOverride     whether the price was manually overridden
 * @param computedAt         the timestamp when the price was computed
 */
public record PriceResponse(
        String symbol,
        String date,
        String currency,
        String price,
        @JsonProperty("price_usd") String priceUsd,
        @JsonProperty("fx_rate") String fxRate,
        @JsonProperty("fx_source") String fxSource,
        @JsonProperty("fx_date") String fxDate,
        String source,
        @JsonProperty("quality_score") int qualityScore,
        @JsonProperty("fair_value_level") String fairValueLevel,
        @JsonProperty("source_count") int sourceCount,
        @JsonProperty("methodology_version") String methodologyVersion,
        @JsonProperty("stablecoin_pegged") boolean stablecoinPegged,
        @JsonProperty("manual_override") boolean manualOverride,
        @JsonProperty("computed_at") String computedAt
) {}
