package app.movil.parcial2.ui.screens.cart

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.ui.navigation.XtremeScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    nav: NavHostController,
    vm: CartViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val items by vm.cartItems.collectAsState()
    val total by vm.totalPrice.collectAsState()

    XtremeScaffold(
        nav = nav,
        title = "Carrito",
        showBack = true
    ) { p ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(p)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(p)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(items, key = { it.rowId }) { item ->
                        CartRow(
                            item = item,
                            onInc = { vm.incrementItem(item) },
                            onDec = { vm.decrementItem(item) },
                            onDelete = { vm.deleteItem(item) }
                        )
                        Divider()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Total: $${"%.2f".format(total)}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { nav.navigate(Rutas.PAYMENT) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = items.isNotEmpty()
                    ) {
                        Text("Finalizar compra")
                    }
                }
            }
        }
    }
}

@Composable
private fun CartRow(
    item: EntidadItemCarrito,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        headlineContent = { Text(item.name) },
        supportingContent = {
            Text(
                "x${item.quantity} • $${"%.2f".format(item.unitPrice)} = " +
                        "$${"%.2f".format(item.unitPrice * item.quantity)}"
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDec) { Icon(Icons.Filled.Remove, contentDescription = "Disminuir") }
                Text("${item.quantity}", modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = onInc) { Icon(Icons.Filled.Add, contentDescription = "Aumentar") }
                Spacer(Modifier.width(12.dp))
                IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
            }
        }
    )
}
