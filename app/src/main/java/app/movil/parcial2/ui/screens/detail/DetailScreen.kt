package app.movil.parcial2.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch


import androidx.compose.ui.platform.LocalContext
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import app.movil.parcial2.data.local.entidades.EntidadProducto
import app.movil.parcial2.data.RepositorioProductoImpl
import app.movil.parcial2.data.RepositorioCarritoImpl

@Composable
fun DetailScreen(nav: NavHostController, productId: Long) {
    val ctx = LocalContext.current
    val db = remember { BaseDeDatos.get(ctx) }
    val productDao = remember { db.productoDao() }
    val cartRepo = remember { RepositorioCarritoImpl(db.carritoDao()) }
    var product by remember { mutableStateOf<EntidadProducto?>(null) }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(productId) { product = productDao.getById(productId)?.let {
        EntidadProducto(it.id, it.name, it.price, it.description, it.category)
    } }

    Scaffold(
        topBar = { TopAppBar(title = { Text(product?.name ?: "Detalle") }) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { p ->
        product?.let { prod ->
            Column(Modifier.padding(p).padding(16.dp)) {
                Text(prod.description)
                Text("Precio: $${prod.price}", style = MaterialTheme.typography.titleMedium)
                Button(onClick = {
                    scope.launch {
                        cartRepo.addOrInc(prod.id, prod.name, prod.price)
                        snackbar.showSnackbar("Producto agregado al carrito")
                    }
                }, modifier = Modifier.padding(top = 12.dp)) { Text("Agregar al carrito") }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}