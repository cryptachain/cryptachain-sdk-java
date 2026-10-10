package com.cryptachain.sdk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Trivy lane 2 (C:/dev/_tmp/t3/trivy2-DONE.md, cryptachain-sdk-java section; owner 2026-10-10 item 2): the Jackson
 * jars this SDK ships with (compile scope = what every consumer gets) resolve at or above the release that fixes
 * every HIGH advisory in findings_vuln.tsv.
 *
 * <p>The version is read from the jar each class was actually LOADED from, not from pom.xml, so a property that no
 * longer reaches a dependency (or a transitive downgrade) is caught too.</p>
 *
 * <p>jackson-annotations: from 2.20 on Jackson publishes it WITHOUT a patch number ({@code 2.21}, not
 * {@code 2.21.7}); jackson-databind 2.21.7 depends on annotations {@code 2.21}, so its floor is {@code 2.21}.</p>
 */
@DisplayName("Trivy lane 2: the SDK's Jackson jars resolve at or above the fix of every HIGH advisory")
class DependencyCveFloorTest {

    @ParameterizedTest(name = "{0} ({3}) >= {2}")
    @CsvSource({
            // artifact,             class loaded from that jar,                                floor,  advisories
            "jackson-core,           com.fasterxml.jackson.core.JsonFactory,                    2.21.7, CVE-2026-68494/89407/89425",
            "jackson-databind,       com.fasterxml.jackson.databind.ObjectMapper,               2.21.7, CVE-2026-54512/54513/68497/91776/91777",
            "jackson-annotations,    com.fasterxml.jackson.annotation.JsonProperty,             2.21,   databind 2.21.7 sibling (no-patch scheme)",
            "jackson-datatype-jsr310, com.fasterxml.jackson.datatype.jsr310.JavaTimeModule,     2.21.7, databind 2.21.7 sibling (same jackson.version)"
    })
    void resolvedVersionIsAtOrAboveTheFix(String artifact, String className, String floor, String advisory)
            throws Exception {
        String location = Class.forName(className, false, getClass().getClassLoader())
                .getProtectionDomain().getCodeSource().getLocation().toString();
        Matcher m = Pattern.compile("/" + Pattern.quote(artifact) + "-([0-9][0-9A-Za-z.\\-]*?)\\.jar$").matcher(location);
        assertTrue(m.find(), className + " must be loaded from a " + artifact + "-<version>.jar, was " + location);
        String resolved = m.group(1);
        assertTrue(compare(resolved, floor) >= 0,
                artifact + " resolved " + resolved + " (" + location + "); " + advisory + " is fixed in " + floor);
    }

    /** Numeric, dot-separated comparison; a non-numeric qualifier counts as 0 for its segment. */
    static int compare(String a, String b) {
        String[] x = a.split("[.\\-]");
        String[] y = b.split("[.\\-]");
        for (int i = 0; i < Math.max(x.length, y.length); i++) {
            int p = i < x.length ? num(x[i]) : 0;
            int q = i < y.length ? num(y[i]) : 0;
            if (p != q) return Integer.compare(p, q);
        }
        return 0;
    }

    private static int num(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
