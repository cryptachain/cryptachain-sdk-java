package com.cryptachain.sdk;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CryptaChain client builder and configuration.
 */
class CryptaChainTest {

    @Test
    void builderCreatesClientWithDefaults() {
        CryptaChain client = CryptaChain.builder()
                .apiKey("test-key")
                .build();

        assertNotNull(client);
        assertNotNull(client.wallets());
        assertNotNull(client.tokens());
        assertNotNull(client.blockchains());
        assertNotNull(client.prices());
        assertNotNull(client.fx());
        assertNotNull(client.screening());
        assertNotNull(client.health());
        assertEquals("test-key", client.config().apiKey());
        assertEquals("https://api.cryptachain.com", client.config().baseUrl());
    }

    @Test
    void builderAcceptsCustomConfiguration() {
        CryptaChain client = CryptaChain.builder()
                .apiKey("custom-key")
                .baseUrl("https://staging.cryptachain.com")
                .connectTimeout(Duration.ofSeconds(5))
                .requestTimeout(Duration.ofSeconds(60))
                .maxRetries(5)
                .retryBaseDelay(Duration.ofMillis(200))
                .autoRetryRateLimit(false)
                .build();

        CryptaChainConfig config = client.config();
        assertEquals("custom-key", config.apiKey());
        assertEquals("https://staging.cryptachain.com", config.baseUrl());
        assertEquals(Duration.ofSeconds(5), config.connectTimeout());
        assertEquals(Duration.ofSeconds(60), config.requestTimeout());
        assertEquals(5, config.maxRetries());
        assertEquals(Duration.ofMillis(200), config.retryBaseDelay());
        assertFalse(config.autoRetryRateLimit());
    }

    @Test
    void builderThrowsOnMissingApiKey() {
        assertThrows(IllegalArgumentException.class, () ->
                CryptaChain.builder().build());
    }

    @Test
    void builderThrowsOnBlankApiKey() {
        assertThrows(IllegalArgumentException.class, () ->
                CryptaChain.builder().apiKey("   ").build());
    }

    @Test
    void createFromConfigWorks() {
        CryptaChainConfig config = new CryptaChainConfig.Builder()
                .apiKey("from-config")
                .build();

        CryptaChain client = CryptaChain.create(config);
        assertNotNull(client);
        assertEquals("from-config", client.config().apiKey());
    }

    @Test
    void resourceInstancesAreStable() {
        CryptaChain client = CryptaChain.builder()
                .apiKey("test-key")
                .build();

        assertSame(client.wallets(), client.wallets());
        assertSame(client.prices(), client.prices());
        assertSame(client.fx(), client.fx());
        assertSame(client.screening(), client.screening());
        assertSame(client.health(), client.health());
        assertSame(client.tokens(), client.tokens());
        assertSame(client.blockchains(), client.blockchains());
    }

    @Test
    void defaultConfigValues() {
        CryptaChainConfig config = new CryptaChainConfig.Builder()
                .apiKey("test")
                .build();

        assertEquals(Duration.ofSeconds(10), config.connectTimeout());
        assertEquals(Duration.ofSeconds(30), config.requestTimeout());
        assertEquals(3, config.maxRetries());
        assertEquals(Duration.ofMillis(500), config.retryBaseDelay());
        assertTrue(config.autoRetryRateLimit());
    }
}
