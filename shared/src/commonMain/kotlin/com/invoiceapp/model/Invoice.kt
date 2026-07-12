// Invoice.kt — invoice model (KMP port from Dart)
package com.invoiceapp.model

import kotlinx.datetime.Clock
import kotlin.uuid.Uuid

enum class InvoiceStatus(val label: String) {
    Draft("草稿"),
    Sent("已发送"),
    Paid("已付款"),
    Cancelled("已取消")
}

data class InvoiceItem(
    val id: String = Uuid.random().toString(),
    val description: String = "",
    val quantity: Int = 1,
    val unitPrice: Double = 0.0
) {
    val amount: Double get() = quantity * unitPrice
}

data class Invoice(
    val id: String = Uuid.random().toString(),
    var invoiceNumber: String = "",
    var customerName: String = "",
    var customerPhone: String = "",
    var customerEmail: String = "",
    var customerAddress: String = "",
    var issueDate: Long = Clock.System.now().toEpochMilliseconds(),
    var dueDate: Long? = null,
    var items: List<InvoiceItem> = emptyList(),
    var taxRate: Double = 0.0,
    var notes: String = "",
    var status: InvoiceStatus = InvoiceStatus.Draft,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
) {
    val subtotal: Double get() = items.sumOf { it.amount }
    val taxAmount: Double get() = subtotal * taxRate / 100.0
    val total: Double get() = subtotal + taxAmount
}
