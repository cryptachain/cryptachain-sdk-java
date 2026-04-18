package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Sync status for a specific blockchain.
 *
 * @param chain          the chain identifier
 * @param chainId        the EVM chain ID
 * @param name           the chain display name
 * @param status         the sync status ("synced", "syncing", "behind", "error")
 * @param latestBlock    the latest synced block number
 * @param headBlock      the chain head block number
 * @param blocksBehind   the number of blocks behind the chain head
 * @param lastSyncedAt   the timestamp of the last sync
 */
public record ChainStatus(
        String chain,
        @JsonProperty("chain_id") Integer chainId,
        String name,
        String status,
        @JsonProperty("latest_block") long latestBlock,
        @JsonProperty("head_block") long headBlock,
        @JsonProperty("blocks_behind") long blocksBehind,
        @JsonProperty("last_synced_at") String lastSyncedAt
) {}
