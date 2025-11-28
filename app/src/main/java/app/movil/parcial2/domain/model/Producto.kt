package app.movil.parcial2.domain.model

enum class Category { SKATE, ROLLER, BMX }

data class Producto(
    val id: Long,
    val name: String,
    val price: Double,
    val description: String,
    val category: String
)
