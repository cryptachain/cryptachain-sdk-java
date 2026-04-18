package com.cryptachain.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response containing token balances for a wallet address.
 *
 * @param balances     the list of token balances
 * @param totalValueUsd the total portfolio value in USD
 * @param chain        the chain identifier
 * @param address      the wallet address
 */
public record TokenBalancesResponse(
        List<TokenBalance> balances,
        @JsonProperty("total_value_usd") String totalValueUsd,
        String chain,
        String address
) {}
