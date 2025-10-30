package app.movil.parcial2.util

import app.movil.parcial2.domain.model.Role
import app.movil.parcial2.domain.model.User

object auth {
    val users = listOf(
        User("admin", "admin123", Role.ADMIN),
        User("cliente", "cliente123", Role.CLIENT)
    )
    fun login(u: String, p: String): User? =
        users.firstOrNull { it.username == u && it.password == p }
}