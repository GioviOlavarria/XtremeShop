package app.movil.parcial2.ui.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import app.movil.parcial2.ui.navigation.XtremeScaffold
import app.movil.parcial2.ui.theme.categoryIconRes
import kotlinx.coroutines.launch

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
    val total = remember(items) { items.sumOf { it.unitPrice * it.quantity } }

    XtremeScaffold(
        nav = nav,
        title = "Carrito",
        showBack = true,
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { paddingValues: PaddingValues ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(items, key = { it.rowId }) { item ->
                        CartRowCard(
                            item = item,
                            onInc = {
                                scope.launch { cartDao.updateQuantity(item.rowId, item.quantity + 1) }
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                cartDao.clear()
                                snackbar.showSnackbar("¡Compra realizada con éxito!")
                            }
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
private fun CartRowCard(
    item: EntidadItemCarrito,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = categoryIconRes(null)),
                contentDescription = item.name,
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "x${item.quantity} • $${"%.2f".format(item.unitPrice)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Total: $${"%.2f".format(item.unitPrice * item.quantity)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDec) {
                    Icon(Icons.Filled.Remove, contentDescription = "Disminuir")
                }
                Text(
                    text = "${item.quantity}",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = onInc) {
                    Icon(Icons.Filled.Add, contentDescription = "Aumentar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}
