package com.invoiceapp.engine

import com.invoiceapp.model.Invoice
import com.invoiceapp.model.InvoiceItem
import com.invoiceapp.model.InvoiceStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class InvoiceEngineTest {

    @Test
    fun `computeStats should handle empty list`() {
        val stats = InvoiceEngine().computeStats(emptyList())
        assertEquals(0, stats.totalCount)
        assertEquals(0.0, stats.totalRevenue)
        assertEquals(0.0, stats.averageInvoiceAmount)
    }

    @Test
    fun `computeStats should calculate correctly`() {
        val paid = Invoice(id = "1", invoiceNumber = "INV-001", customerName = "A",
            items = listOf(InvoiceItem(description = "Item", quantity = 2, unitPrice = 50.0)),
            status = InvoiceStatus.Paid)
        val draft = Invoice(id = "2", invoiceNumber = "INV-002", customerName = "B",
            items = listOf(InvoiceItem(description = "Item", quantity = 1, unitPrice = 100.0)),
            status = InvoiceStatus.Draft)
        val stats = InvoiceEngine().computeStats(listOf(paid, draft))
        assertEquals(2, stats.totalCount)
        assertEquals(100.0, stats.totalRevenue)
        assertEquals(1, stats.paidCount)
        assertEquals(1, stats.draftCount)
        assertEquals(75.0, stats.averageInvoiceAmount)
    }

    @Test
    fun `generateInvoiceNumber should produce correct format`() {
        val number = InvoiceEngine().generateInvoiceNumber(sequence = 1)
        assertTrue(number.startsWith("INV-2026-"))
        assertTrue(number.endsWith("0001"))
    }

    @Test
    fun `generateInvoiceNumber should pad sequence`() {
        val number = InvoiceEngine().generateInvoiceNumber(sequence = 42)
        assertTrue(number.endsWith("0042"))
    }

    @Test
    fun `validateInvoice should return errors for invalid invoice`() {
        val invoice = Invoice(
            id = "1",
            invoiceNumber = "",
            customerName = "",
            items = listOf(
                InvoiceItem(quantity = 0, unitPrice = -5.0)
            ),
            taxRate = -1.0
        )
        val errors = InvoiceEngine().validateInvoice(invoice)
        assertTrue(errors.isNotEmpty())
        assertTrue(errors.any { it.contains("Customer name") })
        assertTrue(errors.any { it.contains("Invoice number") })
        assertTrue(errors.any { it.contains("quantities") })
        assertTrue(errors.any { it.contains("prices") })
    }

    @Test
    fun `validateInvoice should pass for valid invoice`() {
        val invoice = Invoice(
            id = "1",
            invoiceNumber = "INV-001",
            customerName = "Test Corp",
            items = listOf(
                InvoiceItem(description = "Service", quantity = 1, unitPrice = 100.0)
            ),
            taxRate = 10.0
        )
        val errors = InvoiceEngine().validateInvoice(invoice)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `filterByStatus should filter correctly`() {
        val paid = Invoice(id = "1", invoiceNumber = "I1", customerName = "A", status = InvoiceStatus.Paid)
        val draft = Invoice(id = "2", invoiceNumber = "I2", customerName = "B", status = InvoiceStatus.Draft)
        val paid2 = Invoice(id = "3", invoiceNumber = "I3", customerName = "C", status = InvoiceStatus.Paid)
        val result = InvoiceEngine().filterByStatus(listOf(paid, draft, paid2), InvoiceStatus.Paid)
        assertEquals(2, result.size)
    }

    @Test
    fun `searchInvoices should find by customer name`() {
        val inv1 = Invoice(id = "1", invoiceNumber = "INV-001", customerName = "Acme Corp")
        val inv2 = Invoice(id = "2", invoiceNumber = "INV-002", customerName = "Beta LLC")
        val result = InvoiceEngine().searchInvoices(listOf(inv1, inv2), "acme")
        assertEquals(1, result.size)
        assertEquals("INV-001", result[0].invoiceNumber)
    }

    @Test
    fun `calculateTotals should compute subtotal tax and total`() {
        val items = listOf(
            InvoiceItem(description = "Widget", quantity = 3, unitPrice = 25.0),
            InvoiceItem(description = "Gadget", quantity = 2, unitPrice = 50.0)
        )
        val subtotal = InvoiceEngine().calculateSubtotal(items)
        assertEquals(175.0, subtotal)
        val tax = InvoiceEngine().calculateTax(subtotal, 10.0)
        assertEquals(17.5, tax)
        val total = InvoiceEngine().calculateTotal(subtotal, tax)
        assertEquals(192.5, total)
    }
}
