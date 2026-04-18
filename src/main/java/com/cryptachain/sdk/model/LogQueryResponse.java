package com.cryptachain.sdk.model;

import java.util.List;

/**
 * Response containing event logs matching a query.
 *
 * @param logs       the list of matching logs
 * @param pagination the pagination metadata
 */
public record LogQueryResponse(
        List<Log> logs,
        Pagination pagination
) {}
