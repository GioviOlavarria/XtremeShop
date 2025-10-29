package app.movil.parcial2.domain.model

data class ProductoCarrito(
    val productId: Long,
    val name: String,
    val unitPrice: Double,
    val quantity: Int
)