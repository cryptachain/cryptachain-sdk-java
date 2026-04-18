package com.cryptachain.sdk;

import com.cryptachain.sdk.http.HttpClient;
import com.cryptachain.sdk.resource.*;

import java.time.Duration;

/**
 * Main entry point for the CryptaChain Java SDK.
 *
 * <p>Create an instance using the builder pattern:</p>
 * <pre>{@code
 * var client = CryptaChain.builder()
 *     .apiKey("your-api-key")
 *     .build();
 *
 * var balances = client.wallets().getBalances("0xABC...", "ethereum");
 * }</pre>
 *
 * <p>The client provides access to resource classes for each API domain:</p>
 * <ul>
 *   <li>{@link #wallets()} — wallet transfers, balances, and summaries</li>
 *   <li>{@link #tokens()} — token metadata</li>
 *   <li>{@link #blockchains()} — supported chains</li>
 *   <li>{@link #prices()} — asset prices (v2.3 P11 format)</li>
 *   <li>{@link #fx()} — foreign exchange rates and IAS 21 monthly averages</li>
 *   <li>{@link #screening()} — address screening and risk scoring</li>
 *   <li>{@link #health()} — system and chain status</li>
 * </ul>
 */
public final class CryptaChain {

    private final CryptaChainConfig config;
    private final HttpClient httpClient;
    private final WalletResource walletResource;
    private final TokenResource tokenResource;
    private final BlockchainResource blockchainResource;
    private final PriceResource priceResource;
    private final FxResource fxResource;
    private final ScreeningResource screeningResource;
    private final HealthResource healthResource;

    private CryptaChain(CryptaChainConfig config) {
        this.config = config;
        this.httpClient = new HttpClient(config);
        this.walletResource = new WalletResource(httpClient);
        this.tokenResource = new TokenResource(httpClient);
        this.blockchainResource = new BlockchainResource(httpClient);
        this.priceResource = new PriceResource(httpClient);
        this.fxResource = new FxResource(httpClient);
        this.screeningResource = new ScreeningResource(httpClient);
        this.healthResource = new HealthResource(httpClient);
    }

    /**
     * Creates a new builder for configuring a CryptaChain client.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a CryptaChain client from the given configuration.
     *
     * @param config the client configuration
     * @return a new CryptaChain client
     */
    public static CryptaChain create(CryptaChainConfig config) {
        return new CryptaChain(config);
    }

    /**
     * Returns the wallet resource for transfer, balance, and summary operations.
     *
     * @return the wallet resource
     */
    public WalletResource wallets() {
        return walletResource;
    }

    /**
     * Returns the token resource for metadata operations.
     *
     * @return the token resource
     */
    public TokenResource tokens() {
        return tokenResource;
    }

    /**
     * Returns the blockchain resource for chain listing.
     *
     * @return the blockchain resource
     */
    public BlockchainResource blockchains() {
        return blockchainResource;
    }

    /**
     * Returns the price resource for asset price lookups.
     *
     * @return the price resource
     */
    public PriceResource prices() {
        return priceResource;
    }

    /**
     * Returns the FX resource for foreign exchange rate operations.
     *
     * @return the FX resource
     */
    public FxResource fx() {
        return fxResource;
    }

    /**
     * Returns the screening resource for address risk scoring.
     *
     * @return the screening resource
     */
    public ScreeningResource screening() {
        return screeningResource;
    }

    /**
     * Returns the health resource for system status checks.
     *
     * @return the health resource
     */
    public HealthResource health() {
        return healthResource;
    }

    /**
     * Returns the configuration used by this client.
     *
     * @return the configuration
     */
    public CryptaChainConfig config() {
        return config;
    }

    /**
     * Builder for creating a {@link CryptaChain} client.
     */
    public static final class Builder {

        private final CryptaChainConfig.Builder configBuilder = new CryptaChainConfig.Builder();

        private Builder() {}

        /**
         * Sets the API key for authentication.
         *
         * @param apiKey the API key
         * @return this builder
         */
        public Builder apiKey(String apiKey) {
            configBuilder.apiKey(apiKey);
            return this;
        }

        /**
         * Sets the base URL for the CryptaChain API.
         *
         * @param baseUrl the base URL (default: https://api.cryptachain.com)
         * @return this builder
         */
        public Builder baseUrl(String baseUrl) {
            configBuilder.baseUrl(baseUrl);
            return this;
        }

        /**
         * Sets the connection timeout.
         *
         * @param connectTimeout the connection timeout (default: 10s)
         * @return this builder
         */
        public Builder connectTimeout(Duration connectTimeout) {
            configBuilder.connectTimeout(connectTimeout);
            return this;
        }

        /**
         * Sets the request timeout.
         *
         * @param requestTimeout the request timeout (default: 30s)
         * @return this builder
         */
        public Builder requestTimeout(Duration requestTimeout) {
            configBuilder.requestTimeout(requestTimeout);
            return this;
        }

        /**
         * Sets the maximum number of retries for failed requests.
         *
         * @param maxRetries the maximum retries (default: 3)
         * @return this builder
         */
        public Builder maxRetries(int maxRetries) {
            configBuilder.maxRetries(maxRetries);
            return this;
        }

        /**
         * Sets the base delay for exponential backoff between retries.
         *
         * @param retryBaseDelay the base delay (default: 500ms)
         * @return this builder
         */
        public Builder retryBaseDelay(Duration retryBaseDelay) {
            configBuilder.retryBaseDelay(retryBaseDelay);
            return this;
        }

        /**
         * Sets whether to automatically retry on rate-limit (429) responses.
         *
         * @param autoRetryRateLimit true to auto-retry (default: true)
         * @return this builder
         */
        public Builder autoRetryRateLimit(boolean autoRetryRateLimit) {
            configBuilder.autoRetryRateLimit(autoRetryRateLimit);
            return this;
        }

        /**
         * Builds and returns a new CryptaChain client.
         *
         * @return a configured CryptaChain client
         * @throws IllegalArgumentException if required configuration is missing
         */
        public CryptaChain build() {
            return new CryptaChain(configBuilder.build());
        }
    }
}
