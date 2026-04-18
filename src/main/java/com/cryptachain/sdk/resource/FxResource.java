package com.cryptachain.sdk.resource;

import com.cryptachain.sdk.http.HttpClient;
import com.cryptachain.sdk.model.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Resource for foreign exchange rate operations.
 *
 * <p>Supports single FX rates, daily history, IAS 21 monthly averages, and currency listing.</p>
 */
public final class FxResource {

    private final HttpClient httpClient;

    /**
     * Creates a new FxResource.
     *
     * @param httpClient the HTTP client
     */
    public FxResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Gets a single FX rate.
     *
     * @param from the source currency (e.g., "EUR")
     * @param to   the target currency (e.g., "USD")
     * @param date the rate date
     * @return the FX rate
     */
    public FxRate getRate(String from, String to, LocalDate date) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("to", to);
        if (date != null) params.put("date", date.toString());
        return httpClient.get("/v1/fx/rate", params, FxRate.class);
    }

    /**
     * Asynchronously gets a single FX rate.
     *
     * @param from the source currency
     * @param to   the target currency
     * @param date the rate date
     * @return a CompletableFuture with the FX rate
     */
    public CompletableFuture<FxRate> getRateAsync(String from, String to, LocalDate date) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("from", from);
        params.put("to", to);
        if (date != null) params.put("date", date.toString());
        return httpClient.getAsync("/v1/fx/rate", params, FxRate.class);
    }

    /**
     * Gets daily FX rate history for a currency.
     *
     * @param currency the currency code
     * @param from     the start date
     * @param to       the end date
     * @return the FX history response
     */
    public FxHistoryResponse getHistory(String currency, LocalDate from, LocalDate to) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("currency", currency);
        if (from != null) params.put("from", from.toString());
        if (to != null) params.put("to", to.toString());
        return httpClient.get("/v1/fx/history", params, FxHistoryResponse.class);
    }

    /**
     * Asynchronously gets daily FX rate history for a currency.
     *
     * @param currency the currency code
     * @param from     the start date
     * @param to       the end date
     * @return a CompletableFuture with the FX history
     */
    public CompletableFuture<FxHistoryResponse> getHistoryAsync(String currency, LocalDate from, LocalDate to) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("currency", currency);
        if (from != null) params.put("from", from.toString());
        if (to != null) params.put("to", to.toString());
        return httpClient.getAsync("/v1/fx/history", params, FxHistoryResponse.class);
    }

    /**
     * Gets the IAS 21 monthly average FX rate.
     *
     * @param currency the currency code
     * @param year     the year
     * @param month    the month (1-12)
     * @return the monthly average response
     */
    public FxMonthlyAverageResponse getMonthlyAverage(String currency, int year, int month) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("currency", currency);
        params.put("year", String.valueOf(year));
        params.put("month", String.valueOf(month));
        return httpClient.get("/v1/fx/monthly-average", params, FxMonthlyAverageResponse.class);
    }

    /**
     * Asynchronously gets the IAS 21 monthly average FX rate.
     *
     * @param currency the currency code
     * @param year     the year
     * @param month    the month (1-12)
     * @return a CompletableFuture with the monthly average
     */
    public CompletableFuture<FxMonthlyAverageResponse> getMonthlyAverageAsync(String currency, int year, int month) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("currency", currency);
        params.put("year", String.valueOf(year));
        params.put("month", String.valueOf(month));
        return httpClient.getAsync("/v1/fx/monthly-average", params, FxMonthlyAverageResponse.class);
    }

    /**
     * Lists all supported currencies.
     *
     * @return the currencies response
     */
    public CurrenciesResponse listCurrencies() {
        return httpClient.get("/v1/fx/currencies", null, CurrenciesResponse.class);
    }

    /**
     * Asynchronously lists all supported currencies.
     *
     * @return a CompletableFuture with the currencies response
     */
    public CompletableFuture<CurrenciesResponse> listCurrenciesAsync() {
        return httpClient.getAsync("/v1/fx/currencies", null, CurrenciesResponse.class);
    }
}
