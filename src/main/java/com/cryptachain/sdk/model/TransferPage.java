package com.cryptachain.sdk.model;

import java.util.List;

/**
 * A single page of transfer results with pagination metadata.
 *
 * @param transfers  the list of transfers on this page
 * @param pagination the pagination metadata
 */
public record TransferPage(
        List<Transfer> transfers,
        Pagination pagination
) {}
