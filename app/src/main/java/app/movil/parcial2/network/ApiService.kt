package app.movil.parcial2.network

import app.movil.parcial2.domain.model.Producto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

import app.movil.parcial2.domain.model.User

interface ApiService {

    // Productos
    @GET("api/products")
    suspend fun getProducts(): List<Producto>
    // Get producto por ID
    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Producto

    @POST("api/products")
    suspend fun createProduct(@Body product: Producto): Producto

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Body product: Producto): Producto

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Unit>

    // Usuarios
    // Post usuario
    @POST("api/users")
    suspend fun registerUser(@Body user: User): User
    // Fetch  users
    @GET("api/users")
    suspend fun getUsers(): List<User>
}