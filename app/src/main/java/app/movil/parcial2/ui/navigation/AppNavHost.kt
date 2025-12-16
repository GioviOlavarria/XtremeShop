package app.movil.parcial2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.movil.parcial2.ui.screens.about.about
import app.movil.parcial2.ui.screens.admin.PantallaAdmin
import app.movil.parcial2.ui.screens.admin.categories.AdminCategoriesScreen
import app.movil.parcial2.ui.screens.admin.orders.AdminOrderDetailScreen
import app.movil.parcial2.ui.screens.admin.orders.AdminOrdersScreen
import app.movil.parcial2.ui.screens.admin.users.AdminUserDetailScreen
import app.movil.parcial2.ui.screens.admin.users.AdminUsersScreen
import app.movil.parcial2.ui.screens.cart.CartScreen
import app.movil.parcial2.ui.screens.catalog.CatalogScreen
import app.movil.parcial2.ui.screens.checkout.CheckoutScreen
import app.movil.parcial2.ui.screens.confirmation.ConfirmationScreen
import app.movil.parcial2.ui.screens.dashboard.DashboardScreen
import app.movil.parcial2.ui.screens.detail.DetailScreen
import app.movil.parcial2.ui.screens.home.HomeScreen
import app.movil.parcial2.ui.screens.login.LoginScreen
import app.movil.parcial2.ui.screens.orders.MyOrdersScreen
import app.movil.parcial2.ui.screens.orders.OrderDetailScreen
import app.movil.parcial2.ui.screens.profile.ProfileScreen
import app.movil.parcial2.ui.screens.recover.RecoverPasswordScreen
import app.movil.parcial2.ui.screens.registro.PantallaRegistro
import app.movil.parcial2.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(nav: NavHostController) {
    NavHost(
        navController = nav,
        startDestination = Rutas.HOME
    ) {
        composable(Rutas.LOGIN) { LoginScreen(nav) }
        composable(Rutas.REGISTER) { PantallaRegistro(nav = nav) }
        composable(Rutas.RECOVER) { RecoverPasswordScreen(nav) }

        composable(Rutas.HOME) { HomeScreen(nav) }
        composable(Rutas.CATALOG) { CatalogScreen(nav) }
        composable(Rutas.CART) { CartScreen(nav) }
        composable(Rutas.CHECKOUT) { CheckoutScreen(nav) }
        composable("${Rutas.CONFIRMATION}/{orderId}") { back ->
            val orderId = back.arguments?.getString("orderId")?.toLongOrNull() ?: 0L
            ConfirmationScreen(nav, orderId)
        }
        composable("${Rutas.ORDER_DETAIL}/{orderId}") { back ->
            val orderId = back.arguments?.getString("orderId")?.toLongOrNull() ?: 0L
            OrderDetailScreen(nav, orderId)
        }

        composable(Rutas.PROFILE) { ProfileScreen(nav) }
        composable(Rutas.SETTINGS) { SettingsScreen(nav) }
        composable(Rutas.MY_ORDERS) { MyOrdersScreen(nav) }

        composable(Rutas.ABOUT) { about(nav) }

        composable(Rutas.DASHBOARD) { DashboardScreen(nav) }
        composable(Rutas.ADMIN) { PantallaAdmin(nav) }

        composable(Rutas.ADMIN_USERS) { AdminUsersScreen(nav) }
        composable("${Rutas.ADMIN_USER_DETAIL}/{userId}") { back ->
            val userId = back.arguments?.getString("userId")?.toLongOrNull() ?: 0L
            AdminUserDetailScreen(nav, userId)
        }

        composable(Rutas.ADMIN_CATEGORIES) { AdminCategoriesScreen(nav) }

        composable(Rutas.ADMIN_ORDERS) { AdminOrdersScreen(nav) }
        composable("${Rutas.ADMIN_ORDER_DETAIL}/{orderId}") { back ->
            val orderId = back.arguments?.getString("orderId")?.toLongOrNull() ?: 0L
            AdminOrderDetailScreen(nav, orderId)
        }

        composable("${Rutas.DETAIL}/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: 0L
            DetailScreen(nav, id)
        }
    }
}
