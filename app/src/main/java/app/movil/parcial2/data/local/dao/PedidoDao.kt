package app.movil.parcial2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.movil.parcial2.data.local.entidades.EntidadPedido
import app.movil.parcial2.data.local.entidades.EntidadPedidoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {

    @Query("SELECT * FROM pedidos ORDER BY createdAtMillis DESC")
    fun observarPedidos(): Flow<List<EntidadPedido>>

    @Query("SELECT * FROM pedidos WHERE username = :username ORDER BY createdAtMillis DESC")
    fun observarPedidosPorUsuario(username: String): Flow<List<EntidadPedido>>

    @Query("SELECT * FROM pedidos WHERE id = :orderId LIMIT 1")
    suspend fun obtenerPedido(orderId: Long): EntidadPedido?

    @Query("SELECT * FROM pedido_items WHERE orderId = :orderId")
    suspend fun obtenerItems(orderId: Long): List<EntidadPedidoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPedido(pedido: EntidadPedido): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarItems(items: List<EntidadPedidoItem>)

    @Query("UPDATE pedidos SET status = :status WHERE id = :orderId")
    suspend fun actualizarEstado(orderId: Long, status: String)
}
