package app.movil.parcial2.ui.screens.catalog

import app.movil.parcial2.domain.model.Producto

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.entidades.EntidadProducto
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.ui.navigation.Rutas
import kotlinx.coroutines.flow.collectLatest

import retrofit2.Call
import retrofit2.http.GET

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(nav: NavHostController) {

    val api = remember {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    // --- START: Corrected Logic ---

    val tabs = listOf("Skate" to "SKATE", "Roller" to "ROLLER", "BMX" to "BMX")
    var selectedTabIndex by remember { mutableStateOf(0) }

    // State for the complete list of products fetched from the API
    var allProducts by remember { mutableStateOf<List<Producto>>(emptyList()) }
    // State for only the products visible in the selected tab
    var filteredProducts by remember { mutableStateOf<List<Producto>>(emptyList()) }

    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) } // Start loading immediately

    // 1. Fetch all products ONCE when the screen first launches
    LaunchedEffect(Unit) { // `Unit` makes this run only once
        loading = true
        error = null
        try {
            allProducts = api.getProducts()
        } catch (e: Exception) {
            error = "Failed to load products: ${e.message}"
        }
        loading = false
    }

    // 2. Re-filter the list whenever the fetched products or the selected tab change
    LaunchedEffect(allProducts, selectedTabIndex) {
        if (allProducts.isNotEmpty()) {
            val selectedCategory = tabs[selectedTabIndex].second
            filteredProducts = allProducts.filter { it.category == selectedCategory }
        }
    }

    // --- END: Corrected Logic ---

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // Tabs
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { i, (label, _) ->
                    Tab(
                        selected = i == selectedTabIndex,
                        onClick = { selectedTabIndex = i }, // Update the selected tab index
                        text = { Text(label) }
                    )
                }
            }

            when {
                loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: $error", color = MaterialTheme.colorScheme.error)
                    }
                }

                // Use the filtered list for the UI
                filteredProducts.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay productos en esta categoría") // More specific message
                    }
                }

                else -> {
                    // Use the filtered list for the LazyColumn
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(filteredProducts, key = { it.id }) { prod ->
                            ListItem(
                                headlineContent = { Text(prod.name) },
                                supportingContent = { Text("$${prod.price}") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { nav.navigate("${Rutas.DETAIL}/${prod.id}") }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

