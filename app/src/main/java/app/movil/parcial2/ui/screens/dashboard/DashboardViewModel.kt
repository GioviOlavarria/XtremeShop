package app.movil.parcial2.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import app.movil.parcial2.util.VentasSesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DashboardState(
    val totalVentas: Int = 0,
    val productosVendidos: Int = 0,
    val dineroProcesado: Double = 0.0
)

class DashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        _state.value = DashboardState(
            totalVentas = VentasSesion.totalVentas,
            productosVendidos = VentasSesion.productosVendidos,
            dineroProcesado = VentasSesion.dineroProcesado
        )
    }
}