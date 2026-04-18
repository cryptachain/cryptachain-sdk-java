package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Result of an address screening check.
 *
 * @param address         the screened address
 * @param chain           the chain identifier
 * @param sanctionsMatch  whether the address matches a sanctions list
 * @param riskScore       the risk score (0-100)
 * @param riskLevel       the risk level ("low", "medium", "high", "severe")
 * @param flags           the list of screening flags
 * @param labels          the known labels for this address
 * @param screenedAt      the timestamp of the screening
 */
public record ScreeningResult(
        String address,
        String chain,
        @JsonProperty("sanctions_match") boolean sanctionsMatch,
        @JsonProperty("risk_score") int riskScore,
        @JsonProperty("risk_level") String riskLevel,
        List<ScreeningFlag> flags,
        List<String> labels,
        @JsonProperty("screened_at") String screenedAt
) {}
