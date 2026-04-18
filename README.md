# CryptaChain Java SDK

Official Java SDK for the [CryptaChain API](https://docs.cryptachain.com) -- blockchain data infrastructure for 131+ chains.

## Requirements

- Java 17 or later
- No additional dependencies beyond Jackson (JSON)

## Installation

### Maven

```xml
<dependency>
    <groupId>com.cryptachain</groupId>
    <artifactId>cryptachain-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.cryptachain:cryptachain-sdk:0.1.0'
```

## Quick Start

```java
import com.cryptachain.sdk.CryptaChain;

var client = CryptaChain.builder()
    .apiKey("your-api-key")
    .build();

// Get token balances
var balances = client.wallets().getBalances("0xABC...", "ethereum");
System.out.println("Portfolio: $" + balances.totalValueUsd());

// Stream all transfers (auto-paginates)
client.wallets().streamAllTransfers("0xABC...", "ethereum")
    .forEach(tx -> System.out.printf("%s %s %s %s ($%s)%n",
        tx.timestamp(), tx.direction(), tx.valueFormatted(),
        tx.asset().symbol(), tx.valueUsdAtTime()));
```

## Configuration

```java
import java.time.Duration;

var client = CryptaChain.builder()
    .apiKey("your-api-key")
    .baseUrl("https://api.cryptachain.com")   // default
    .connectTimeout(Duration.ofSeconds(10))    // default
    .requestTimeout(Duration.ofSeconds(30))    // default
    .maxRetries(3)                             // default
    .retryBaseDelay(Duration.ofMillis(500))    // default
    .autoRetryRateLimit(true)                  // default
    .build();
```

## API Reference

### Wallets

```java
// Get paginated transfers
TransferPage page = client.wallets().getTransfers("0x...", "ethereum");
TransferPage page2 = client.wallets().getTransfers("0x...", "ethereum", page.pagination().cursor(), 50);

// Stream all transfers (auto-paginates lazily)
Stream<Transfer> allTxs = client.wallets().streamAllTransfers("0x...", "ethereum");

// Filter by transfer type
TransferPage erc20 = client.wallets().getErc20Transfers("0x...", "ethereum", null, null);
TransferPage nfts = client.wallets().getNftTransfers("0x...", "ethereum", null, null);
TransferPage native_ = client.wallets().getNativeTransfers("0x...", "ethereum", null, null);

// Balances
TokenBalancesResponse balances = client.wallets().getBalances("0x...", "ethereum");
NativeBalance nativeBal = client.wallets().getNativeBalance("0x...", "ethereum");

// Summary
WalletSummary summary = client.wallets().getSummary("0x...", "ethereum");
```

### Prices

```java
// Price by symbol (v2.3 P11 response)
PriceResponse price = client.prices().bySymbol("BTC", LocalDate.of(2026, 4, 4), "EUR");
System.out.println("BTC: " + price.price() + " " + price.currency());
System.out.println("Quality: " + price.qualityScore() + ", IFRS Level: " + price.fairValueLevel());

// Price by contract
PriceResponse tokenPrice = client.prices().byContract(1, "0xA0b8...", LocalDate.now(), "USD");

// Batch prices (up to 500)
PriceBatchRequest batch = new PriceBatchRequest(
    List.of(
        new PriceBatchRequest.PriceItem("BTC", null, null, "2026-04-04"),
        new PriceBatchRequest.PriceItem("ETH", null, null, "2026-04-04")
    ),
    "USD"
);
PriceBatchResponse batchResp = client.prices().batch(batch);

// Point-in-time price (1min precision via ClickHouse)
PriceAtResponse atPrice = client.prices().at("BTC", 1743782400L, "USD");

// Pricing methodology
Methodology methodology = client.prices().getMethodology();
```

### FX Rates

```java
// Single rate
FxRate rate = client.fx().getRate("EUR", "USD", LocalDate.of(2026, 4, 4));

// Daily history
FxHistoryResponse history = client.fx().getHistory("EUR",
    LocalDate.of(2026, 1, 1), LocalDate.of(2026, 4, 4));

// IAS 21 monthly average
FxMonthlyAverageResponse avg = client.fx().getMonthlyAverage("EUR", 2026, 4);

// List all 36 supported currencies
CurrenciesResponse currencies = client.fx().listCurrencies();
```

### Address Screening

```java
// Single address
ScreeningResult result = client.screening().screenAddress("0x...", "ethereum");
if (result.sanctionsMatch()) {
    System.err.println("SANCTIONS MATCH: " + result.flags().get(0).description());
}

// Bulk screening
BulkScreeningResponse bulk = client.screening().screenBulk(
    List.of("0xAddr1", "0xAddr2", "0xAddr3"), "ethereum");
```

### Tokens and Chains

```java
// Token metadata
TokenMetadata btc = client.tokens().getBySymbol("BTC");

// List supported chains
List<Chain> chains = client.blockchains().listChains();
```

### Health and Status

```java
// System health
SystemStatus status = client.health().getServiceStatus();

// Chain sync status
List<ChainStatus> chainStatuses = client.health().getChainStatuses();
```

### Async (CompletableFuture)

Every sync method has an async variant returning `CompletableFuture`:

```java
CompletableFuture<TokenBalancesResponse> future =
    client.wallets().getBalancesAsync("0x...", "ethereum");

future.thenAccept(balances ->
    System.out.println("Portfolio: $" + balances.totalValueUsd()));
```

## Error Handling

The SDK throws typed exceptions:

| Exception | HTTP Code | Description |
|-----------|-----------|-------------|
| `AuthenticationException` | 401 | Invalid or expired API key |
| `QuotaExceededException` | 402 | Compute unit quota exceeded |
| `ChainNotFoundException` | 404 | Chain or resource not found |
| `RateLimitException` | 429 | Rate limit exceeded |
| `CryptaChainException` | Other | Base exception for all errors |

```java
try {
    var balances = client.wallets().getBalances("0x...", "ethereum");
} catch (RateLimitException e) {
    System.out.println("Rate limited. Retry after: " + e.retryAfter());
} catch (AuthenticationException e) {
    System.out.println("Invalid API key");
} catch (QuotaExceededException e) {
    System.out.printf("Quota exceeded: %d/%d CU, resets at %s%n",
        e.usedCU(), e.limitCU(), e.resetAt());
} catch (CryptaChainException e) {
    System.out.printf("Error %d: %s (request: %s)%n",
        e.statusCode(), e.getMessage(), e.requestId());
}
```

## Retry Behavior

- Automatic retries with exponential backoff (configurable)
- 429 responses: respects `Retry-After` header, auto-retries if `autoRetryRateLimit` is enabled
- 5xx errors: retried up to `maxRetries` times
- 401/402/403/404: never retried (thrown immediately)
- Max backoff: 30 seconds per retry

## Utilities

```java
import com.cryptachain.sdk.util.AddressUtils;
import com.cryptachain.sdk.util.ChainUtils;

// Address normalization (EVM lowercased, non-EVM preserved)
String addr = AddressUtils.normalize("0xABC123...", "ethereum"); // -> "0xabc123..."
String tron = AddressUtils.normalize("TAddr...", "tron");        // -> "TAddr..." (case-sensitive)

// Chain alias resolution
String chain = ChainUtils.resolve("eth");   // -> "ethereum"
Integer id = ChainUtils.chainId("polygon"); // -> 137
```

## License

MIT License. See [LICENSE](LICENSE) for details.
