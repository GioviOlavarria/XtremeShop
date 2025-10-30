package app.movil.parcial2.domain.repository

import app.movil.parcial2.domain.model.ProductoCarrito
import kotlinx.coroutines.flow.Flow

interface RepositorioCarrito {
    fun observeCart(): Flow<List<ProductoCarrito>>
    suspend fun addOrInc(productId: Long, name: String, unitPrice: Double)
    suspend fun decOrRemove(rowId: Long)
    suspend fun updateQuantity(rowId: Long, quantity: Int)
    suspend fun remove(rowId: Long)
    suspend fun clear()
}