package app.movil.parcial2.data

import android.content.Context
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import app.movil.parcial2.data.local.entidades.EntidadPedido
import app.movil.parcial2.data.local.entidades.EntidadPedidoItem
import app.movil.parcial2.domain.model.EstadoPedido
import app.movil.parcial2.domain.repository.RepositorioPedidos
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class RepositorioPedidosImpl(context: Context) : RepositorioPedidos {
    private val db = BaseDeDatos.get(context)
    private val pedidoDao = db.pedidoDao()

    override fun observarPedidos(): Flow<List<EntidadPedido>> = pedidoDao.observarPedidos()

    override fun observarPedidosPorUsuario(username: String): Flow<List<EntidadPedido>> =
        pedidoDao.observarPedidosPorUsuario(username)

    override suspend fun crearPedidoDesdeCarrito(
        username: String,
        userId: Long?,
        items: List<EntidadItemCarrito>,
        customerName: String?,
        customerEmail: String?,
        customerPhone: String?,
        address: String?,
        notes: String?
    ): Long {
        val total = items.sumOf { it.unitPrice * it.quantity }
        val orderNumber = generarNumeroPedido()

        val pedidoId = pedidoDao.insertarPedido(
            EntidadPedido(
                orderNumber = orderNumber,
                userId = userId,
                username = username,
                createdAtMillis = System.currentTimeMillis(),
                status = EstadoPedido.NUEVO.name,
                customerName = customerName,
                customerEmail = customerEmail,
                customerPhone = customerPhone,
                address = address,
                notes = notes,
                total = total
            )
        )

        val itemsPedido = items.map {
            EntidadPedidoItem(
                orderId = pedidoId,
                productId = it.productId,
                name = it.name,
                unitPrice = it.unitPrice,
                quantity = it.quantity
            )
        }
        pedidoDao.insertarItems(itemsPedido)

        return pedidoId
    }

    override suspend fun obtenerPedido(orderId: Long): EntidadPedido? = pedidoDao.obtenerPedido(orderId)

    override suspend fun obtenerItems(orderId: Long): List<EntidadPedidoItem> = pedidoDao.obtenerItems(orderId)

    override suspend fun actualizarEstado(orderId: Long, estado: EstadoPedido) {
        pedidoDao.actualizarEstado(orderId, estado.name)
    }

    private fun generarNumeroPedido(): String {
        val n = Random.nextInt(100000, 999999)
        return "XS-$n"
    }
}
