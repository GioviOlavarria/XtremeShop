package app.movil.parcial2.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pedido_items",
    indices = [Index("orderId")]
)
data class EntidadPedidoItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val productId: Long,
    val name: String,
    val unitPrice: Double,
    val quantity: Int
)
