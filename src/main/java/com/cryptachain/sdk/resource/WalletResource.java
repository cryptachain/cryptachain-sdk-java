package com.cryptachain.sdk.resource;

import com.cryptachain.sdk.http.HttpClient;
import com.cryptachain.sdk.model.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Resource for wallet-related API operations.
 *
 * <p>Provides methods to query wallet transfers, balances, and summaries across supported chains.</p>
 */
public final class WalletResource {

    private final HttpClient httpClient;

    /**
     * Creates a new WalletResource.
     *
     * @param httpClient the HTTP client
     */
    public WalletResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Gets a page of transfers for a wallet address.
     *
     * @param address  the wallet address
     * @param chain    the chain identifier (e.g., "ethereum")
     * @param cursor   the pagination cursor (null for first page)
     * @param pageSize the page size (default 100)
     * @return a page of transfers
     */
    public TransferPage getTransfers(String address, String chain, String cursor, Integer pageSize) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("chain", chain);
        if (cursor != null) params.put("cursor", cursor);
        if (pageSize != null) params.put("page_size", pageSize.toString());
        return httpClient.get("/v1/wallets/" + address + "/transfers", params, TransferPage.class);
    }

    /**
     * Gets a page of transfers for a wallet address with default page size.
     *
     * @param address the wallet address
     * @param chain   the chain identifier
     * @return a page of transfers
     */
    public TransferPage getTransfers(String address, String chain) {
        return getTransfers(address, chain, null, null);
    }

    /**
     * Asynchronously gets a page of transfers for a wallet address.
     *
     * @param address  the wallet address
     * @param chain    the chain identifier
     * @param cursor   the pagination cursor (null for first page)
     * @param pageSize the page size
     * @return a CompletableFuture with the transfer page
     */
    public CompletableFuture<TransferPage> getTransfersAsync(String address, String chain,
                                                              String cursor, Integer pageSize) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("chain", chain);
        if (cursor != null) params.put("cursor", cursor);
        if (pageSize != null) params.put("page_size", pageSize.toString());
        return httpClient.getAsync("/v1/wallets/" + address + "/transfers", params, TransferPage.class);
    }

    /**
     * Returns a Stream that automatically paginates through all transfers for a wallet.
     *
     * <p>The stream lazily fetches pages as needed using cursor-based pagination.
     * This is ideal for processing large transfer histories without loading everything into memory.</p>
     *
     * @param address the wallet address
     * @param chain   the chain identifier
     * @return a Stream of all transfers
     */
    public Stream<Transfer> streamAllTransfers(String address, String chain) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new TransferIterator(address, chain),
                        Spliterator.ORDERED | Spliterator.NONNULL
                ),
                false
        );
    }

    /**
     * Gets ERC-20 token transfers for a wallet address.
     *
     * @param address  the wallet address
     * @param chain    the chain identifier
     * @param cursor   the pagination cursor (null for first page)
     * @param pageSize the page size
     * @return a page of ERC-20 transfers
     */
    public TransferPage getErc20Transfers(String address, String chain, String cursor, Integer pageSize) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("chain", chain);
        if (cursor != null) params.put("cursor", cursor);
        if (pageSize != null) params.put("page_size", pageSize.toString());
        return httpClient.get("/v1/wallets/" + address + "/erc20-transfers", params, TransferPage.class);
    }

    /**
     * Gets NFT transfers for a wallet address.
     *
     * @param address  the wallet address
     * @param chain    the chain identifier
     * @param cursor   the pagination cursor (null for first page)
     * @param pageSize the page size
     * @return a page of NFT transfers
     */
    public TransferPage getNftTransfers(String address, String chain, String cursor, Integer pageSize) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("chain", chain);
        if (cursor != null) params.put("cursor", cursor);
        if (pageSize != null) params.put("page_size", pageSize.toString());
        return httpClient.get("/v1/wallets/" + address + "/nft-transfers", params, TransferPage.class);
    }

    /**
     * Gets native token transfers for a wallet address.
     *
     * @param address  the wallet address
     * @param chain    the chain identifier
     * @param cursor   the pagination cursor (null for first page)
     * @param pageSize the page size
     * @return a page of native transfers
     */
    public TransferPage getNativeTransfers(String address, String chain, String cursor, Integer pageSize) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("chain", chain);
        if (cursor != null) params.put("cursor", cursor);
        if (pageSize != null) params.put("page_size", pageSize.toString());
        return httpClient.get("/v1/wallets/" + address + "/native-transfers", params, TransferPage.class);
    }

    /**
     * Gets token balances for a wallet address.
     *
     * @param address the wallet address
     * @param chain   the chain identifier
     * @return the token balances response
     */
    public TokenBalancesResponse getBalances(String address, String chain) {
        Map<String, String> params = Map.of("chain", chain);
        return httpClient.get("/v1/wallets/" + address + "/balances", params, TokenBalancesResponse.class);
    }

    /**
     * Asynchronously gets token balances for a wallet address.
     *
     * @param address the wallet address
     * @param chain   the chain identifier
     * @return a CompletableFuture with the token balances
     */
    public CompletableFuture<TokenBalancesResponse> getBalancesAsync(String address, String chain) {
        Map<String, String> params = Map.of("chain", chain);
        return httpClient.getAsync("/v1/wallets/" + address + "/balances", params, TokenBalancesResponse.class);
    }

    /**
     * Gets the native token balance for a wallet address.
     *
     * @param address the wallet address
     * @param chain   the chain identifier
     * @return the native balance
     */
    public NativeBalance getNativeBalance(String address, String chain) {
        Map<String, String> params = Map.of("chain", chain);
        return httpClient.get("/v1/wallets/" + address + "/native-balance", params, NativeBalance.class);
    }

    /**
     * Asynchronously gets the native token balance for a wallet address.
     *
     * @param address the wallet address
     * @param chain   the chain identifier
     * @return a CompletableFuture with the native balance
     */
    public CompletableFuture<NativeBalance> getNativeBalanceAsync(String address, String chain) {
        Map<String, String> params = Map.of("chain", chain);
        return httpClient.getAsync("/v1/wallets/" + address + "/native-balance", params, NativeBalance.class);
    }

    /**
     * Gets a summary for a wallet address.
     *
     * @param address the wallet address
     * @param chain   the chain identifier
     * @return the wallet summary
     */
    public WalletSummary getSummary(String address, String chain) {
        Map<String, String> params = Map.of("chain", chain);
        return httpClient.get("/v1/wallets/" + address + "/summary", params, WalletSummary.class);
    }

    /**
     * Asynchronously gets a summary for a wallet address.
     *
     * @param address the wallet address
     * @param chain   the chain identifier
     * @return a CompletableFuture with the wallet summary
     */
    public CompletableFuture<WalletSummary> getSummaryAsync(String address, String chain) {
        Map<String, String> params = Map.of("chain", chain);
        return httpClient.getAsync("/v1/wallets/" + address + "/summary", params, WalletSummary.class);
    }

    /**
     * Iterator that auto-paginates through transfer pages using cursor-based pagination.
     */
    private class TransferIterator implements Iterator<Transfer> {
        private final String address;
        private final String chain;
        private String cursor;
        private Iterator<Transfer> currentPage;
        private boolean hasMore = true;

        TransferIterator(String address, String chain) {
            this.address = address;
            this.chain = chain;
            fetchNextPage();
        }

        @Override
        public boolean hasNext() {
            if (currentPage != null && currentPage.hasNext()) {
                return true;
            }
            if (hasMore) {
                fetchNextPage();
                return currentPage != null && currentPage.hasNext();
            }
            return false;
        }

        @Override
        public Transfer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return currentPage.next();
        }

        private void fetchNextPage() {
            TransferPage page = getTransfers(address, chain, cursor, null);
            if (page.transfers() != null && !page.transfers().isEmpty()) {
                currentPage = page.transfers().iterator();
                if (page.pagination() != null) {
                    cursor = page.pagination().cursor();
                    hasMore = page.pagination().hasMore();
                } else {
                    hasMore = false;
                }
            } else {
                currentPage = Collections.emptyIterator();
                hasMore = false;
            }
        }
    }
}
