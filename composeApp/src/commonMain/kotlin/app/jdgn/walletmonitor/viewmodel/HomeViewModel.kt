package app.jdgn.walletmonitor.viewmodel

import app.jdgn.walletmonitor.data.local.WalletRepository
import app.jdgn.walletmonitor.database.Accounts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HomeScreenState(
    val wallets: List<Accounts> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val repository: WalletRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()
    
    fun loadWallets() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.getAllAccounts().collectLatest { accounts ->
                _state.value = _state.value.copy(
                    wallets = accounts,
                    isLoading = false
                )
            }
        }
    }
}
