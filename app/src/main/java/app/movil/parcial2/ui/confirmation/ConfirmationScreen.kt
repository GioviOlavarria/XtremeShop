package app.movil.parcial2.ui.screens.confirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.data.RepositorioPedidosImpl
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.ui.navigation.XtremeScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(nav: NavHostController, orderId: Long) {
    val ctx = LocalContext.current
    val repo = remember { RepositorioPedidosImpl(ctx) }

    val orderNumber = remember { mutableStateOf("") }

    LaunchedEffect(orderId) {
        val pedido = repo.obtenerPedido(orderId)
        orderNumber.value = pedido?.orderNumber ?: "N/A"
    }

    XtremeScaffold(nav = nav, title = "Pedido confirmado", showBack = false) { p ->
        Column(
            modifier = Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("✅ Compra realizada con éxito")
            Text("Número de pedido: ${orderNumber.value}")

            Button(
                onClick = { nav.navigate("${Rutas.ORDER_DETAIL}/$orderId") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ver pedido") }

            Button(
                onClick = { nav.navigate(Rutas.HOME) { popUpTo(Rutas.HOME) { inclusive = true } } },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Volver al inicio") }
        }
    }
}
