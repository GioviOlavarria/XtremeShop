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
            // --- Icono de la tienda ---
            Image(
                painter = painterResource(id = R.drawable.icon_shop), // Your image file
                contentDescription = "Banner de XtremeShop", // Accessibility text
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp), // Adjust height as needed
                contentScale = ContentScale.Crop // Scales the image to fill the width
            )

            // --- Other content on your home screen ---
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
                Text("Catálogo")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { nav.navigate(Rutas.CART) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Carrito")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { nav.navigate(Rutas.ABOUT) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Quiénes somos")
            }

            if (user?.role == Role.ADMIN) {
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { nav.navigate(Rutas.ADMIN) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Panel de administración")
                }
            }

            Spacer(Modifier.height(24.dp))

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
