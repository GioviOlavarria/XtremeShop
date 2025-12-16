package app.movil.parcial2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.movil.parcial2.data.local.entidades.EntidadCategoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun observarCategorias(): Flow<List<EntidadCategoria>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(cat: EntidadCategoria): Long

    @Delete
    suspend fun eliminar(cat: EntidadCategoria)

    @Query("DELETE FROM categorias")
    suspend fun eliminarTodo()
}
