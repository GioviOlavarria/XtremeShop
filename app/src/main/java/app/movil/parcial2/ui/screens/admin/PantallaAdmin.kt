package app.movil.parcial2.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Category
import app.movil.parcial2.domain.model.Producto
import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.util.sesion
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdmin(nav: NavHostController) {

    val user = remember { sesion.currentUser }
    if (user?.role != Role.ADMIN) {
        LaunchedEffect(Unit) { nav.popBackStack() }
        return
    }

    val api = remember { RetrofitClient.instance.create(ApiService::class.java) }
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("SKATE") }

    fun limpiar() {
        id = ""; nombre = ""; precio = ""; descripcion = ""; categoria = "SKATE"
    }

    fun refrescar() {
        scope.launch {
            try {
                productos = api.getProducts()
            } catch (e: Exception) {
                snackbar.showSnackbar("Error al refrescar: ${e.message}")
            }
        }
    }

    LaunchedEffect(Unit) {
        refrescar()
    }

    var catExpanded by remember { mutableStateOf(false) }
    val categorias = remember { Category.values().toList() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel de administración", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text("Crear / Modificar producto", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = id,
                onValueChange = { id = it.filter { ch -> ch.isDigit() } },
                label = { Text("ID (numérico y único)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it.filter { ch -> ch.isDigit() || ch == '.' } },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )


            ExposedDropdownMenuBox(
                expanded = catExpanded,
                onExpandedChange = { catExpanded = !catExpanded }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(catExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = catExpanded,
                    onDismissRequest = { catExpanded = false }
                ) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name) },
                            onClick = {
                                categoria = cat.name
                                catExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    scope.launch {
                        try {
                            val pprice = precio.toDoubleOrNull() ?: throw IllegalArgumentException("Precio inválido")
                            if (nombre.isBlank() || descripcion.isBlank()) throw IllegalArgumentException("Campos vacíos")

                            api.createProduct(Producto(id = null, name = nombre.trim(), price = pprice, description = descripcion.trim(), category = categoria))
                            snackbar.showSnackbar("Producto creado")
                            limpiar()
                            refrescar()
                        } catch (e: Exception) {
                            snackbar.showSnackbar("Error: ${e.message}")
                        }
                    }
                }) { Text("Crear") }

                Spacer(Modifier.width(8.dp))

                Button(onClick = {
                    scope.launch {
                        try {
                            val pid = id.toLongOrNull() ?: throw IllegalArgumentException("ID inválido")
                            val pprice = precio.toDoubleOrNull() ?: throw IllegalArgumentException("Precio inválido")
                            if (nombre.isBlank() || descripcion.isBlank()) throw IllegalArgumentException("Campos vacíos")

                            api.updateProduct(pid, Producto(pid, nombre.trim(), pprice, descripcion.trim(), categoria))
                            snackbar.showSnackbar("Producto actualizado")
                            limpiar()
                            refrescar()
                        } catch (e: Exception) {
                            snackbar.showSnackbar("Error: ${e.message}")
                        }
                    }
                }) { Text("Modificar") }

                Spacer(Modifier.width(8.dp))

                Button(onClick = {
                    scope.launch {
                        try {
                            val pid = id.toLongOrNull() ?: throw IllegalArgumentException("ID inválido")
                            api.deleteProduct(pid)
                            snackbar.showSnackbar("Producto eliminado")
                            limpiar()
                            refrescar()
                        } catch (e: Exception) {
                            snackbar.showSnackbar("Error: ${e.message}")
                        }
                    }
                }) { Text("Eliminar por ID") }
            }

            Spacer(Modifier.height(16.dp))

            Text("Listado", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(productos, key = { it.id!! }) { pdt ->
                    ListItem(
                        headlineContent = { Text(pdt.name) },
                        supportingContent = { Text("${pdt.category} • $${"%.2f".format(pdt.price)}") },
                        trailingContent = {
                            IconButton(onClick = {
                                scope.launch {
                                    try {
                                        api.deleteProduct(pdt.id!!)
                                        snackbar.showSnackbar("Producto eliminado")
                                        refrescar()
                                    } catch (e: Exception) {
                                        snackbar.showSnackbar("Error: ${e.message}")
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                id = pdt.id!!.toString()
                                nombre = pdt.name
                                precio = pdt.price.toString()
                                descripcion = pdt.description
                                categoria = pdt.category
                            }
                    )
                    Divider()
                }
            }
        }
    }
}
