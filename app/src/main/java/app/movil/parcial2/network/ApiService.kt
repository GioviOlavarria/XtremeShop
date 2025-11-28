package app.movil.parcial2.network


import app.movil.parcial2.domain.model.Producto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/products")
    suspend fun getProducts(): List<Producto>

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Producto
}