// App.kt — invoice-app entry point
package com.invoiceapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import com.invoiceapp.ui.screens.InvoiceListScreen

@Composable
fun App() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        InvoiceListScreen()
    }
}
