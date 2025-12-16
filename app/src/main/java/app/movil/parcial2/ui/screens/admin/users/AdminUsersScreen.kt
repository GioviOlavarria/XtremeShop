package app.movil.parcial2.ui.screens.admin.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.User
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.ui.navigation.XtremeScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(nav: NavHostController) {
    val api = remember { RetrofitClient.instance.create(ApiService::class.java) }
    val users = remember { mutableStateOf<List<User>>(emptyList()) }
    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            users.value = api.getUsers()
        } catch (e: Exception) {
            error.value = "Error cargando usuarios: ${e.message}"
        }
    }

    XtremeScaffold(nav = nav, title = "Admin • Usuarios", showBack = true) { p ->
        Column(modifier = Modifier.padding(p).padding(8.dp)) {
            if (error.value != null) {
                Text(error.value ?: "")
                return@Column
            }
            LazyColumn {
                items(users.value, key = { it.id ?: 0L }) { u ->
                    ListItem(
                        headlineContent = { Text(u.username) },
                        supportingContent = { Text("Rol: ${u.role.name}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { nav.navigate("${Rutas.ADMIN_USER_DETAIL}/${u.id}") }
                    )
                }
            }
        }
    }
}
