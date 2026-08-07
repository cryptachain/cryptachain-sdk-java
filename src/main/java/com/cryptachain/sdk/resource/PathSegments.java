package com.cryptachain.sdk.resource;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Percent-encoding for user-supplied values interpolated into a request path.
 *
 * <p>Why this exists: {@link com.cryptachain.sdk.http.HttpClient} already percent-encodes
 * <em>query parameters</em> (see {@code HttpClient#buildUrl}), but path segments were
 * concatenated raw. Values such as an ERC-20 {@code symbol()} return are arbitrary
 * attacker-controlled bytes — anyone can deploy a token whose symbol contains {@code /}
 * or {@code ..} — and an unencoded {@code /} splits the path, re-targeting the request at a
 * different endpoint than the caller asked for.</p>
 *
 * <p>This javadoc used to read "The TypeScript SDK guards the same call with
 * {@code encodeURIComponent}; this brings the Java SDK into line with it." That was true of
 * exactly one of twelve TypeScript call sites. {@code wallets.ts} (7 sites) and
 * {@code blockchain.ts} (4) interpolated raw into the path, and {@code client.ts#buildUrl}
 * encodes only query parameters, never the path — so the sentence asserted a guarantee in a
 * sibling codebase that did not hold, in a comment a reader would reasonably trust. All
 * twelve were encoded on 2026-08-07. <b>Do not restate another codebase's safety property
 * here;</b> it cannot be verified from this file and rots silently when the other side
 * changes.</p>
 *
 * <p>Why {@link URLEncoder} plus a {@code +} fix-up, rather than something else:</p>
 * <ul>
 *   <li>{@code URLEncoder} implements {@code application/x-www-form-urlencoded}, not
 *       RFC 3986 path-segment encoding. The two agree on every character it emits
 *       <em>except</em> the space, which form-encoding renders as {@code +}. In a path segment
 *       {@code +} is a literal plus sign, not a space, so it is rewritten to {@code %20}.
 *       Every other byte it emits is a {@code %XX} triplet (legal in a path segment), and every
 *       byte it leaves literal ({@code A-Za-z0-9}, {@code .}, {@code -}, {@code *}, {@code _})
 *       is a legal RFC 3986 {@code pchar}. So after the {@code +} fix-up the output is a valid,
 *       single path segment.</li>
 *   <li>The {@code URI(scheme, host, path, fragment)} multi-argument constructor was rejected:
 *       it treats {@code /} as a legal path character and leaves it intact, which is exactly
 *       the byte that has to be neutralised here.</li>
 *   <li>Guava's {@code UrlEscapers.urlPathSegmentEscaper()} was rejected because it would add a
 *       runtime dependency to a published SDK whose only runtime dependency today is Jackson.</li>
 *   <li>{@code URLEncoder} is already the mechanism this client uses for query parameters, so
 *       reusing it keeps one encoding story in the codebase instead of two.</li>
 * </ul>
 *
 * <p>Note that {@code .} is deliberately left literal, so a segment of {@code ..} survives as
 * {@code ..}. That matches {@code encodeURIComponent} and is harmless here: with {@code /}
 * encoded, a hostile value collapses to a single segment and cannot climb the path.</p>
 */
final class PathSegments {

    private PathSegments() {
    }

    /**
     * Percent-encodes a value for safe use as a single URL path segment.
     *
     * @param value the raw, possibly untrusted value; may be null
     * @return the encoded segment, or an empty string when {@code value} is null
     */
    static String encode(String value) {
        if (value == null) {
            return "";
        }
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
