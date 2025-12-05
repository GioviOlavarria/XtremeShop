package app.movil.parcial2.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.ui.navigation.XtremeScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    nav: NavHostController
) {
    val ctx = LocalContext.current
    val db = remember { BaseDeDatos.get(ctx.applicationContext) }
    val cartDao = remember { db.carritoDao() }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    val items by cartDao.observeCart().collectAsState(initial = emptyList())
    val total = remember(items) {
        items.sumOf { it.unitPrice * it.quantity }
    }

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
                            onInc = {
                                scope.launch {
                                    cartDao.updateQuantity(item.rowId, item.quantity + 1)
                                }
                            },
                            onDec = {
                                scope.launch {
                                    val newQty = item.quantity - 1
                                    if (newQty <= 0) {
                                        cartDao.deleteById(item.rowId)
                                        snackbar.showSnackbar("Producto eliminado del carrito")
                                    } else {
                                        cartDao.updateQuantity(item.rowId, newQty)
                                    }
                                }
                            },
                            onDelete = {
                                scope.launch {
                                    cartDao.deleteById(item.rowId)
                                    snackbar.showSnackbar("Producto eliminado del carrito")
                                }
                            }
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
                        onClick = {

                            nav.navigate(Rutas.PAYMENT)
                        },
                        modifier = Modifier.fillMaxWidth()
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
