package app.movil.parcial2.ui.screens.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.data.local.dao.CarritoDAO
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartDao: CarritoDAO

    init {
        val db = BaseDeDatos.get(application.applicationContext)
        cartDao = db.carritoDao()
    }

    val cartItems = cartDao.observeCart()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalPrice = cartDao.observeTotalPrice()
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun incrementItem(item: EntidadItemCarrito) {
        viewModelScope.launch {
            cartDao.updateQuantity(item.rowId, item.quantity + 1)
        }
    }

    fun decrementItem(item: EntidadItemCarrito) {
        viewModelScope.launch {
            val newQty = item.quantity - 1
            if (newQty <= 0) {
                cartDao.deleteById(item.rowId)
            } else {
                cartDao.updateQuantity(item.rowId, newQty)
            }
        }
    }

    fun deleteItem(item: EntidadItemCarrito) {
        viewModelScope.launch {
            cartDao.deleteById(item.rowId)
        }
    }
}