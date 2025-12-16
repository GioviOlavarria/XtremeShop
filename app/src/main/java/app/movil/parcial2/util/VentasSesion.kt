package app.movil.parcial2.util

/**
 * Objeto singleton para mantener las estadísticas de ventas en memoria.
 * NOTA: Todos los datos se perderán cuando la aplicación se cierre.
 */
object VentasSesion {
    var totalVentas: Int = 0
        private set

    var productosVendidos: Int = 0
        private set

    var dineroProcesado: Double = 0.0
        private set

    /**
     * Registra una nueva venta, actualizando las estadísticas totales.
     * @param monto El valor total de la venta.
     * @param cantidadItems El número total de productos en la venta.
     */
    fun registrarVenta(monto: Double, cantidadItems: Int) {
        totalVentas++
        productosVendidos += cantidadItems
        dineroProcesado += monto
    }

    /**
     * Reinicia todas las estadísticas a cero.
     */
    fun reset() {
        totalVentas = 0
        productosVendidos = 0
        dineroProcesado = 0.0
    }
}
