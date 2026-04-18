package com.cryptachain.sdk.model;

/**
 * A screening flag indicating a risk factor for an address.
 *
 * @param type        the flag type (e.g., "OFAC", "DARKNET", "MIXER")
 * @param source      the source of the flag
 * @param description a human-readable description
 * @param severity    the severity level ("info", "warning", "critical")
 */
public record ScreeningFlag(
        String type,
        String source,
        String description,
        String severity
) {}
