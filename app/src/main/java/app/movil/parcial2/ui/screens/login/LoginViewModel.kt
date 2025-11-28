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

// This data class will hold the state of the login screen
data class LoginState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class LoginViewModel : ViewModel() {
    // The API service instance
    private val api: ApiService = RetrofitClient.instance.create(ApiService::class.java)

    // Private mutable state flow that only the ViewModel can modify
    private val _state = MutableStateFlow(LoginState())
    // Public immutable state flow that the UI can observe
    val state = _state.asStateFlow()

    fun login(username: String, pass: String) {
        // Don't start a new login if one is already in progress
        if (_state.value.isLoading) return

        viewModelScope.launch {
            // Set loading state and clear previous errors
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // 1. Fetch all users from the API and convert to a Kotlin List
                val allUsers: List<User> = api.getUsers()


                // 2. Find a user that matches the provided credentials
                val foundUser = allUsers.find { it.username.equals(username, ignoreCase = true) && it.password == pass }

                if (foundUser != null) {
                    // 3. If found, update the state with the user object
                    _state.update { it.copy(isLoading = false, user = foundUser) }
                } else {
                    // 4. If not found, update the state with an error message
                    _state.update { it.copy(isLoading = false, error = "Invalid username or password.") }
                }

            } catch (e: Exception) {
                // 5. If there's an exception (e.g., network error), update the state with the error
                _state.update { it.copy(isLoading = false, error = "Connection error: ${e.message}") }
            }
        }
    }
}
