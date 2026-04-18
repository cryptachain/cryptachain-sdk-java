package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response containing the IAS 21 monthly average FX rate.
 *
 * @param currency     the currency code
 * @param year         the year
 * @param month        the month (1-12)
 * @param averageRate  the monthly average rate (as string for precision)
 * @param daysInMonth  the total days in the month
 * @param businessDays the number of business days in the month
 * @param source       the rate source
 * @param computedAt   the timestamp when the average was computed
 */
public record FxMonthlyAverageResponse(
        String currency,
        int year,
        int month,
        @JsonProperty("average_rate") String averageRate,
        @JsonProperty("days_in_month") int daysInMonth,
        @JsonProperty("business_days") int businessDays,
        String source,
        @JsonProperty("computed_at") String computedAt
) {}
