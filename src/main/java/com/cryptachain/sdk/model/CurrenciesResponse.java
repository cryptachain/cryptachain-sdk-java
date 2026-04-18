package com.cryptachain.sdk.model;

import java.util.List;

/**
 * Response containing all supported currencies.
 *
 * @param currencies the list of supported currencies
 * @param count      the total number of supported currencies
 */
public record CurrenciesResponse(
        List<CurrencyInfo> currencies,
        int count
) {}
