package app.movil.parcial2.ui.screens.catalog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.movil.parcial2.data.RepositorioProductoImpl
import app.movil.parcial2.data.local.BaseDeDatos
import app.movil.parcial2.domain.model.Category
import app.movil.parcial2.domain.model.Producto
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ModeloVistaCatalogo(private val repo: RepositorioProductoImpl): ViewModel() {
    val skate: StateFlow<List<Producto>> = repo.observeByCategory(Category.SKATE).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val roller: StateFlow<List<Producto>> = repo.observeByCategory(Category.ROLLER).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val bmx: StateFlow<List<Producto>> = repo.observeByCategory(Category.BMX).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
