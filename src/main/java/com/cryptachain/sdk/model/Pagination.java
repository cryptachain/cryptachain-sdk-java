package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Pagination metadata for paginated API responses.
 *
 * @param cursor     the cursor for the next page (null if no more pages)
 * @param pageSize   the number of items per page
 * @param hasMore    whether more pages are available
 * @param totalCount the total number of items (may be null if not computed)
 */
public record Pagination(
        String cursor,
        @JsonProperty("page_size") int pageSize,
        @JsonProperty("has_more") boolean hasMore,
        @JsonProperty("total_count") Long totalCount
) {}
