package app.movil.parcial2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import app.movil.parcial2.data.local.dao.CarritoDAO
import app.movil.parcial2.data.local.dao.CategoriaDao
import app.movil.parcial2.data.local.dao.PedidoDao
import app.movil.parcial2.data.local.dao.ProductoDao
import app.movil.parcial2.data.local.entidades.EntidadCategoria
import app.movil.parcial2.data.local.entidades.EntidadItemCarrito
import app.movil.parcial2.data.local.entidades.EntidadPedido
import app.movil.parcial2.data.local.entidades.EntidadPedidoItem
import app.movil.parcial2.data.local.entidades.EntidadProducto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        EntidadProducto::class,
        EntidadItemCarrito::class,
        EntidadPedido::class,
        EntidadPedidoItem::class,
        EntidadCategoria::class
    ],
    version = 2
)
abstract class BaseDeDatos : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDAO
    abstract fun pedidoDao(): PedidoDao
    abstract fun categoriaDao(): CategoriaDao

    companion object {
        @Volatile private var INSTANCE: BaseDeDatos? = null

        fun get(context: Context): BaseDeDatos =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BaseDeDatos::class.java,
                    "app.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(seedCallback)
                    .build()
                    .also { INSTANCE = it }
            }

        private val seedCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    db.execSQL(
                        """
                        INSERT INTO productos(id,name,price,description,category) VALUES
                        (1,'Skate Street',49990,'Tabla de arce 8.0','SKATE'),
                        (2,'Skate Pro',79990,'Rodamientos ABEC-9','SKATE'),
                        (3,'Roller Basic',39990,'Patines recreativos','ROLLER'),
                        (4,'Roller Fit',69990,'Patines fitness','ROLLER'),
                        (5,'BMX Park',119990,'Cuadro cromoly','BMX'),
                        (6,'BMX Street',149990,'Llantas dobles','BMX')
                        """.trimIndent()
                    )

                    db.execSQL(
                        """
                        INSERT INTO categorias(nombre) VALUES
                        ('SKATE'),
                        ('ROLLER'),
                        ('BMX')
                        """.trimIndent()
                    )
                }
            }
        }
    }
}
