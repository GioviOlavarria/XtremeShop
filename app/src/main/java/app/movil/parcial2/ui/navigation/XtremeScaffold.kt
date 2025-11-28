package app.movil.parcial2.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
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
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XtremeScaffold(
    nav: NavHostController,
    title: String,
    showBack: Boolean = false,
    snackbarHost: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun navigateAndClose(route: String) {
        scope.launch { drawerState.close() }
        nav.navigate(route)
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

                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { navigateAndClose(Rutas.HOME) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Storefront, contentDescription = null) },
                    label = { Text("Catálogo") },
                    selected = false,
                    onClick = { navigateAndClose(Rutas.CATALOG) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
                    label = { Text("Carrito") },
                    selected = false,
                    onClick = { navigateAndClose(Rutas.CART) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Info, contentDescription = null) },
                    label = { Text("Quiénes somos") },
                    selected = false,
                    onClick = { navigateAndClose(Rutas.ABOUT) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                    label = { Text("Admin") },
                    selected = false,
                    onClick = { navigateAndClose(Rutas.ADMIN) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
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
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Volver"
                                )
                            }
                        } else {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menú de navegación"
                                )
                            }
                        }
                    },
                    actions = {
                        if (showBack) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menú de navegación"
                                )
                            }
                        }
                    }
                )
            },
            snackbarHost = {
                snackbarHost?.invoke()
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}
