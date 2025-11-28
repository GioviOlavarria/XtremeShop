package app.movil.parcial2.domain.model

enum class Role {
    USER,
    ADMIN
}
data class User(
    val id: Long? = null,
    val username: String,
    val password: String,
    val role: Role = Role.USER
)