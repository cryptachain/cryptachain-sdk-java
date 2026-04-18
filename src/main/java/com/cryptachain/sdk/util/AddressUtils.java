package com.cryptachain.sdk.util;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Utility methods for blockchain address validation and normalization.
 */
public final class AddressUtils {

    private static final Pattern EVM_ADDRESS_PATTERN = Pattern.compile("^0x[0-9a-fA-F]{40}$");
    private static final Pattern SOLANA_ADDRESS_PATTERN = Pattern.compile("^[1-9A-HJ-NP-Za-km-z]{32,44}$");
    private static final Pattern BITCOIN_ADDRESS_PATTERN = Pattern.compile(
            "^(1[1-9A-HJ-NP-Za-km-z]{25,34}|3[1-9A-HJ-NP-Za-km-z]{25,34}|bc1[0-9a-zA-HJ-NP-Z]{25,90})$"
    );

    /** Chains where addresses are EVM-compatible (case-insensitive). */
    private static final Set<String> EVM_CHAINS = Set.of(
            "ethereum", "polygon", "arbitrum", "optimism", "base", "avalanche",
            "bsc", "fantom", "gnosis", "celo", "linea", "zksync", "scroll",
            "mantle", "blast", "mode", "manta", "moonbeam", "moonriver"
    );

    private AddressUtils() {}

    /**
     * Validates that the given address is a plausible blockchain address.
     *
     * @param address the address to validate
     * @return true if the address has a valid format
     */
    public static boolean isValidAddress(String address) {
        if (address == null || address.isBlank()) {
            return false;
        }
        return EVM_ADDRESS_PATTERN.matcher(address).matches()
                || SOLANA_ADDRESS_PATTERN.matcher(address).matches()
                || BITCOIN_ADDRESS_PATTERN.matcher(address).matches();
    }

    /**
     * Normalizes an address for the given chain.
     *
     * <p>EVM-compatible chain addresses are lowercased. Non-EVM addresses are returned as-is
     * because they are case-sensitive (e.g., Solana, Bitcoin, TRON, Cosmos).</p>
     *
     * @param address the address to normalize
     * @param chain   the chain identifier (e.g., "ethereum", "solana")
     * @return the normalized address
     */
    public static String normalize(String address, String chain) {
        if (address == null) {
            return null;
        }
        if (chain != null && EVM_CHAINS.contains(chain.toLowerCase())) {
            return address.toLowerCase();
        }
        return address;
    }

    /**
     * Returns whether the given chain uses EVM-compatible (case-insensitive) addresses.
     *
     * @param chain the chain identifier
     * @return true if the chain is EVM-compatible
     */
    public static boolean isEvmChain(String chain) {
        return chain != null && EVM_CHAINS.contains(chain.toLowerCase());
    }
}
