package app.movil.parcial2.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class EntidadProducto(
    @PrimaryKey val id: Long?,
    val name: String,
    val price: Double,
    val description: String,
    val category: String
)