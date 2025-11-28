package app.movil.parcial2.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.data.RepositorioCarritoImpl
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.entidades.EntidadProducto
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.ui.navigation.XtremeScaffold
import app.movil.parcial2.ui.theme.categoryIconRes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(nav: NavHostController, productId: Long) {
    val ctx = LocalContext.current
    val db = remember { BaseDeDatos.get(ctx) }
    val cartRepo = remember { RepositorioCarritoImpl(db.carritoDao()) }

    var product by remember { mutableStateOf<EntidadProducto?>(null) }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    val api = remember {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    LaunchedEffect(productId) {
        scope.launch {
            try {
                val fetchedProduct = api.getProductById(productId)
                product = EntidadProducto(
                    id = fetchedProduct.id,
                    name = fetchedProduct.name,
                    price = fetchedProduct.price,
                    description = fetchedProduct.description,
                    category = fetchedProduct.category
                )
            } catch (e: Exception) {
                snackbar.showSnackbar("Error al cargar el producto: ${e.message}")
            }
        }
    }

    XtremeScaffold(
        nav = nav,
        title = product?.name ?: "Detalle",
        showBack = true,
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { paddingValues ->
        val prod = product
        if (prod == null) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = categoryIconRes(prod.category)),
                            contentDescription = prod.name,
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )

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

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    cartRepo.addOrInc(prod.id, prod.name, prod.price)
                                    snackbar.showSnackbar("Producto agregado al carrito")
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
