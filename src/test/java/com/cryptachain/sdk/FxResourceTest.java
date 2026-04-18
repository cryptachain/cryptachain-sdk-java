package com.cryptachain.sdk;

import com.cryptachain.sdk.model.*;
import com.cryptachain.sdk.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FX-related model deserialization.
 */
class FxResourceTest {

    private final ObjectMapper mapper = JsonUtils.mapper();

    @Test
    void deserializeFxRate() throws Exception {
        String json = """
                {
                    "currency": "EUR",
                    "date": "2026-04-04",
                    "rate": "0.9191",
                    "source": "ECB",
                    "is_business_day": true,
                    "ratedate": "2026-04-04"
                }
                """;

        FxRate rate = mapper.readValue(json, FxRate.class);
        assertEquals("EUR", rate.currency());
        assertEquals("0.9191", rate.rate());
        assertEquals("ECB", rate.source());
        assertTrue(rate.isBusinessDay());
        assertEquals("2026-04-04", rate.ratedate());
    }

    @Test
    void deserializeFxHistoryResponse() throws Exception {
        String json = """
                {
                    "currency": "EUR",
                    "from": "2026-04-01",
                    "to": "2026-04-04",
                    "rates": [
                        {
                            "currency": "EUR",
                            "date": "2026-04-01",
                            "rate": "0.9200",
                            "source": "ECB",
                            "is_business_day": true,
                            "ratedate": "2026-04-01"
                        },
                        {
                            "currency": "EUR",
                            "date": "2026-04-02",
                            "rate": "0.9180",
                            "source": "ECB",
                            "is_business_day": true,
                            "ratedate": "2026-04-02"
                        }
                    ]
                }
                """;

        FxHistoryResponse resp = mapper.readValue(json, FxHistoryResponse.class);
        assertEquals("EUR", resp.currency());
        assertEquals("2026-04-01", resp.from());
        assertEquals("2026-04-04", resp.to());
        assertEquals(2, resp.rates().size());
        assertEquals("0.9200", resp.rates().get(0).rate());
    }

    @Test
    void deserializeFxMonthlyAverageResponse() throws Exception {
        String json = """
                {
                    "currency": "EUR",
                    "year": 2026,
                    "month": 4,
                    "average_rate": "0.9190",
                    "days_in_month": 30,
                    "business_days": 22,
                    "source": "ECB",
                    "computed_at": "2026-04-05T00:00:00Z"
                }
                """;

        FxMonthlyAverageResponse resp = mapper.readValue(json, FxMonthlyAverageResponse.class);
        assertEquals("EUR", resp.currency());
        assertEquals(2026, resp.year());
        assertEquals(4, resp.month());
        assertEquals("0.9190", resp.averageRate());
        assertEquals(30, resp.daysInMonth());
        assertEquals(22, resp.businessDays());
        assertEquals("ECB", resp.source());
    }

    @Test
    void deserializeCurrenciesResponse() throws Exception {
        String json = """
                {
                    "currencies": [
                        {
                            "code": "EUR",
                            "name": "Euro",
                            "source": "ECB",
                            "pegged_to_usd": false,
                            "peg_ratio": null
                        },
                        {
                            "code": "AED",
                            "name": "UAE Dirham",
                            "source": "ECB",
                            "pegged_to_usd": true,
                            "peg_ratio": "3.6725"
                        }
                    ],
                    "count": 2
                }
                """;

        CurrenciesResponse resp = mapper.readValue(json, CurrenciesResponse.class);
        assertEquals(2, resp.count());
        assertEquals(2, resp.currencies().size());

        CurrencyInfo eur = resp.currencies().get(0);
        assertEquals("EUR", eur.code());
        assertEquals("Euro", eur.name());
        assertFalse(eur.peggedToUsd());
        assertNull(eur.pegRatio());

        CurrencyInfo aed = resp.currencies().get(1);
        assertEquals("AED", aed.code());
        assertTrue(aed.peggedToUsd());
        assertEquals("3.6725", aed.pegRatio());
    }

    @Test
    void deserializeChainStatus() throws Exception {
        String json = """
                {
                    "chain": "ethereum",
                    "chain_id": 1,
                    "name": "Ethereum",
                    "status": "synced",
                    "latest_block": 19500000,
                    "head_block": 19500005,
                    "blocks_behind": 5,
                    "last_synced_at": "2026-04-04T12:00:00Z"
                }
                """;

        ChainStatus cs = mapper.readValue(json, ChainStatus.class);
        assertEquals("ethereum", cs.chain());
        assertEquals(1, cs.chainId());
        assertEquals("synced", cs.status());
        assertEquals(19500000L, cs.latestBlock());
        assertEquals(5L, cs.blocksBehind());
    }

    @Test
    void deserializeSystemStatus() throws Exception {
        String json = """
                {
                    "status": "healthy",
                    "version": "2.3.0",
                    "uptime": 86400,
                    "services": [
                        {
                            "name": "postgres",
                            "status": "healthy",
                            "latency_ms": 2
                        },
                        {
                            "name": "clickhouse",
                            "status": "healthy",
                            "latency_ms": 5
                        }
                    ],
                    "timestamp": "2026-04-04T12:00:00Z"
                }
                """;

        SystemStatus ss = mapper.readValue(json, SystemStatus.class);
        assertEquals("healthy", ss.status());
        assertEquals("2.3.0", ss.version());
        assertEquals(86400L, ss.uptime());
        assertEquals(2, ss.services().size());
        assertEquals("postgres", ss.services().get(0).name());
        assertEquals(2L, ss.services().get(0).latency());
    }

    @Test
    void unknownFieldsAreIgnored() throws Exception {
        String json = """
                {
                    "currency": "EUR",
                    "date": "2026-04-04",
                    "rate": "0.92",
                    "source": "ECB",
                    "is_business_day": true,
                    "ratedate": "2026-04-04",
                    "unknown_field": "should_be_ignored"
                }
                """;

        FxRate rate = mapper.readValue(json, FxRate.class);
        assertNotNull(rate);
        assertEquals("EUR", rate.currency());
    }
}
