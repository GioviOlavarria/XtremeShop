package app.movil.parcial2.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class EntidadCategoria(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String
)
