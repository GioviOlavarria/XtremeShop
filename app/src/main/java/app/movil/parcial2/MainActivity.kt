package app.movil.parcial2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import app.movil.parcial2.ui.navigation.AppNavHost
import app.movil.parcial2.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeVm: app.movil.parcial2.ui.theme.ThemeViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel()

            val isDark by themeVm.isDark.collectAsState()

            app.movil.parcial2.ui.theme.XtremeShopTheme(darkTheme = isDark) {
                // Tu NavHost actual
                val nav = androidx.navigation.compose.rememberNavController()
                app.movil.parcial2.ui.navigation.AppNavHost(nav)
            }
        }
    }
}