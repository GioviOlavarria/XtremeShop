package app.movil.parcial2.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.ui.navigation.Rutas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavHostController) {
    val user = remember { app.movil.parcial2.util.sesion.currentUser }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Tienda Extrema") }) }
    ) { p ->
        LazyColumn(Modifier.padding(p).padding(16.dp)) {
            item { Button(onClick = { nav.navigate(Rutas.CATALOG) }) { Text("Catálogo") } }
            item { Button(onClick = { nav.navigate(Rutas.CART) }) { Text("Carrito") } }
            item { Button(onClick = { nav.navigate(Rutas.ABOUT) }) { Text("Quiénes somos") } }
            if (user?.role == Role.ADMIN) {
                item { Button(onClick = { nav.navigate(Rutas.ADMIN) }) { Text("Panel de administración") } }
            }
            item { Button(onClick = {
                app.movil.parcial2.util.sesion.currentUser = null
                nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.HOME) { inclusive = true } }
            }) { Text("Cerrar sesión") } }
        }
    }
}