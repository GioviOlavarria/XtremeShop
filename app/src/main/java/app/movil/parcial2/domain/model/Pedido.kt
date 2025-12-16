package app.movil.parcial2.domain.model

enum class EstadoPedido { NUEVO, EN_PROCESO, COMPLETADO, CANCELADO }

data class PedidoItem(
    val productId: Long,
    val name: String,
    val unitPrice: Double,
    val quantity: Int
)

data class Pedido(
    val id: Long = 0,
    val orderNumber: String,
    val userId: Long?,
    val username: String,
    val createdAtMillis: Long,
    val status: EstadoPedido,
    val customerName: String?,
    val customerEmail: String?,
    val customerPhone: String?,
    val address: String?,
    val notes: String?,
    val total: Double
)
