package app.movil.parcial2.data

import app.movil.parcial2.data.local.dao.CarritoDAO
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import app.movil.parcial2.domain.model.ProductoCarrito
import app.movil.parcial2.domain.repository.RepositorioCarrito
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositorioCarritoImpl(private val dao: CarritoDAO): RepositorioCarrito {
    private fun EntidadItemCarrito.toDomain() = ProductoCarrito(productId, name, unitPrice, quantity)
    override fun observeCart(): Flow<List<ProductoCarrito>> =
        dao.observeCart().map { list -> list.map { it.toDomain() } }

    override suspend fun addOrInc(productId: Long, name: String, unitPrice: Double) {

        dao.upsert(EntidadItemCarrito(productId = productId, name = name, unitPrice = unitPrice, quantity = 1))
    }
    override suspend fun decOrRemove(rowId: Long) {
        dao.deleteById(rowId)
    }
    override suspend fun updateQuantity(rowId: Long, quantity: Int) {
        if (quantity <= 0) dao.deleteById(rowId) else dao.updateQuantity(rowId, quantity)
    }
    override suspend fun remove(rowId: Long) = dao.deleteById(rowId)
    override suspend fun clear() = dao.clear()
}