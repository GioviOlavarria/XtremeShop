package app.movil.parcial2.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import app.movil.parcial2.util.sesion
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Producto
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.ui.navigation.XtremeScaffold
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavHostController) {
    val api = remember { RetrofitClient.instance.create(ApiService::class.java) }
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            productos = api.getProducts()
        } catch (e: Exception) {
            error = "No se pudieron cargar productos: ${e.message}"
        }
    }

    val mejores = remember(productos) {
        productos
            .sortedByDescending { it.price ?: 0.0 }
            .take(3)
    }

    XtremeScaffold(nav = nav, title = "XtremeShop", showBack = false) { p ->
        Column(
            modifier = Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            if (sesion.currentUser == null) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { nav.navigate(Rutas.LOGIN) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Iniciar sesión")
                    }
                    Spacer(Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = { nav.navigate(Rutas.REGISTER) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Registrarse")
                    }
                }
            }


            Text("Mejores productos", style = MaterialTheme.typography.titleLarge)

            if (error != null) {
                Text(error ?: "", color = MaterialTheme.colorScheme.error)
            }

            if (mejores.isEmpty()) {
                Text("Cargando...")
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(mejores, key = { it.id ?: 0L }) { pr ->
                        MejorProductoCard(
                            producto = pr,
                            onClick = {
                                val id = pr.id ?: 0L
                                nav.navigate("${Rutas.DETAIL}/$id")
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))


            Button(
                onClick = { nav.navigate(Rutas.CATALOG) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Catálogo")
            }


            OutlinedButton(
                onClick = { nav.navigate(Rutas.ABOUT) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Conocer más / Quiénes somos")
            }
        }
    }
}

@Composable
private fun MejorProductoCard(
    producto: Producto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(240.dp)
            .height(220.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val img = producto.imageUrl ?: ""
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(img)
                    .crossfade(true)
                    .build(),
                contentDescription = producto.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Text(
                text = producto.name ?: "Producto",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${"%.0f".format(producto.price ?: 0.0)}",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
