package app.movil.parcial2.data.local.dao

import androidx.room.*
import app.movil.parcial2.data.local.entidades.EntidadProducto
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY name")
    fun observeAll(): Flow<List<EntidadProducto>>

    @Query("SELECT * FROM productos WHERE category = :category ORDER BY name")
    fun observeByCategory(category: String): Flow<List<EntidadProducto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getById(id: Long): EntidadProducto?

    @Insert(onConflict = OnConflictStrategy.ABORT) // ID único (validación)
    suspend fun insert(producto: EntidadProducto)

    @Update
    suspend fun update(producto: EntidadProducto)

    @Delete
    suspend fun delete(producto: EntidadProducto)
}