@file:OptIn(ExperimentalMaterial3Api::class)
package app.movil.parcial2.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

    if (state.user != null) {
        LaunchedEffect(Unit) {
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
                    modifier = Modifier.padding(24.dp)
                ) {
                    OutlinedTextField(
                        value = user,
                        onValueChange = { user = it },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    state.error?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Button(
                        onClick = { vm.login(user, pass) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Entrar")
                    }
                    LaunchedEffect(state.user) {
                        state.user?.let { sesion.currentUser = it }
                    }
                }
            }
        }
    }
}
