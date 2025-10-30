package app.movil.parcial2.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.movil.parcial2.ui.screens.*
import app.movil.parcial2.ui.screens.about.about
import app.movil.parcial2.ui.screens.admin.PantallaAdmin
import app.movil.parcial2.ui.screens.cart.CartScreen
import app.movil.parcial2.ui.screens.home.HomeScreen
import app.movil.parcial2.ui.screens.login.LoginScreen

@Composable
fun AppNavHost(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Rutas.LOGIN) {
        composable(Rutas.LOGIN) { LoginScreen(nav) }
        composable(Rutas.HOME) { HomeScreen(nav) }
        composable(Rutas.CATALOG) { CatalogScreen(nav) }
        composable(Rutas.CART) { CartScreen(nav) }
        composable(Rutas.ABOUT) { about(nav) }
        composable(Rutas.ADMIN) { PantallaAdmin(nav) }
        composable(Rutas.DETAIL) { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
            DetailScreen(nav, id)
        }
    }
}

@Composable
fun CatalogScreen(x0: NavHostController) {
    TODO("Not yet implemented")
}