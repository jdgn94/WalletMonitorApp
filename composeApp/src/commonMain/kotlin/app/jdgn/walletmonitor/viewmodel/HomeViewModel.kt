package app.jdgn.walletmonitor.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeScreenState(
    val walletCount: Int = 0,
    val isLoading: Boolean = false
)

class HomeViewModel : ViewModel() {
    
    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()
    
    fun loadWallets() {
        _state.value = _state.value.copy(isLoading = true)
        // Aquí puedes cargar los walltes de la base de datos
        _state.value = _state.value.copy(isLoading = false)
    }
}
