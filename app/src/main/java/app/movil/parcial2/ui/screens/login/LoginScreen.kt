package app.movil.parcial2.ui.screens.login

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHostController
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.ui.screens.login.LoginViewModel

@Composable
fun LoginScreen(nav: NavHostController, vm: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    if (state.user != null) {
        // Ir al Home y limpiar backstack
        LaunchedEffect(Unit) {
            nav.navigate(Rutas.HOME) { popUpTo(Rutas.LOGIN) { inclusive = true } }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Iniciar sesión") }) }
    ) { p ->
        Column(Modifier.padding(p).padding(16.dp)) {
            OutlinedTextField(user, { user = it }, label = { Text("Usuario") })
            OutlinedTextField(pass, { pass = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())
            if (state.error != null) Text(state.error!!, color = MaterialTheme.colorScheme.error)
            Button(onClick = { vm.login(user, pass) }, modifier = Modifier.padding(top = 12.dp)) {
                Text("Entrar")
            }
        }
    }
}