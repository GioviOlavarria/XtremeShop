package app.movil.parcial2.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Category
import app.movil.parcial2.domain.model.Producto
import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.ui.navigation.XtremeScaffold
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
    var categoria by remember { mutableStateOf(Category.SKATE.name) }
    var imageUrl by remember { mutableStateOf("") }

    var catExpanded by remember { mutableStateOf(false) }
    val categorias = remember { Category.values().map { it.name } }

    fun limpiar() {
        id = ""
        nombre = ""
        precio = ""
        descripcion = ""
        categoria = Category.SKATE.name
        imageUrl = ""
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

    XtremeScaffold(
        nav = nav,
        title = "Panel de administración",
        showBack = true,
        snackbarHost = { SnackbarHost(snackbar) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Datos del producto")

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID (solo para editar/eliminar)") },
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
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de imagen") },
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    label = { Text("Categoría") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { catExpanded = !catExpanded }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { catExpanded = true }
                )

                DropdownMenu(
                    expanded = catExpanded,
                    onDismissRequest = { catExpanded = false }
                ) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                categoria = cat
                                catExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val pprice = precio.toDoubleOrNull()
                                    ?: throw IllegalArgumentException("Precio inválido")
                                if (nombre.isBlank() || descripcion.isBlank()) {
                                    throw IllegalArgumentException("Campos vacíos")
                                }

                                val nuevo = Producto(
                                    id = null,
                                    name = nombre.trim(),
                                    price = pprice,
                                    description = descripcion.trim(),
                                    category = categoria,
                                    imageUrl = imageUrl.trim().ifBlank { null }
                                )
                                api.createProduct(nuevo)
                                snackbar.showSnackbar("Producto creado")
                                limpiar()
                                refrescar()
                            } catch (e: Exception) {
                                snackbar.showSnackbar("Error: ${e.message}")
                            }
                        }
                    }
                ) {
                    Text("Crear")
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val pid = id.toLongOrNull()
                                    ?: throw IllegalArgumentException("ID inválido")
                                val pprice = precio.toDoubleOrNull()
                                    ?: throw IllegalArgumentException("Precio inválido")
                                if (nombre.isBlank() || descripcion.isBlank()) {
                                    throw IllegalArgumentException("Campos vacíos")
                                }

                                val actualizado = Producto(
                                    id = pid,
                                    name = nombre.trim(),
                                    price = pprice,
                                    description = descripcion.trim(),
                                    category = categoria,
                                    imageUrl = imageUrl.trim().ifBlank { null }
                                )
                                api.updateProduct(pid, actualizado)
                                snackbar.showSnackbar("Producto actualizado")
                                limpiar()
                                refrescar()
                            } catch (e: Exception) {
                                snackbar.showSnackbar("Error: ${e.message}")
                            }
                        }
                    }
                ) {
                    Text("Modificar")
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val pid = id.toLongOrNull()
                                ?: throw IllegalArgumentException("ID inválido")
                            api.deleteProduct(pid)
                            snackbar.showSnackbar("Producto eliminado")
                            limpiar()
                            refrescar()
                        } catch (e: Exception) {
                            snackbar.showSnackbar("Error: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar por ID")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Productos actuales")

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos, key = { it.id ?: 0L }) { pdt ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                id = (pdt.id ?: 0L).toString()
                                nombre = pdt.name
                                precio = pdt.price.toString()
                                descripcion = pdt.description
                                categoria = pdt.category
                                imageUrl = pdt.imageUrl ?: ""
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(pdt.name)
                            Text(
                                "$${"%.0f".format(pdt.price)} - ${pdt.category}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        val pid = pdt.id
                                            ?: throw IllegalArgumentException("ID inválido")
                                        api.deleteProduct(pid)
                                        snackbar.showSnackbar("Producto eliminado")
                                        if (id == pid.toString()) {
                                            limpiar()
                                        }
                                        refrescar()
                                    } catch (e: Exception) {
                                        snackbar.showSnackbar("Error: ${e.message}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar"
                            )
                        }
                    }
                }
            }
        }
    }
}
