package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes the pricing methodology used by the CryptaChain API.
 *
 * @param version         the methodology version
 * @param description     a human-readable description
 * @param sources         the data sources used
 * @param qualityScoring  how quality scores are computed
 * @param fairValueLevels the IFRS fair value hierarchy level definitions
 * @param updatedAt       when the methodology was last updated
 */
public record Methodology(
        String version,
        String description,
        String sources,
        @JsonProperty("quality_scoring") String qualityScoring,
        @JsonProperty("fair_value_levels") String fairValueLevels,
        @JsonProperty("updated_at") String updatedAt
) {}
