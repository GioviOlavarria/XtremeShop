package app.movil.parcial2.domain.repository


import app.movil.parcial2.domain.model.Producto
import app.movil.parcial2.domain.model.Category

import kotlinx.coroutines.flow.Flow

interface RepositorioProductos {
    fun observeAll(): Flow<List<Producto>>
    fun observeByCategory(category: Category): Flow<List<Producto>>
    suspend fun get(id: Long): Producto?
    suspend fun create(product: Producto)
    suspend fun update(product: Producto)
    suspend fun delete(id: Long)
}