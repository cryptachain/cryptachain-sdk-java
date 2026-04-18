package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Information about a supported blockchain.
 *
 * @param id             the chain identifier (e.g., "ethereum")
 * @param chainId        the EVM chain ID (null for non-EVM chains)
 * @param name           the display name
 * @param nativeSymbol   the native token symbol
 * @param type           the chain type ("evm", "utxo", "account")
 * @param testnet        whether this is a testnet
 * @param explorerUrl    the block explorer URL
 * @param rpcUrl         the public RPC URL
 */
public record Chain(
        String id,
        @JsonProperty("chain_id") Integer chainId,
        String name,
        @JsonProperty("native_symbol") String nativeSymbol,
        String type,
        boolean testnet,
        @JsonProperty("explorer_url") String explorerUrl,
        @JsonProperty("rpc_url") String rpcUrl
) {}
