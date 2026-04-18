package com.cryptachain.sdk;

import com.cryptachain.sdk.model.*;
import com.cryptachain.sdk.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for price-related model deserialization.
 */
class PriceResourceTest {

    private final ObjectMapper mapper = JsonUtils.mapper();

    @Test
    void deserializePriceResponse() throws Exception {
        String json = """
                {
                    "symbol": "BTC",
                    "date": "2026-04-04",
                    "currency": "EUR",
                    "price": "62500.00",
                    "price_usd": "68000.00",
                    "fx_rate": "0.9191",
                    "fx_source": "ECB",
                    "fx_date": "2026-04-04",
                    "source": "coingecko",
                    "quality_score": 95,
                    "fair_value_level": "1",
                    "source_count": 5,
                    "methodology_version": "2.3",
                    "stablecoin_pegged": false,
                    "manual_override": false,
                    "computed_at": "2026-04-04T12:00:00Z"
                }
                """;

        PriceResponse price = mapper.readValue(json, PriceResponse.class);
        assertEquals("BTC", price.symbol());
        assertEquals("EUR", price.currency());
        assertEquals("62500.00", price.price());
        assertEquals("68000.00", price.priceUsd());
        assertEquals("0.9191", price.fxRate());
        assertEquals("ECB", price.fxSource());
        assertEquals(95, price.qualityScore());
        assertEquals("1", price.fairValueLevel());
        assertEquals(5, price.sourceCount());
        assertFalse(price.stablecoinPegged());
        assertFalse(price.manualOverride());
    }

    @Test
    void deserializePriceBatchResponse() throws Exception {
        String json = """
                {
                    "prices": [
                        {
                            "symbol": "BTC",
                            "date": "2026-04-04",
                            "currency": "USD",
                            "price": "68000.00",
                            "price_usd": "68000.00",
                            "fx_rate": "1.0000",
                            "fx_source": null,
                            "fx_date": null,
                            "source": "coingecko",
                            "quality_score": 95,
                            "fair_value_level": "1",
                            "source_count": 5,
                            "methodology_version": "2.3",
                            "stablecoin_pegged": false,
                            "manual_override": false,
                            "computed_at": "2026-04-04T12:00:00Z"
                        },
                        {
                            "symbol": "ETH",
                            "date": "2026-04-04",
                            "currency": "USD",
                            "price": "3500.00",
                            "price_usd": "3500.00",
                            "fx_rate": "1.0000",
                            "fx_source": null,
                            "fx_date": null,
                            "source": "coingecko",
                            "quality_score": 93,
                            "fair_value_level": "1",
                            "source_count": 4,
                            "methodology_version": "2.3",
                            "stablecoin_pegged": false,
                            "manual_override": false,
                            "computed_at": "2026-04-04T12:00:00Z"
                        }
                    ],
                    "total": 2
                }
                """;

        PriceBatchResponse resp = mapper.readValue(json, PriceBatchResponse.class);
        assertEquals(2, resp.total());
        assertEquals(2, resp.prices().size());
        assertEquals("BTC", resp.prices().get(0).symbol());
        assertEquals("ETH", resp.prices().get(1).symbol());
    }

    @Test
    void deserializePriceAtResponse() throws Exception {
        String json = """
                {
                    "symbol": "BTC",
                    "timestamp": "2026-04-04T12:30:00Z",
                    "currency": "USD",
                    "price": "67850.25",
                    "price_usd": "67850.25",
                    "source": "clickhouse"
                }
                """;

        PriceAtResponse resp = mapper.readValue(json, PriceAtResponse.class);
        assertEquals("BTC", resp.symbol());
        assertEquals("67850.25", resp.price());
        assertEquals("clickhouse", resp.source());
    }

    @Test
    void deserializeMethodology() throws Exception {
        String json = """
                {
                    "version": "2.3",
                    "description": "Multi-source price aggregation",
                    "sources": "CoinGecko, Binance, Coinbase",
                    "quality_scoring": "Weighted by volume and reliability",
                    "fair_value_levels": "Level 1: Active market, Level 2: Observable inputs, Level 3: Unobservable",
                    "updated_at": "2026-03-01T00:00:00Z"
                }
                """;

        Methodology m = mapper.readValue(json, Methodology.class);
        assertEquals("2.3", m.version());
        assertNotNull(m.qualityScoring());
        assertNotNull(m.fairValueLevels());
    }

    @Test
    void serializePriceBatchRequest() throws Exception {
        PriceBatchRequest request = new PriceBatchRequest(
                List.of(
                        new PriceBatchRequest.PriceItem("BTC", null, null, "2026-04-04"),
                        new PriceBatchRequest.PriceItem(null, 1, "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", "2026-04-04")
                ),
                "USD"
        );

        String json = mapper.writeValueAsString(request);
        assertNotNull(json);
        assertTrue(json.contains("\"currency\":\"USD\""));
        assertTrue(json.contains("\"BTC\""));

        // Verify round-trip
        PriceBatchRequest deserialized = mapper.readValue(json, PriceBatchRequest.class);
        assertEquals(2, deserialized.requests().size());
        assertEquals("USD", deserialized.currency());
    }
}
