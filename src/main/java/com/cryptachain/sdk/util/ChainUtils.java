package com.cryptachain.sdk.util;

import java.util.Map;

/**
 * Utility methods for chain identifier mapping and validation.
 */
public final class ChainUtils {

    /** Maps common chain aliases to their canonical API identifiers. */
    private static final Map<String, String> CHAIN_ALIASES = Map.ofEntries(
            Map.entry("eth", "ethereum"),
            Map.entry("matic", "polygon"),
            Map.entry("arb", "arbitrum"),
            Map.entry("op", "optimism"),
            Map.entry("avax", "avalanche"),
            Map.entry("ftm", "fantom"),
            Map.entry("bnb", "bsc"),
            Map.entry("sol", "solana"),
            Map.entry("btc", "bitcoin"),
            Map.entry("dot", "polkadot"),
            Map.entry("atom", "cosmos")
    );

    /** Maps chain identifiers to their EVM chain IDs. */
    private static final Map<String, Integer> CHAIN_IDS = Map.ofEntries(
            Map.entry("ethereum", 1),
            Map.entry("polygon", 137),
            Map.entry("arbitrum", 42161),
            Map.entry("optimism", 10),
            Map.entry("base", 8453),
            Map.entry("avalanche", 43114),
            Map.entry("bsc", 56),
            Map.entry("fantom", 250),
            Map.entry("gnosis", 100),
            Map.entry("linea", 59144),
            Map.entry("zksync", 324),
            Map.entry("scroll", 534352),
            Map.entry("blast", 81457)
    );

    private ChainUtils() {}

    /**
     * Resolves a chain alias to its canonical identifier.
     *
     * @param chain the chain name or alias (e.g., "eth", "matic")
     * @return the canonical chain identifier (e.g., "ethereum", "polygon"), or the input lowercased if no alias found
     */
    public static String resolve(String chain) {
        if (chain == null) {
            return null;
        }
        String lower = chain.toLowerCase();
        return CHAIN_ALIASES.getOrDefault(lower, lower);
    }

    /**
     * Returns the EVM chain ID for the given chain, or null if not an EVM chain.
     *
     * @param chain the chain identifier
     * @return the chain ID, or null
     */
    public static Integer chainId(String chain) {
        if (chain == null) {
            return null;
        }
        return CHAIN_IDS.get(resolve(chain));
    }
}
