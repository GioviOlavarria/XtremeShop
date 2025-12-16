package app.movil.parcial2.ui.screens.admin.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.domain.model.User
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.ui.navigation.XtremeScaffold
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserDetailScreen(nav: NavHostController, userId: Long) {

    val api = remember { RetrofitClient.instance.create(ApiService::class.java) }

    var loadedUser by remember { mutableStateOf<User?>(null) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var roleText by remember { mutableStateOf("USER") }

    var msg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        try {
            val u = api.getUserById(userId)
            loadedUser = u
            username = u.username
            password = u.password
            roleText = u.role.name
        } catch (e: Exception) {
            msg = "Error cargando usuario: ${e.message}"
        }
    }

    XtremeScaffold(nav = nav, title = "Detalle usuario", showBack = true) { p ->
        Column(
            modifier = Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            msg?.let { Text(it) }

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = roleText,
                onValueChange = { roleText = it },
                label = { Text("Rol (ADMIN/USER)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val base = loadedUser ?: return@Button
                    val newRole = if (roleText.trim().uppercase() == "ADMIN") Role.ADMIN else Role.USER

                    runBlocking {
                        api.updateUser(
                            userId,
                            base.copy(
                                username = username.trim(),
                                password = password.trim(),
                                role = newRole
                            )
                        )
                    }
                    nav.popBackStack()
                },
                enabled = loadedUser != null && username.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar cambios") }

            Button(
                onClick = {
                    runBlocking { api.deleteUser(userId) }
                    nav.popBackStack()
                },
                enabled = loadedUser != null,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Eliminar usuario") }
        }
    }
}
