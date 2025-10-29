package app.movil.parcial2.domain.model

enum class Role { ADMIN, CLIENT }
data class User(val username: String, val password: String, val role: Role)