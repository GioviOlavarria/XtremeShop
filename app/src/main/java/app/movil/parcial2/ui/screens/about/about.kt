package app.movil.parcial2.ui.screens.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun about(nav: NavHostController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Quiénes somos") }) }) { p ->
        Column(Modifier.padding(p).padding(16.dp)) {
            Text("Somos 2 compañeros de Duoc UC realizando un parcial para app móviles. los sangre ")

        }
    }
}