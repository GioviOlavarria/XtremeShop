package app.movil.parcial2.ui.screens.admin.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun AdminOrdersScreen(nav: NavHostController) {
    val ctx = LocalContext.current
    val repo = remember { RepositorioPedidosImpl(ctx) }
    val pedidos by repo.observarPedidos().collectAsState(initial = emptyList())

    XtremeScaffold(nav = nav, title = "Admin • Pedidos", showBack = true) { p ->
        Column(modifier = Modifier.padding(p).padding(8.dp)) {
            if (pedidos.isEmpty()) {
                Text("No hay pedidos aún.")
            } else {
                LazyColumn {
                    items(pedidos, key = { it.id }) { pedido ->
                        ListItem(
                            headlineContent = { Text("Pedido ${pedido.orderNumber}") },
                            supportingContent = { Text("Cliente: ${pedido.username} • Estado: ${pedido.status}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { nav.navigate("${Rutas.ADMIN_ORDER_DETAIL}/${pedido.id}") }
                        )
                    }
                }
            }
        }
    }
}
