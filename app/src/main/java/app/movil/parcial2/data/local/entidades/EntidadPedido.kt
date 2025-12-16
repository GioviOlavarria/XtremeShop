package app.movil.parcial2.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos")
data class EntidadPedido(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val userId: Long?,
    val username: String,
    val createdAtMillis: Long,
    val status: String,

    val customerName: String?,
    val customerEmail: String?,
    val customerPhone: String?,
    val address: String?,
    val notes: String?,

    val total: Double
)
