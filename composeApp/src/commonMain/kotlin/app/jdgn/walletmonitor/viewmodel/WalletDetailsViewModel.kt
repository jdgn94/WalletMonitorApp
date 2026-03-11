package app.jdgn.walletmonitor.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class WalletDetailsState(
    val walletId: String = "",
    val walletName: String = "",
    val walletAddress: String = "",
    val walletColor: String = "#000000",
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
)

class WalletDetailsViewModel : ViewModel() {
    
    private val _state = MutableStateFlow(WalletDetailsState())
    val state: StateFlow<WalletDetailsState> = _state.asStateFlow()
    
    fun loadWallet(walletId: String) {
        _state.value = _state.value.copy(isLoading = true, walletId = walletId)
        // Aquí puedes cargar los datos del wallet desde la base de datos
        _state.value = _state.value.copy(isLoading = false)
    }
    
    fun toggleEditing() {
        _state.value = _state.value.copy(isEditing = !_state.value.isEditing)
    }
    
    fun updateWalletName(name: String) {
        _state.value = _state.value.copy(walletName = name)
    }
    
    fun updateWalletAddress(address: String) {
        _state.value = _state.value.copy(walletAddress = address)
    }
    
    fun updateWalletColor(color: String) {
        _state.value = _state.value.copy(walletColor = color)
    }
    
    fun saveWallet() {
        _state.value = _state.value.copy(isLoading = true)
        // Aquí puedes guardar los cambios del wallet
        _state.value = _state.value.copy(isLoading = false, isEditing = false)
    }
}
