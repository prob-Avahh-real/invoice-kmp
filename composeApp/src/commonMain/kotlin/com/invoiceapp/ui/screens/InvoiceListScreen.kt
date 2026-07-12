// InvoiceListScreen.kt — invoice management list + stats
package com.invoiceapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.invoiceapp.model.Invoice
import com.invoiceapp.model.InvoiceStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InvoiceListScreen() {
    // TODO: load from SQLDelight
    val invoices = remember { mutableStateListOf<Invoice>() }
    var showNewInvoice by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showNewInvoice = true }) {
                Icon(Icons.Default, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            // Summary bar
            val total = invoices.sumOf { it.total }
            val draftCount = invoices.count { it.status == InvoiceStatus.Draft }
            Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primaryContainer) {
                Row(
                    Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatBadge("总金额", "¥${"%.2f".format(total)}")
                    StatBadge("发票数", "${invoices.size}")
                    StatBadge("草稿", "$draftCount")
                }
            }

            LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
                items(invoices.sortedByDescending { it.createdAt }) { invoice ->
                    InvoiceCard(invoice)
                }
            }
        }
    }
}

@Composable
fun InvoiceCard(invoice: Invoice) {
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    val statusColor = when (invoice.status) {
        InvoiceStatus.Draft -> Color.Gray
        InvoiceStatus.Sent -> Color(0xFF2196F3)
        InvoiceStatus.Paid -> Color(0xFF4CAF50)
        InvoiceStatus.Cancelled -> Color(0xFFF44336)
    }

    Card(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable {},
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(invoice.invoiceNumber, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(invoice.customerName, color = Color.Gray, fontSize = 14.sp)
                if (invoice.items.isNotEmpty()) {
                    Text("${invoice.items.size} 项 · ${"%.2f".format(invoice.total)}",
                        color = Color.Gray, fontSize = 12.sp)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("¥${"%.2f".format(invoice.total)}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Surface(color = statusColor.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                    Text(invoice.status.label, color = statusColor, fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
                Spacer(Modifier.height(4.dp))
                Text(df.format(Date(invoice.createdAt)), fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun StatBadge(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}
