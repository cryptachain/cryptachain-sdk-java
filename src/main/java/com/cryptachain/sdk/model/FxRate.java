package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A foreign exchange rate for a single date.
 *
 * @param currency      the currency code
 * @param date          the rate date (YYYY-MM-DD)
 * @param rate          the exchange rate (as string for precision)
 * @param source        the rate source (e.g., "ECB", "FRED")
 * @param isBusinessDay whether the date is a business day
 * @param ratedate      the actual date the rate was published
 */
public record FxRate(
        String currency,
        String date,
        String rate,
        String source,
        @JsonProperty("is_business_day") boolean isBusinessDay,
        String ratedate
) {}
