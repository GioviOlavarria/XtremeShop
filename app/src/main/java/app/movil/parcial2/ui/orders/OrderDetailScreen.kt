package app.movil.parcial2.ui.screens.orders

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
import app.movil.parcial2.data.local.entidades.EntidadPedido
import app.movil.parcial2.data.local.entidades.EntidadPedidoItem
import app.movil.parcial2.ui.navigation.XtremeScaffold
import app.movil.parcial2.util.BoletaUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(nav: NavHostController, orderId: Long) {
    val ctx = LocalContext.current
    val repo = remember { RepositorioPedidosImpl(ctx) }

    val pedido = remember { mutableStateOf<EntidadPedido?>(null) }
    val items = remember { mutableStateOf<List<EntidadPedidoItem>>(emptyList()) }
    val boleta = remember { mutableStateOf("") }

    LaunchedEffect(orderId) {
        val p = repo.obtenerPedido(orderId)
        val itx = repo.obtenerItems(orderId)
        pedido.value = p
        items.value = itx
        if (p != null) boleta.value = BoletaUtil.generarBoletaTexto(p, itx)
    }

    XtremeScaffold(nav = nav, title = "Detalle del pedido", showBack = true) { p ->
        Column(
            modifier = Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val pdd = pedido.value
            if (pdd == null) {
                Text("Cargando pedido...")
                return@Column
            }

            Text("Pedido: ${pdd.orderNumber}")
            Text("Estado: ${pdd.status}")
            Text("Total: $${"%.0f".format(pdd.total)}")

            Text("---- BOLETA ----")
            Text(boleta.value)

            Button(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Volver") }
        }
    }
}
