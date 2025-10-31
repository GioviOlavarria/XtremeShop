package app.movil.parcial2.ui.screens.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.entidades.EntidadProducto
import app.movil.parcial2.ui.navigation.Rutas
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(nav: NavHostController) {
    val ctx = LocalContext.current
    val db = remember { BaseDeDatos.get(ctx.applicationContext) }
    val productoDao = remember { db.productoDao() }


    val tabs = listOf("Skate" to "SKATE", "Roller" to "ROLLER", "BMX" to "BMX")
    var selected by remember { mutableStateOf(0) }

    var products by remember { mutableStateOf<List<EntidadProducto>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(selected) {
        error = null
        products = emptyList()
        try {
            productoDao.observeByCategory(tabs[selected].second).collectLatest { list ->
                products = list
            }
        } catch (t: Throwable) {
            error = t.message ?: t.toString()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { p ->
        Column(Modifier.padding(p).fillMaxSize()) {

            TabRow(selectedTabIndex = selected) {
                tabs.forEachIndexed { i, (label, _) ->
                    Tab(
                        selected = i == selected,
                        onClick = { selected = i },
                        text = { Text(label) }
                    )
                }
            }

            when {
                error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: $error", color = MaterialTheme.colorScheme.error)
                    }
                }
                products.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay productos")
                    }
                }
                else -> {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(products, key = { it.id }) { prod ->
                            ListItem(
                                headlineContent = { Text(prod.name) },
                                supportingContent = { Text("$${"%.2f".format(prod.price)}") },
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
