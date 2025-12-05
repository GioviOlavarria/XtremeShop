package app.movil.parcial2.ui.screens.login

import androidx.compose.ui.semantics.password
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.movil.parcial2.domain.model.User
import app.movil.parcial2.network.ApiService
import app.movil.parcial2.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class LoginViewModel : ViewModel() {

    private val api: ApiService = RetrofitClient.instance.create(ApiService::class.java)


    private val _state = MutableStateFlow(LoginState())

    val state = _state.asStateFlow()

    fun login(username: String, pass: String) {

        if (_state.value.isLoading) return

        viewModelScope.launch {

            _state.update { it.copy(isLoading = true, error = null) }

            try {

                val allUsers: List<User> = api.getUsers()



                val foundUser = allUsers.find { it.username.equals(username, ignoreCase = true) && it.password == pass }

                if (foundUser != null) {

                    _state.update { it.copy(isLoading = false, user = foundUser) }
                } else {

                    _state.update { it.copy(isLoading = false, error = "Invalid username or password.") }
                }

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Connection error: ${e.message}") }
            }
        }
    }
}
