package app.movil.parcial2.ui.screens.admin.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.entidades.EntidadCategoria
import app.movil.parcial2.ui.navigation.XtremeScaffold
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCategoriesScreen(nav: NavHostController) {
    val ctx = LocalContext.current
    val dao = remember { BaseDeDatos.get(ctx).categoriaDao() }
    val cats by dao.observarCategorias().collectAsState(initial = emptyList())

    val (newCat, setNewCat) = remember { mutableStateOf("") }

    XtremeScaffold(nav = nav, title = "Admin • Categorías", showBack = true) { p ->
        Column(
            modifier = Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = newCat,
                onValueChange = setNewCat,
                label = { Text("Nueva categoría (ej: SKATE)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    val v = newCat.trim()
                    if (v.isNotBlank()) {
                        runBlocking { dao.insertar(EntidadCategoria(nombre = v.uppercase())) }
                        setNewCat("")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Agregar") }

            LazyColumn {
                items(cats, key = { it.id }) { c ->
                    ListItem(
                        headlineContent = { Text(c.nombre) },
                        trailingContent = {
                            Button(onClick = { runBlocking { dao.eliminar(c) } }) { Text("Eliminar") }
                        }
                    )
                }
            }
        }
    }
}
