package com.invoiceapp.data

import com.invoiceapp.model.Invoice
import com.invoiceapp.model.InvoiceItem
import com.invoiceapp.model.InvoiceStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class InMemoryInvoiceRepositoryTest {

    @Test
    fun `add should persist invoice`() = kotlinx.coroutines.test.runTest {
        val repo = InMemoryInvoiceRepository()
        val invoice = Invoice(id = "1", invoiceNumber = "INV-001", customerName = "Test")
        repo.add(invoice)
        val loaded = repo.getById("1")
        assertNotNull(loaded)
        assertEquals("INV-001", loaded.invoiceNumber)
    }

    @Test
    fun `update should modify existing invoice`() = kotlinx.coroutines.test.runTest {
        val repo = InMemoryInvoiceRepository()
        repo.add(Invoice(id = "1", invoiceNumber = "INV-001", customerName = "Old"))
        repo.update(Invoice(id = "1", invoiceNumber = "INV-001", customerName = "Updated"))
        val loaded = repo.getById("1")
        assertEquals("Updated", loaded?.customerName)
    }

    @Test
    fun `delete should remove invoice`() = kotlinx.coroutines.test.runTest {
        val repo = InMemoryInvoiceRepository()
        repo.add(Invoice(id = "1", invoiceNumber = "INV-001", customerName = "Test"))
        repo.delete("1")
        assertNull(repo.getById("1"))
    }

    @Test
    fun `getAll should return all invoices`() = kotlinx.coroutines.test.runTest {
        val repo = InMemoryInvoiceRepository()
        repo.add(Invoice(id = "1", invoiceNumber = "I1", customerName = "A"))
        repo.add(Invoice(id = "2", invoiceNumber = "I2", customerName = "B"))
        assertEquals(2, repo.getAll().size)
    }

    @Test
    fun `clear should remove all invoices`() = kotlinx.coroutines.test.runTest {
        val repo = InMemoryInvoiceRepository()
        repo.add(Invoice(id = "1", invoiceNumber = "I1", customerName = "A"))
        repo.add(Invoice(id = "2", invoiceNumber = "I2", customerName = "B"))
        repo.clear()
        assertEquals(0, repo.getAll().size)
    }

    @Test
    fun `observeInvoices should emit updates`() = kotlinx.coroutines.test.runTest {
        val repo = InMemoryInvoiceRepository()
        val inv = Invoice(id = "1", invoiceNumber = "I1", customerName = "A")
        repo.add(inv)
        val observed = repo.observeInvoices()
        assertEquals(1, observed.size)
    }
}
