package com.cryptachain.sdk.model;

import java.util.List;

/**
 * Response containing daily FX rate history for a currency.
 *
 * @param currency the currency code
 * @param from     the start date of the range
 * @param to       the end date of the range
 * @param rates    the list of daily FX rates
 */
public record FxHistoryResponse(
        String currency,
        String from,
        String to,
        List<FxRate> rates
) {}
