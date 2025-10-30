package app.movil.parcial2.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_carrito")
data class EntidadItemCarrito(
    @PrimaryKey(autoGenerate = true) val rowId: Long = 0,
    val productId: Long,
    val name: String,
    val unitPrice: Double,
    val quantity: Int
)