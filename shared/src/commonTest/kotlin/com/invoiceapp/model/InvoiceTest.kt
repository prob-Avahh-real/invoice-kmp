package com.invoiceapp.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InvoiceTest {

    @Test
    fun `InvoiceItem amount should be quantity times unitPrice`() {
        val item = InvoiceItem(description = "Widget", quantity = 3, unitPrice = 25.0)
        assertEquals(75.0, item.amount)
    }

    @Test
    fun `InvoiceItem with zero quantity should have zero amount`() {
        val item = InvoiceItem(description = "Free", quantity = 0, unitPrice = 100.0)
        assertEquals(0.0, item.amount)
    }

    @Test
    fun `Invoice total should include tax`() {
        val invoice = Invoice(
            id = "1",
            invoiceNumber = "INV-001",
            customerName = "Test",
            items = listOf(
                InvoiceItem(description = "Service", quantity = 2, unitPrice = 100.0)
            ),
            taxRate = 10.0
        )
        assertEquals(200.0, invoice.subtotal)
        assertEquals(20.0, invoice.taxAmount)
        assertEquals(220.0, invoice.total)
    }

    @Test
    fun `Invoice with zero tax should have total equal subtotal`() {
        val invoice = Invoice(
            id = "1",
            invoiceNumber = "INV-002",
            customerName = "Test",
            items = listOf(
                InvoiceItem(description = "Item", quantity = 1, unitPrice = 50.0)
            ),
            taxRate = 0.0
        )
        assertEquals(50.0, invoice.subtotal)
        assertEquals(0.0, invoice.taxAmount)
        assertEquals(50.0, invoice.total)
    }

    @Test
    fun `InvoiceStatus labels should be non-empty`() {
        InvoiceStatus.entries.forEach { status ->
            assertTrue(status.label.isNotEmpty())
        }
    }

    @Test
    fun `Invoice should have unique ID`() {
        val inv1 = Invoice(invoiceNumber = "I1", customerName = "A")
        val inv2 = Invoice(invoiceNumber = "I2", customerName = "B")
        assertTrue(inv1.id != inv2.id)
    }
}
