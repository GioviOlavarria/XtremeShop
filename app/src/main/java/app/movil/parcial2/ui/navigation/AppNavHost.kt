package app.movil.parcial2.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.movil.parcial2.ui.screens.*

@Composable
fun AppNavHost(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Rutas.LOGIN) {
        composable(Rutas.LOGIN) { LoginScreen(nav) }
        composable(Rutas.HOME) { HomeScreen(nav) }
        composable(Rutas.CATALOG) { CatalogScreen(nav) }
        composable(Rutas.CART) { CartScreen(nav) }
        composable(Rutas.ABOUT) { AboutScreen(nav) }
        composable(Rutas.ADMIN) { AdminScreen(nav) }
        composable(Rutas.DETAIL) { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
            DetailScreen(nav, id)
        }
    }
}