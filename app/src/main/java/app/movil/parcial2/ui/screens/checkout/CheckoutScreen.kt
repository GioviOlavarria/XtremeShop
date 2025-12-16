package app.movil.parcial2.ui.screens.checkout

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import app.movil.parcial2.data.RepositorioPedidosImpl
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.ui.navigation.XtremeScaffold
import app.movil.parcial2.ui.screens.cart.CartViewModel
import app.movil.parcial2.util.VentasSesion
import app.movil.parcial2.util.sesion
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(nav: NavHostController) {
    LaunchedEffect(Unit) {
        if (sesion.currentUser == null) {
            nav.navigate(Rutas.LOGIN)
            return@LaunchedEffect
        }
    }
    val ctx = LocalContext.current

    val cartVm: CartViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory(ctx.applicationContext as Application)
    )

    val items by cartVm.cartItems.collectAsState()
    val total by cartVm.totalPrice.collectAsState()

    val repoPedidos = remember { RepositorioPedidosImpl(ctx) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    XtremeScaffold(nav = nav, title = "Checkout", showBack = true) { p ->
        Column(
            modifier = Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Campos opcionales. Total: $${"%.0f".format(total)}")

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val u = sesion.currentUser ?: return@Button

                    val orderId = runBlocking {
                        repoPedidos.crearPedidoDesdeCarrito(
                            username = u.username,
                            userId = u.id,
                            items = items,
                            customerName = name.trim().takeIf { it.isNotBlank() },
                            customerEmail = email.trim().takeIf { it.isNotBlank() },
                            customerPhone = phone.trim().takeIf { it.isNotBlank() },
                            address = address.trim().takeIf { it.isNotBlank() },
                            notes = notes.trim().takeIf { it.isNotBlank() }
                        )
                    }

                    VentasSesion.registrarVenta(total, items.sumOf { it.quantity })
                    runBlocking { cartVm.clearCart() }

                    nav.navigate("${Rutas.CONFIRMATION}/$orderId") {
                        popUpTo(Rutas.CART) { inclusive = true }
                    }
                },
                enabled = items.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar compra")
            }
        }
    }
}
