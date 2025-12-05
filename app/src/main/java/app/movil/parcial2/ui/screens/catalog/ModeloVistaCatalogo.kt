package app.movil.parcial2.ui.screens.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Producto
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.ui.navigation.XtremeScaffold
import app.movil.parcial2.ui.theme.categoryIconRes
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(nav: NavHostController) {
    val api = remember { RetrofitClient.instance.create(ApiService::class.java) }

    val tabs: List<Pair<String, String?>> = listOf(
        "Todos" to null,
        "Skate" to "SKATE",
        "Roller" to "ROLLER",
        "BMX" to "BMX"
    )

    var selectedTabIndex by remember { mutableStateOf(0) }
    var allProducts by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        loading = true
        error = null
        try {
            allProducts = api.getProducts()
        } catch (e: Exception) {
            error = "Error al cargar productos: ${e.message}"
        }
        loading = false
    }

    val filteredProducts: List<Producto> by remember(allProducts, selectedTabIndex) {
        val cat = tabs[selectedTabIndex].second
        val result = if (cat == null) {
            allProducts
        } else {
            allProducts.filter { it.category == cat }
        }
        mutableStateOf(result)
    }

    XtremeScaffold(
        nav = nav,
        title = "Catálogo",
        showBack = false
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                tabs.forEachIndexed { index, pair ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = { selectedTabIndex = index },
                        text = { Text(pair.first) }
                    )
                }
            }

            when {
                loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
                    }
                }

                filteredProducts.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay productos disponibles")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredProducts, key = { it.id ?: 0L }) { prod ->
                            CatalogItemCard(
                                producto = prod,
                                onClick = { nav.navigate("${Rutas.DETAIL}/${prod.id}") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogItemCard(
    producto: Producto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageModifier = Modifier
                .size(80.dp)

            if (!producto.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = producto.imageUrl,
                    contentDescription = producto.name,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = categoryIconRes(producto.category)),
                    contentDescription = producto.name,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${"%.0f".format(producto.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
