package app.movil.parcial2.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.ui.navigation.XtremeScaffold
import app.movil.parcial2.util.sesion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(nav: NavHostController) {
    val u = sesion.currentUser
    val (username, setUsername) = remember { mutableStateOf(u?.username ?: "") }
    val (paymentMethod, setPaymentMethod) = remember { mutableStateOf("Tarjeta (simulado)") }
    val (prefs, setPrefs) = remember { mutableStateOf("Ofertas y promociones") }

    XtremeScaffold(nav = nav, title = "Perfil", showBack = true) { p ->
        Column(
            modifier = Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Datos personales")
            OutlinedTextField(value = username, onValueChange = setUsername, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth())

            Text("Métodos de pago (simulado)")
            OutlinedTextField(value = paymentMethod, onValueChange = setPaymentMethod, label = { Text("Método") }, modifier = Modifier.fillMaxWidth())

            Text("Preferencias")
            OutlinedTextField(value = prefs, onValueChange = setPrefs, label = { Text("Preferencias") }, modifier = Modifier.fillMaxWidth())

            Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Guardar (simulado)")
            }
        }
    }
}
