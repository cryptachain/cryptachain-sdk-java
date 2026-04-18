package com.cryptachain.sdk;

import com.cryptachain.sdk.model.*;
import com.cryptachain.sdk.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for wallet-related model deserialization and behavior.
 */
class WalletResourceTest {

    private final ObjectMapper mapper = JsonUtils.mapper();

    @Test
    void deserializeTransferPage() throws Exception {
        String json = """
                {
                    "transfers": [
                        {
                            "hash": "0xabc123",
                            "block_number": 19500000,
                            "timestamp": "2026-04-04T12:00:00Z",
                            "from": "0xsender",
                            "to": "0xreceiver",
                            "value": "1000000000000000000",
                            "value_formatted": "1.0",
                            "asset": {
                                "symbol": "ETH",
                                "name": "Ether",
                                "contract_address": null,
                                "decimals": 18,
                                "token_id": null
                            },
                            "direction": "out",
                            "transfer_type": "native",
                            "value_usd_at_time": "3500.00",
                            "fee": "21000000000000",
                            "fee_usd": "0.07",
                            "status": "success",
                            "chain": "ethereum"
                        }
                    ],
                    "pagination": {
                        "cursor": "next_abc",
                        "page_size": 100,
                        "has_more": true,
                        "total_count": 500
                    }
                }
                """;

        TransferPage page = mapper.readValue(json, TransferPage.class);
        assertNotNull(page);
        assertEquals(1, page.transfers().size());

        Transfer tx = page.transfers().get(0);
        assertEquals("0xabc123", tx.hash());
        assertEquals(19500000L, tx.blockNumber());
        assertEquals("out", tx.direction());
        assertEquals("1.0", tx.valueFormatted());
        assertEquals("ETH", tx.asset().symbol());
        assertNull(tx.asset().contractAddress());
        assertEquals("3500.00", tx.valueUsdAtTime());

        Pagination pagination = page.pagination();
        assertEquals("next_abc", pagination.cursor());
        assertTrue(pagination.hasMore());
        assertEquals(100, pagination.pageSize());
        assertEquals(500L, pagination.totalCount());
    }

    @Test
    void deserializeTokenBalancesResponse() throws Exception {
        String json = """
                {
                    "balances": [
                        {
                            "symbol": "USDC",
                            "name": "USD Coin",
                            "contract_address": "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48",
                            "balance": "5000000000",
                            "balance_formatted": "5000.00",
                            "decimals": 6,
                            "price_usd": "1.00",
                            "value_usd": "5000.00",
                            "chain": "ethereum"
                        }
                    ],
                    "total_value_usd": "5000.00",
                    "chain": "ethereum",
                    "address": "0xwallet"
                }
                """;

        TokenBalancesResponse resp = mapper.readValue(json, TokenBalancesResponse.class);
        assertNotNull(resp);
        assertEquals("5000.00", resp.totalValueUsd());
        assertEquals(1, resp.balances().size());

        TokenBalance bal = resp.balances().get(0);
        assertEquals("USDC", bal.symbol());
        assertEquals("5000.00", bal.balanceFormatted());
        assertEquals(6, bal.decimals());
    }

    @Test
    void deserializeNativeBalance() throws Exception {
        String json = """
                {
                    "balance": "2500000000000000000",
                    "balance_formatted": "2.5",
                    "symbol": "ETH",
                    "price_usd": "3500.00",
                    "value_usd": "8750.00",
                    "chain": "ethereum",
                    "address": "0xwallet"
                }
                """;

        NativeBalance nb = mapper.readValue(json, NativeBalance.class);
        assertEquals("2.5", nb.balanceFormatted());
        assertEquals("ETH", nb.symbol());
        assertEquals("8750.00", nb.valueUsd());
    }

    @Test
    void deserializeWalletSummary() throws Exception {
        String json = """
                {
                    "address": "0xwallet",
                    "chain": "ethereum",
                    "native_balance": "2.5",
                    "token_count": 15,
                    "nft_count": 3,
                    "total_value_usd": "25000.00",
                    "transaction_count": 450,
                    "first_transaction": "2020-01-15T10:00:00Z",
                    "last_transaction": "2026-04-04T14:30:00Z",
                    "is_contract": false
                }
                """;

        WalletSummary summary = mapper.readValue(json, WalletSummary.class);
        assertEquals("0xwallet", summary.address());
        assertEquals(15, summary.tokenCount());
        assertEquals(450L, summary.transactionCount());
        assertFalse(summary.isContract());
    }

    @Test
    void transferRecordEquality() {
        Transfer.TransferAsset asset = new Transfer.TransferAsset("ETH", "Ether", null, 18, null);
        Transfer t1 = new Transfer("0xhash", 100, "2026-01-01T00:00:00Z", "0xa", "0xb",
                "1", "1.0", asset, "out", "native", "3500", "0.01", "0.035", "success", "ethereum");
        Transfer t2 = new Transfer("0xhash", 100, "2026-01-01T00:00:00Z", "0xa", "0xb",
                "1", "1.0", asset, "out", "native", "3500", "0.01", "0.035", "success", "ethereum");
        assertEquals(t1, t2);
    }
}
