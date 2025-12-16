package app.movil.parcial2.util

import app.movil.parcial2.data.local.entidades.EntidadPedido
import app.movil.parcial2.data.local.entidades.EntidadPedidoItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BoletaUtil {

    fun generarBoletaTexto(pedido: EntidadPedido, items: List<EntidadPedidoItem>): String {
        val df = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale("es", "CL"))
        val fecha = df.format(Date(pedido.createdAtMillis))

        val detalle = buildString {
            appendLine("BOLETA XTREMESHOP")
            appendLine("Pedido: ${pedido.orderNumber}")
            appendLine("Fecha: $fecha")
            appendLine("Cliente: ${pedido.customerName ?: pedido.username}")
            if (!pedido.customerEmail.isNullOrBlank()) appendLine("Email: ${pedido.customerEmail}")
            if (!pedido.customerPhone.isNullOrBlank()) appendLine("Tel: ${pedido.customerPhone}")
            if (!pedido.address.isNullOrBlank()) appendLine("Dirección: ${pedido.address}")
            appendLine("--------------------------------")
            items.forEach { itx ->
                val sub = itx.unitPrice * itx.quantity
                appendLine("${itx.name}  x${itx.quantity}  $${"%.0f".format(itx.unitPrice)}  =  $${"%.0f".format(sub)}")
            }
            appendLine("--------------------------------")
            appendLine("TOTAL: $${"%.0f".format(pedido.total)}")
            appendLine("Estado: ${pedido.status}")
            if (!pedido.notes.isNullOrBlank()) {
                appendLine("--------------------------------")
                appendLine("Notas: ${pedido.notes}")
            }
            appendLine("--------------------------------")
            appendLine("Gracias por comprar en XtremeShop.")
        }
        return detalle
    }
}
