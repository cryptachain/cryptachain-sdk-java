package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Summary information for a wallet address.
 *
 * @param address           the wallet address
 * @param chain             the chain identifier
 * @param nativeBalance     the native token balance (formatted)
 * @param tokenCount        the number of tokens held
 * @param nftCount          the number of NFTs held
 * @param totalValueUsd     the total portfolio value in USD
 * @param transactionCount  the total number of transactions
 * @param firstTransaction  the timestamp of the first transaction
 * @param lastTransaction   the timestamp of the most recent transaction
 * @param isContract        whether the address is a smart contract
 */
public record WalletSummary(
        String address,
        String chain,
        @JsonProperty("native_balance") String nativeBalance,
        @JsonProperty("token_count") int tokenCount,
        @JsonProperty("nft_count") int nftCount,
        @JsonProperty("total_value_usd") String totalValueUsd,
        @JsonProperty("transaction_count") long transactionCount,
        @JsonProperty("first_transaction") String firstTransaction,
        @JsonProperty("last_transaction") String lastTransaction,
        @JsonProperty("is_contract") boolean isContract
) {}
