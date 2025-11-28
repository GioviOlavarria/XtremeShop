package app.movil.parcial2.network

import app.movil.parcial2.domain.model.Producto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("api/products")
    suspend fun getProducts(): List<Producto>

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Producto

    @POST("api/products")
    suspend fun createProduct(@Body product: Producto): Producto

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Body product: Producto): Producto

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Unit>
}