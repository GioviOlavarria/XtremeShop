package app.movil.parcial2.data.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import app.movil.parcial2.data.local.dao.ProductoDao
import app.movil.parcial2.data.local.dao.CarritoDAO
import app.movil.parcial2.data.local.entidades.EntidadProducto
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [EntidadProducto::class, EntidadItemCarrito::class], version = 1)
abstract class BaseDeDatos : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDAO

    companion object {
        @Volatile private var INSTANCE: BaseDeDatos? = null

        fun get(context: Context): BaseDeDatos =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, BaseDeDatos::class.java, "app.db")
                    .addCallback(seedCallback)
                    .build()
                    .also { INSTANCE = it }
            }


        private val seedCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {

                    db.execSQL("INSERT INTO productos(id,name,price,description,category) VALUES" +
                            "(1,'Skate Street',49990,'Tabla de arce 8.0', 'SKATE')," +
                            "(2,'Skate Pro',79990,'Rodamientos ABEC-9', 'SKATE')," +
                            "(3,'Roller Basic',39990,'Patines recreativos', 'ROLLER')," +
                            "(4,'Roller Fit',69990,'Patines fitness', 'ROLLER')," +
                            "(5,'BMX Park',119990,'Cuadro cromoly', 'BMX')," +
                            "(6,'BMX Street',149990,'Llantas dobles', 'BMX')"
                    )
                }
            }
        }
    }
}