package com.cryptachain.sdk.model;

import java.util.List;

/**
 * Response from a batch price lookup.
 *
 * @param prices the list of price responses
 * @param total  the total number of prices returned
 */
public record PriceBatchResponse(
        List<PriceResponse> prices,
        int total
) {}
