package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Overall system status including individual service health.
 *
 * @param status    the overall system status ("healthy", "degraded", "down")
 * @param version   the API version
 * @param uptime    the uptime in seconds
 * @param services  the list of individual service statuses
 * @param timestamp the status check timestamp
 */
public record SystemStatus(
        String status,
        String version,
        long uptime,
        List<ServiceStatus> services,
        String timestamp
) {
    /**
     * Status of an individual service.
     *
     * @param name    the service name
     * @param status  the service status
     * @param latency the latency in milliseconds
     */
    public record ServiceStatus(
            String name,
            String status,
            @JsonProperty("latency_ms") long latency
    ) {}
}
