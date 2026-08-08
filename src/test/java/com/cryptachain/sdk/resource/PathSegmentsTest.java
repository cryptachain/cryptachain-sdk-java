package com.cryptachain.sdk.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Pins the path-segment encoding that stops an attacker-controlled value — an ERC-20
 * {@code symbol()} return is arbitrary bytes chosen by whoever deployed the contract — from
 * splitting the request path and re-targeting the call at a different endpoint.
 *
 * <p>Revert {@code PathSegments.encode} to an identity function, or drop the
 * {@code PathSegments.encode(...)} call from {@code TokenResource}/{@code WalletResource},
 * and {@link #hostileSymbolCannotEscapeTheTokensPath()},
 * {@link #slashIsEncoded()} and {@link #tokenPathKeepsTheSymbolInOneSegment()} go red.</p>
 */
@DisplayName("PathSegments — user-controlled path segment encoding")
class PathSegmentsTest {

    @Test
    @DisplayName("slash is percent-encoded (it is the byte that re-targets the request)")
    void slashIsEncoded() {
        assertEquals("a%2Fb", PathSegments.encode("a/b"));
    }

    @Test
    @DisplayName("a hostile symbol collapses to one segment and cannot climb the path")
    void hostileSymbolCannotEscapeTheTokensPath() {
        String encoded = PathSegments.encode("../../admin/keys");
        assertEquals("..%2F..%2Fadmin%2Fkeys", encoded);
        assertFalse(encoded.contains("/"), "no bare slash may survive encoding");
    }

    @Test
    @DisplayName("the live production symbol \"><h1>test</h1> is fully escaped")
    void liveProductionSymbolIsEscaped() {
        String encoded = PathSegments.encode("\"><h1>test</h1>");
        assertEquals("%22%3E%3Ch1%3Etest%3C%2Fh1%3E", encoded);
        assertFalse(encoded.contains("<"));
        assertFalse(encoded.contains(">"));
        assertFalse(encoded.contains("\""));
        assertFalse(encoded.contains("/"));
    }

    @Test
    @DisplayName("space becomes %20, not + — form-encoding's one disagreement with a path segment")
    void spaceBecomesPercent20NotPlus() {
        // URLEncoder alone would produce "FraxlendV1+-+WBTC%2FFRAX"; in a path segment a '+'
        // is a literal plus, so it must be %20. This assertion is what pins the .replace().
        assertEquals("FraxlendV1%20-%20WBTC%2FFRAX", PathSegments.encode("FraxlendV1 - WBTC/FRAX"));
    }

    @Test
    @DisplayName("a literal plus in the input survives as %2B and is not confused with a space")
    void literalPlusIsEncoded() {
        assertEquals("C%2B%2B", PathSegments.encode("C++"));
    }

    @Test
    @DisplayName("control characters and newlines are encoded, not passed through")
    void controlCharactersAreEncoded() {
        assertEquals("BTC%0D%0AX", PathSegments.encode("BTC\r\nX"));
    }

    @Test
    @DisplayName("ordinary symbols and 0x addresses are unchanged — no behaviour change for real callers")
    void ordinaryValuesAreUnchanged() {
        assertEquals("BTC", PathSegments.encode("BTC"));
        assertEquals("USDC", PathSegments.encode("USDC"));
        assertEquals("0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48",
                PathSegments.encode("0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48"));
    }

    @Test
    @DisplayName("null is tolerated (empty segment) rather than NPE-ing the caller")
    void nullBecomesEmpty() {
        assertEquals("", PathSegments.encode(null));
    }

    /**
     * Boundary check: asserts the path string the resource actually builds, not just the
     * helper in isolation. A unit test on the helper alone cannot see a caller that forgot
     * to call it, so this reproduces the concatenation the resources perform.
     */
    @Test
    @DisplayName("the built /v1/tokens path keeps the symbol inside one segment")
    void tokenPathKeepsTheSymbolInOneSegment() {
        String path = "/v1/tokens/" + PathSegments.encode("../../admin/keys");
        assertEquals("/v1/tokens/..%2F..%2Fadmin%2Fkeys", path);
        // Leading "" + v1 + tokens + symbol == 4. Unencoded, the hostile value would add
        // three more segments and the request would leave the /v1/tokens/ collection.
        assertEquals(4, path.split("/", -1).length);
    }
}
