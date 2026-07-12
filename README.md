# Invoice KMP — KMP

An invoice/expense tracker built with Kotlin Multiplatform + Compose Multiplatform.

Track invoices with status (Draft, Sent, Paid, Cancelled), manage line items, calculate taxes, and view summary statistics.

## Architecture

```
model/          → Invoice, InvoiceItem, InvoiceStatus
engine/         → InvoiceEngine (stats, validation, search, invoice numbering)
data/           → IInvoiceRepository + InMemoryInvoiceRepository
composeApp/     → InvoiceListScreen (Scaffold, cards, summary stats)
```

## Build

```bash
./gradlew :shared:allTests
./gradlew :composeApp:assembleRelease
./gradlew :shared:linkDebugFrameworkIosArm64
```

## Tech Stack
- Kotlin 2.1.10, Compose Multiplatform 1.7.3
- Material3, kotlinx-coroutines, kotlinx-datetime, kotlinx-serialization
