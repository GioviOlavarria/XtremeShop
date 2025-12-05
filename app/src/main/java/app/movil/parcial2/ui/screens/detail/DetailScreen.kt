package app.movil.parcial2.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.data.RepositorioCarritoImpl
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.domain.model.Producto
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.ui.navigation.XtremeScaffold
import app.movil.parcial2.ui.theme.categoryIconRes
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.runtime.rememberCoroutineScope
import app.movil.parcial2.ui.navigation.Rutas
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(nav: NavHostController, productId: Long) {
    val ctx = LocalContext.current
    val db = remember { BaseDeDatos.get(ctx) }
    val cartRepo = remember { RepositorioCarritoImpl(db.carritoDao()) }
    val api = remember { RetrofitClient.instance.create(ApiService::class.java) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var product by remember { mutableStateOf<Producto?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(productId) {
        loading = true
        error = null
        try {
            product = api.getProductById(productId)
        } catch (e: Exception) {
            error = "Error al cargar el producto: ${e.message}"
        }
        loading = false
    }

    XtremeScaffold(
        nav = nav,
        title = product?.name ?: "Detalle",
        showBack = true,
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                loading -> {
                    CircularProgressIndicator()
                }

                error != null -> {
                    Text(
                        text = error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                product == null -> {
                    Text("Producto no encontrado")
                }

                else -> {
                    val prod = product!!
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val imageModifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()

                        if (!prod.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = prod.imageUrl,
                                contentDescription = prod.name,
                                modifier = imageModifier,
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = categoryIconRes(prod.category)),
                                contentDescription = prod.name,
                                modifier = imageModifier,
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = prod.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = prod.description,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Precio: $${"%.0f".format(prod.price)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    cartRepo.addOrInc(prod.id!!, prod.name, prod.price)
                                    snackbar.showSnackbar("Producto agregado al carrito")

                                    nav.navigate(Rutas.HOME) {
                                        // Limpia la pila de navegación para que el usuario no pueda
                                        // volver a la pantalla de detalles con el botón de retroceso.
                                        popUpTo(Rutas.HOME) {
                                            inclusive = true
                                        }
                                        // Evita crear una nueva instancia del Home si ya existe.
                                        launchSingleTop = true
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Agregar al carrito")
                        }
                    }
                }
            }
        }
    }
}
