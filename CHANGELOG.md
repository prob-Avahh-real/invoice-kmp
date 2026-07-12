# Changelog

## [1.0.0] — 2026-06-07

### Added
- Initial release: invoice and expense tracking
- Invoice management with Draft/Sent/Paid/Cancelled status
- Line item support with quantity, unit price, and automatic amount calculation
- Tax calculation (subtotal, tax amount, total)
- Invoice stats engine (counts, revenue, averages)
- Invoice validation and search
- Invoice number generation (INV-YYYY-NNNN format)
- IInvoiceRepository with InMemoryInvoiceRepository implementation
- 3 test classes with 20+ test cases
- Gradle version catalog
- CI/CD pipeline (GitHub Actions)
- ProGuard rules for release builds

### Known Issues
- Uses java.text.SimpleDateFormat in UI — needs kotlinx.datetime migration for iOS compatibility
- No persistence layer — in-memory storage only
