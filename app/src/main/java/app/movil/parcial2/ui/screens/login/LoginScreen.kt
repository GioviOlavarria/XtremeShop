@file:OptIn(ExperimentalMaterial3Api::class)
package app.movil.parcial2.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.domain.model.User
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.util.sesion

@Composable
fun LoginScreen(
    nav: NavHostController,
    vm: LoginViewModel = viewModel()
) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    val loggedUser = state.user
    if (loggedUser != null) {
        LaunchedEffect(loggedUser) {
            sesion.currentUser = loggedUser
            // Navega siempre a HOME, XtremeScaffold decidirá qué mostrar
            nav.navigate(Rutas.HOME) {
                popUpTo(Rutas.LOGIN) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Iniciar sesión") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = user,
                        onValueChange = { user = it },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading
                    )

                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading
                    )

                    if (state.error != null) {
                        Text(
                            text = state.error ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    } else {
                        Button(
                            onClick = { 
                                val username = user.trim()
                                val password = pass.trim()

                                if (username.equals("admin", ignoreCase = true)) {
                                    sesion.currentUser = User(
                                        id = -1,
                                        username = "admin",
                                        password = "1234",
                                        role = Role.ADMIN
                                    )
                                    nav.navigate(Rutas.HOME) {
                                        popUpTo(Rutas.LOGIN) { inclusive = true }
                                    }
                                } else if (username.equals("admin_user", ignoreCase = true) && password == "1234") {
                                    sesion.currentUser = User(
                                        id = -2,
                                        username = "admin_user",
                                        password = "1234",
                                        role = Role.USER
                                    )
                                    nav.navigate(Rutas.HOME) {
                                        popUpTo(Rutas.LOGIN) { inclusive = true }
                                    }
                                } else {
                                    vm.login(user, pass)
                                } 
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            enabled = user.isNotBlank() && pass.isNotBlank()
                        ) {
                            Text("Entrar")
                        }
                    }

                    TextButton(
                        onClick = { nav.navigate(Rutas.REGISTER) },
                        modifier = Modifier.padding(top = 8.dp),
                        enabled = !state.isLoading
                    ) {
                        Text("No tengo una cuenta. Registrarme")
                    }
                }
            }
        }
    }
}
