package app.movil.parcial2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.movil.parcial2.ui.screens.home.HomeScreen
import app.movil.parcial2.ui.screens.login.LoginScreen
import app.movil.parcial2.ui.screens.cart.CartScreen
import app.movil.parcial2.ui.screens.about.about
import app.movil.parcial2.ui.screens.admin.PantallaAdmin
import app.movil.parcial2.ui.screens.dashboard.DashboardScreen
import app.movil.parcial2.ui.screens.detail.DetailScreen
import app.movil.parcial2.ui.screens.catalog.CatalogScreen
import app.movil.parcial2.ui.screens.payment.PaymentScreen
import app.movil.parcial2.ui.screens.registro.PantallaRegistro

@Composable
fun AppNavHost(nav: NavHostController) {
    NavHost(
        navController = nav,
        startDestination = Rutas.LOGIN
    ) {
        composable(Rutas.LOGIN)   { LoginScreen(nav) }
        composable(Rutas.REGISTER) { PantallaRegistro(nav = nav) }
        composable(Rutas.HOME)    { HomeScreen(nav) }
        composable(Rutas.CATALOG) { CatalogScreen(nav) }
        composable(Rutas.CART)    { CartScreen(nav) }
        composable(Rutas.ABOUT)   { about(nav) }
        composable(Rutas.ADMIN)   { PantallaAdmin(nav) }
        composable(Rutas.DASHBOARD) { DashboardScreen(nav) }
        composable(Rutas.PAYMENT) { PaymentScreen(nav) }

        composable("${Rutas.DETAIL}/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
            DetailScreen(nav, id)
        }
    }
}