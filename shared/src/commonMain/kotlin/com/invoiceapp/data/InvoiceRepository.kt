package com.invoiceapp.data

import com.invoiceapp.model.Invoice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface IInvoiceRepository {
    fun observeInvoices(): Flow<List<Invoice>>
    suspend fun add(invoice: Invoice)
    suspend fun update(invoice: Invoice)
    suspend fun delete(id: String)
    suspend fun getById(id: String): Invoice?
    suspend fun getAll(): List<Invoice>
    suspend fun clear()
}

class InMemoryInvoiceRepository : IInvoiceRepository {
    private val mutex = Mutex()
    private val _invoices = MutableStateFlow<List<Invoice>>(emptyList())

    override fun observeInvoices(): Flow<List<Invoice>> = _invoices.asStateFlow()

    override suspend fun add(invoice: Invoice) = mutex.withLock {
        _invoices.value = _invoices.value + invoice
    }

    override suspend fun update(invoice: Invoice) = mutex.withLock {
        _invoices.value = _invoices.value.map { if (it.id == invoice.id) invoice else it }
    }

    override suspend fun delete(id: String) = mutex.withLock {
        _invoices.value = _invoices.value.filter { it.id != id }
    }

    override suspend fun getById(id: String): Invoice? = mutex.withLock {
        _invoices.value.find { it.id == id }
    }

    override suspend fun getAll(): List<Invoice> = mutex.withLock { _invoices.value }

    override suspend fun clear() = mutex.withLock { _invoices.value = emptyList() }
}
