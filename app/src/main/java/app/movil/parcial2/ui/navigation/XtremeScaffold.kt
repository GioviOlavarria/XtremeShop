package app.movil.parcial2.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.util.sesion
import kotlinx.coroutines.launch



private fun baseRoute(route: String?): String? {
    return route?.substringBefore("/")
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XtremeScaffold(
    nav: NavHostController,
    title: String,
    showBack: Boolean = false,
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val user = sesion.currentUser

    fun navigateAndClose(route: String) {
        val current = nav.currentDestination?.route?.let { baseRoute(it) }
        val target = baseRoute(route)
        if (current == target) {
            scope.launch { drawerState.close() }
            return
        }

        scope.launch {
            drawerState.close()

            nav.navigate(route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "XtremeShop",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )

                if (user?.role == Role.ADMIN) {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.BarChart, null) },
                        label = { Text("Dashboard") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.DASHBOARD) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.AdminPanelSettings, null) },
                        label = { Text("Productos") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.ADMIN) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Category, null) },
                        label = { Text("Categorías") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.ADMIN_CATEGORIES) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.ListAlt, null) },
                        label = { Text("Pedidos") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.ADMIN_ORDERS) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.People, null) },
                        label = { Text("Usuarios") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.ADMIN_USERS) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Logout, null) },
                        label = { Text("Cerrar sesión") },
                        selected = false,
                        onClick = {
                            sesion.currentUser = null
                            navigateAndClose(Rutas.LOGIN)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                } else {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Home, null) },
                        label = { Text("Inicio") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.HOME) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Storefront, null) },
                        label = { Text("Catálogo") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.CATALOG) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.ShoppingCart, null) },
                        label = { Text("Carrito") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.CART) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.ListAlt, null) },
                        label = { Text("Mis pedidos") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.MY_ORDERS) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Person, null) },
                        label = { Text("Perfil") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.PROFILE) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Settings, null) },
                        label = { Text("Configuración") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.SETTINGS) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Filled.Info, null) },
                        label = { Text("Quiénes somos") },
                        selected = false,
                        onClick = { navigateAndClose(Rutas.ABOUT) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        if (showBack) {
                            IconButton(onClick = { nav.popBackStack() }) {
                                Icon(Icons.Filled.ArrowBack, "Volver")
                            }
                        } else {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, "Menú de navegación")
                            }
                        }
                    },
                    actions = {
                        if (user?.role != Role.ADMIN) {
                            IconButton(onClick = { nav.navigate(Rutas.CART) }) {
                                Icon(Icons.Filled.ShoppingCart, "Ir al carrito")
                            }
                        }
                    }
                )
            },
            snackbarHost = snackbarHost
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}
