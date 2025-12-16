package app.movil.parcial2.ui.screens.admin.orders

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.data.RepositorioPedidosImpl
import app.movil.parcial2.domain.model.EstadoPedido
import app.movil.parcial2.ui.navigation.XtremeScaffold
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderDetailScreen(nav: NavHostController, orderId: Long) {
    val ctx = LocalContext.current
    val repo = remember { RepositorioPedidosImpl(ctx) }

    val orderNumber = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val total = remember { mutableStateOf(0.0) }
    val estado = remember { mutableStateOf(EstadoPedido.NUEVO) }

    LaunchedEffect(orderId) {
        val p = repo.obtenerPedido(orderId)
        if (p != null) {
            orderNumber.value = p.orderNumber
            username.value = p.username
            total.value = p.total
            estado.value = runCatching { EstadoPedido.valueOf(p.status) }.getOrDefault(EstadoPedido.NUEVO)
        }
    }

    XtremeScaffold(nav = nav, title = "Admin • Detalle pedido", showBack = true) { p ->
        Column(
            modifier = Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Pedido: ${orderNumber.value}")
            Text("Cliente: ${username.value}")
            Text("Total: $${"%.0f".format(total.value)}")

            OutlinedTextField(
                value = estado.value.name,
                onValueChange = { s ->
                    estado.value = when (s.uppercase().trim()) {
                        "EN_PROCESO" -> EstadoPedido.EN_PROCESO
                        "COMPLETADO" -> EstadoPedido.COMPLETADO
                        "CANCELADO" -> EstadoPedido.CANCELADO
                        else -> EstadoPedido.NUEVO
                    }
                },
                label = { Text("Estado (NUEVO/EN_PROCESO/COMPLETADO/CANCELADO)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    runBlocking { repo.actualizarEstado(orderId, estado.value) }
                    nav.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar estado") }
        }
    }
}
