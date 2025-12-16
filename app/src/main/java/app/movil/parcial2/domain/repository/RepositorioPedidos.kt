package app.movil.parcial2.domain.repository

import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import app.movil.parcial2.domain.model.EstadoPedido
import kotlinx.coroutines.flow.Flow
import app.movil.parcial2.data.local.entidades.EntidadPedido

interface RepositorioPedidos {
    fun observarPedidos(): Flow<List<EntidadPedido>>
    fun observarPedidosPorUsuario(username: String): Flow<List<EntidadPedido>>
    suspend fun crearPedidoDesdeCarrito(
        username: String,
        userId: Long?,
        items: List<EntidadItemCarrito>,
        customerName: String?,
        customerEmail: String?,
        customerPhone: String?,
        address: String?,
        notes: String?
    ): Long

    suspend fun obtenerPedido(orderId: Long): EntidadPedido?
    suspend fun obtenerItems(orderId: Long): List<app.movil.parcial2.data.local.entidades.EntidadPedidoItem>
    suspend fun actualizarEstado(orderId: Long, estado: EstadoPedido)
}
