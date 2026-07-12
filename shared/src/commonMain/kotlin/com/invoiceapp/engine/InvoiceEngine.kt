package com.invoiceapp.engine

import com.invoiceapp.model.Invoice
import com.invoiceapp.model.InvoiceItem
import com.invoiceapp.model.InvoiceStatus

data class InvoiceStats(
    val totalCount: Int,
    val totalRevenue: Double,
    val paidCount: Int,
    val draftCount: Int,
    val sentCount: Int,
    val cancelledCount: Int,
    val averageInvoiceAmount: Double,
    val maxInvoiceAmount: Double,
    val minInvoiceAmount: Double
)

class InvoiceEngine {

    fun computeStats(invoices: List<Invoice>): InvoiceStats {
        val total = invoices.size
        val revenue = invoices.filter { it.status == InvoiceStatus.Paid }.sumOf { it.total }
        val amounts = if (invoices.isEmpty()) emptyList() else invoices.map { it.total }
        return InvoiceStats(
            totalCount = total,
            totalRevenue = revenue,
            paidCount = invoices.count { it.status == InvoiceStatus.Paid },
            draftCount = invoices.count { it.status == InvoiceStatus.Draft },
            sentCount = invoices.count { it.status == InvoiceStatus.Sent },
            cancelledCount = invoices.count { it.status == InvoiceStatus.Cancelled },
            averageInvoiceAmount = if (amounts.isNotEmpty()) amounts.average() else 0.0,
            maxInvoiceAmount = amounts.maxOrNull() ?: 0.0,
            minInvoiceAmount = amounts.minOrNull() ?: 0.0
        )
    }

    fun generateInvoiceNumber(prefix: String = "INV", year: Int = 2026, sequence: Int): String {
        val seq = sequence.toString().padStart(4, '0')
        return "$prefix-$year-$seq"
    }

    fun validateInvoice(invoice: Invoice): List<String> {
        val errors = mutableListOf<String>()
        if (invoice.customerName.isBlank()) errors.add("Customer name is required")
        if (invoice.invoiceNumber.isBlank()) errors.add("Invoice number is required")
        if (invoice.items.isEmpty()) errors.add("At least one item is required")
        if (invoice.items.any { it.quantity <= 0 }) errors.add("Item quantities must be positive")
        if (invoice.items.any { it.unitPrice < 0 }) errors.add("Item prices cannot be negative")
        if (invoice.taxRate < 0) errors.add("Tax rate cannot be negative")
        if (invoice.taxRate > 100) errors.add("Tax rate cannot exceed 100%")
        return errors
    }

    fun calculateSubtotal(items: List<InvoiceItem>): Double = items.sumOf { it.amount }

    fun calculateTax(subtotal: Double, taxRate: Double): Double = subtotal * taxRate / 100.0

    fun calculateTotal(subtotal: Double, tax: Double): Double = subtotal + tax

    fun filterByStatus(invoices: List<Invoice>, status: InvoiceStatus): List<Invoice> =
        invoices.filter { it.status == status }

    fun searchInvoices(invoices: List<Invoice>, query: String): List<Invoice> {
        val q = query.lowercase()
        return invoices.filter {
            it.customerName.lowercase().contains(q) ||
            it.invoiceNumber.lowercase().contains(q)
        }
    }
}
