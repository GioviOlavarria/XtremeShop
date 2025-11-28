// Create a new file: app/movil/parcial2/ui/screens/registro/PantallaRegistro.kt

package app.movil.parcial2.ui.screens.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.domain.model.User
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import app.movil.parcial2.ui.navigation.Rutas
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(nav: NavHostController) {
    val api = remember { RetrofitClient.instance.create(ApiService::class.java) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(Role.USER) } // Default role is USER

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Crear cuenta") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registro de nuevo usuario", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // Role Selection
            Text("Selecciona un rol:", style = MaterialTheme.typography.bodyMedium)
            Row(Modifier.fillMaxWidth()) {
                Role.values().forEach { role ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (role == selectedRole),
                            onClick = { selectedRole = role }
                        )
                        Text(text = role.name, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        scope.launch {
                            try {
                                val newUser = User(username = username.trim(), password = password, role = selectedRole)
                                api.registerUser(newUser)
                                snackbarHostState.showSnackbar("¡Usuario registrado con éxito!")
                                // Navigate to login after a short delay
                                kotlinx.coroutines.delay(1000)
                                nav.navigate(Rutas.LOGIN) {
                                    // Clear back stack to prevent going back to register screen
                                    popUpTo(Rutas.LOGIN) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Error al registrar: ${e.message}")
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, completa todos los campos")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
            TextButton(onClick = { nav.popBackStack() }) {
                Text("Ya tengo una cuenta")
            }
        }
    }
}
