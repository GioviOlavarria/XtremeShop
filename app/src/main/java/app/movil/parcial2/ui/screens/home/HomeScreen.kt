package app.movil.parcial2.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.ui.navigation.Rutas
import app.movil.parcial2.ui.navigation.XtremeScaffold
import app.movil.parcial2.util.sesion
import app.movil.parcial2.R

@Composable
fun HomeScreen(nav: NavHostController) {
    val user = remember { sesion.currentUser }

    XtremeScaffold(
        nav = nav,
        title = "XtremeShop",
        showBack = false
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            if (user?.role == Role.ADMIN) {
                // --- Admin View ---
                Text(
                    text = "Panel de Administrador",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Bienvenido, ${user.username}.",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = { nav.navigate(Rutas.DASHBOARD) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Dashboard de Ventas")
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { nav.navigate(Rutas.ADMIN) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Gestionar Productos")
                }

            } else {
                // --- Regular User View ---
                Image(
                    painter = painterResource(id = R.drawable.icon_shop),
                    contentDescription = "Banner de XtremeShop",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bienvenido a XtremeShop",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { nav.navigate(Rutas.CATALOG) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Catálogo")
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { nav.navigate(Rutas.ABOUT) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Conocer más")
                }
            }

            Spacer(Modifier.weight(1.0f)) // Pushes the logout button to the bottom

            OutlinedButton(
                onClick = {
                    sesion.currentUser = null
                    nav.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
