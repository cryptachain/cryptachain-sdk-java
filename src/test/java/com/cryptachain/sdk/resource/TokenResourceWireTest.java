package com.cryptachain.sdk.resource;

import com.cryptachain.sdk.CryptaChain;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Wire-level counterpart to {@link PathSegmentsTest}.
 *
 * <p>{@code PathSegmentsTest} exercises the encoder; this exercises the <em>caller</em>. A unit
 * test on a helper cannot see a resource method that forgot to call it, so this asserts the raw
 * request line a real server receives when {@code tokens().getBySymbol(...)} is invoked with an
 * attacker-controlled symbol. Uses the JDK's own {@code com.sun.net.httpserver.HttpServer} on an
 * ephemeral loopback port — no new dependency, no external network.</p>
 *
 * <p>Delete {@code PathSegments.encode(...)} from {@code TokenResource.getBySymbol} and
 * {@link #hostileSymbolNeverLeavesTheTokensCollection()} goes red: the server sees
 * {@code /v1/admin/keys} instead of {@code /v1/tokens/...}.</p>
 */
@DisplayName("TokenResource — the path the server actually receives")
class TokenResourceWireTest {

    private HttpServer server;
    private final List<String> receivedPaths = new ArrayList<>();
    private String baseUrl;

    @BeforeEach
    void startServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            // getRawPath() — NOT getPath() — so percent-encoding is visible. A decoded
            // comparison would pass just as happily against the unencoded code.
            receivedPaths.add(exchange.getRequestURI().getRawPath());
            byte[] body = "{\"symbol\":\"OK\",\"name\":\"Fine\",\"decimals\":18}"
                    .getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        });
        server.start();
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
    }

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    private CryptaChain client() {
        return CryptaChain.builder().apiKey("test-key").baseUrl(baseUrl).maxRetries(0).build();
    }

    @Test
    @DisplayName("a symbol containing ../ cannot re-target the request off /v1/tokens/")
    void hostileSymbolNeverLeavesTheTokensCollection() {
        client().tokens().getBySymbol("../../admin/keys");

        assertEquals(1, receivedPaths.size());
        String path = receivedPaths.get(0);
        assertTrue(path.startsWith("/v1/tokens/"), "request left the tokens collection: " + path);
        assertFalse(path.contains("/admin"), "request reached /admin: " + path);
        assertEquals("/v1/tokens/..%2F..%2Fadmin%2Fkeys", path);
    }

    @Test
    @DisplayName("the live production symbol \"><h1>test</h1> arrives fully escaped")
    void liveProductionSymbolArrivesEscaped() {
        client().tokens().getBySymbol("\"><h1>test</h1>");

        assertEquals("/v1/tokens/%22%3E%3Ch1%3Etest%3C%2Fh1%3E", receivedPaths.get(0));
    }

    @Test
    @DisplayName("an ordinary symbol is unaffected — no behaviour change for real callers")
    void ordinarySymbolIsUnchanged() {
        client().tokens().getBySymbol("USDC");

        assertEquals("/v1/tokens/USDC", receivedPaths.get(0));
    }

    @Test
    @DisplayName("a wallet address with an embedded slash stays inside one segment")
    void walletAddressStaysInOneSegment() {
        try {
            client().wallets().getBalances("0xabc/../../admin", "ethereum");
        } catch (RuntimeException ignored) {
            // Response-shape mismatch is irrelevant — the request line is already captured.
        }

        assertEquals("/v1/wallets/0xabc%2F..%2F..%2Fadmin/balances", receivedPaths.get(0));
    }
}
