package com.cryptachain.sdk.model;

import java.util.List;

/**
 * Response from a bulk address screening request.
 *
 * @param results the list of screening results
 * @param total   the total number of addresses screened
 */
public record BulkScreeningResponse(
        List<ScreeningResult> results,
        int total
) {}
