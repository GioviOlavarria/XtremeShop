package app.movil.parcial2.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.movil.parcial2.R
import app.movil.parcial2.ui.navigation.XtremeScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun about(nav: NavHostController) {
    XtremeScaffold(nav = nav, title = "Quiénes somos", showBack = true) { p ->
        Box(
            modifier = Modifier
                .padding(p)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.about_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Surface(
                color = Color.Black.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxSize()
            ) {}

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "XtremeShop",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Somos una tienda de productos extremos. Nuestra misión es ofrecer calidad, buen precio y una experiencia rápida de compra.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "• Skate • BMX • Roller • Accesorios",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}
