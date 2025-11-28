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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(nav: NavHostController) {

    val api = remember {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    val tabs: List<Pair<String, String>> = listOf(
        "Skate" to "SKATE",
        "Roller" to "ROLLER",
        "BMX" to "BMX"
    )

    var selectedTabIndex by remember { mutableStateOf(0) }

    var allProducts by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var filteredProducts by remember { mutableStateOf<List<Producto>>(emptyList()) }

    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

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

    LaunchedEffect(allProducts, selectedTabIndex) {
        if (allProducts.isNotEmpty()) {
            val selectedCategory = tabs[selectedTabIndex].second
            filteredProducts = allProducts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        } else {
            filteredProducts = emptyList()
        }
    }

    XtremeScaffold(
        nav = nav,
        title = "Catálogo",
        showBack = true
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, (label, _) ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = { selectedTabIndex = index },
                        text = { Text(label) }
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
                        Text(
                            text = error ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                filteredProducts.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay productos en esta categoría")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredProducts, key = { it.id }) { prod ->
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
private fun CatalogItemCard(
    producto: Producto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = categoryIconRes(producto.category)),
                contentDescription = producto.name,
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(0.2f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${"%.0f".format(producto.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
