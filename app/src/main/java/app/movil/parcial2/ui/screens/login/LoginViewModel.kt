package app.movil.parcial2.ui.screens.login

import androidx.lifecycle.ViewModel
import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.domain.model.User
import app.movil.parcial2.util.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginState(val user: User? = null, val error: String? = null)

class LoginViewModel: ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun login(u: String, p: String) {
        val user = auth.login(u, p)
        _state.value = if (user != null) LoginState(user) else LoginState(error = "Usuario o contraseña incorrectos")
    }
}