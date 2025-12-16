package app.movil.parcial2.ui.screens.payment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.util.VentasSesion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
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
    val totalItems = remember(items) {
        items.sumOf { it.quantity }
    }

    var nombreTitular by remember { mutableStateOf("") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var fechaExpiracion by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .fillMaxSize()
                .padding(16.dp)
        ) {


            Text(
                text = "Resumen de compra",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            if (items.isEmpty()) {
                Text("No hay productos en el carrito.")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(items, key = { it.rowId }) { item ->
                        PaymentItemRow(item)
                        Divider()
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Total: $${"%.2f".format(total)}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))


            Text(
                text = "Datos de pago (ficticios)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = nombreTitular,
                onValueChange = { nombreTitular = it },
                label = { Text("Nombre del titular") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = numeroTarjeta,
                onValueChange = { numeroTarjeta = it },
                label = { Text("Número de tarjeta") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = fechaExpiracion,
                    onValueChange = { fechaExpiracion = it },
                    label = { Text("Fecha expiración (MM/AA)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        VentasSesion.registrarVenta(total, totalItems)

                        cartDao.clear()
                        snackbar.showSnackbar("Pago simulado con éxito. ¡Gracias por tu compra!")
                        
                        // Navegar a la pantalla de inicio
                        nav.navigate(Rutas.HOME) {
                            popUpTo(Rutas.HOME) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = items.isNotEmpty()
            ) {
                Text("Pagar ahora")
            }
        }
    }
}

@Composable
private fun PaymentItemRow(item: EntidadItemCarrito) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = item.name, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "x${item.quantity}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "$${"%.2f".format(item.unitPrice * item.quantity)}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
