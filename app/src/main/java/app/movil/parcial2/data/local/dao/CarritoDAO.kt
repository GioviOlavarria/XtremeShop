package app.movil.parcial2.data.local.dao


import androidx.room.*
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDAO {
    @Query("SELECT * FROM items_carrito ORDER BY rowId")
    fun observeCart(): Flow<List<EntidadItemCarrito>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: EntidadItemCarrito)

    @Query("UPDATE items_carrito SET quantity = :q WHERE rowId = :rowId")
    suspend fun updateQuantity(rowId: Long, q: Int)

    @Query("DELETE FROM items_carrito WHERE rowId = :rowId")
    suspend fun deleteById(rowId: Long)

    @Query("DELETE FROM items_carrito")
    suspend fun clear()
}