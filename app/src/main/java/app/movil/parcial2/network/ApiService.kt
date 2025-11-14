package app.movil.parcial2.network


import app.movil.parcial2.domain.model.Producto
import retrofit2.http.GET

interface ApiService {

    @GET("api/products")
    suspend fun getProducts(): List<Producto>
}