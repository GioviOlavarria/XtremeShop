package app.movil.parcial2.data

import app.movil.parcial2.data.local.dao.ProductoDao
import app.movil.parcial2.data.local.entidades.EntidadProducto
import app.movil.parcial2.domain.model.Category
import app.movil.parcial2.domain.model.Producto
import app.movil.parcial2.domain.repository.RepositorioProductos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositorioProductoImpl(private val dao: ProductoDao) : RepositorioProductos {

    private fun EntidadProducto.toDomain() =
        Producto(id, name, price, description, category, null)

    private fun Producto.toEntity() =
        EntidadProducto(id, name, price, description, category)

    override fun observeAll(): Flow<List<Producto>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeByCategory(category: Category): Flow<List<Producto>> =
        dao.observeByCategory(category.name).map { list -> list.map { it.toDomain() } }

    override suspend fun get(id: Long): Producto? =
        dao.getById(id)?.toDomain()

    override suspend fun create(product: Producto) {
        require(product.name.isNotBlank())
        require(product.description.isNotBlank())
        require(product.price > 0)
        dao.insert(product.toEntity())
    }

    override suspend fun update(product: Producto) {
        require(product.name.isNotBlank())
        require(product.description.isNotBlank())
        require(product.price > 0)
        dao.update(product.toEntity())
    }

    override suspend fun delete(id: Long) {
        dao.getById(id)?.let { dao.delete(it) }
    }
}
