package app.jdgn.walletmonitor.viewmodel

import app.jdgn.walletmonitor.data.local.WalletRepository
import app.jdgn.walletmonitor.database.Account_types
import app.jdgn.walletmonitor.database.Banks
import app.jdgn.walletmonitor.database.Currencies
import app.jdgn.walletmonitor.database.SelectAllCurrenciesWithType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import app.jdgn.walletmonitor.utils.ColorUtils.colorToHex
import app.jdgn.walletmonitor.utils.ColorUtils.getRandomColor

data class CreateAccountState(
    val name: String = "",
    val icon: String = "bank",
    val selectedBank: Banks? = null,
    val selectedCurrency: SelectAllCurrenciesWithType? = null,
    val selectedAccountType: Account_types? = null,
    val selectedColor: Color = getRandomColor(),
    val selectedColorHex: String = colorToHex(selectedColor),
    val amount: Double = 0.0,
    val banks: List<Banks> = emptyList(),
    val currencies: List<SelectAllCurrenciesWithType> = emptyList(),
    val accountTypes: List<Account_types> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class CreateAccountViewModel(
    private val repository: WalletRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateAccountState())
    val state: StateFlow<CreateAccountState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getAllBanks().collect { banks ->
                _state.value = _state.value.copy(banks = banks)
            }
        }
        viewModelScope.launch {
            repository.getAllCurrenciesWithType().collect { currencies ->
                _state.value = _state.value.copy(currencies = currencies)
            }
        }
        viewModelScope.launch {
            repository.getAllAccountTypes().collect { types ->
                _state.value = _state.value.copy(accountTypes = types)
            }
        }
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun onIconChange(icon: String) {
        _state.value = _state.value.copy(icon = icon)
    }

    fun onBankSelected(bank: Banks) {
        _state.value = _state.value.copy(selectedBank = bank)
    }

    fun onCurrencySelected(currency: SelectAllCurrenciesWithType) {
        _state.value = _state.value.copy(selectedCurrency = currency)
    }

    fun onAccountTypeSelected(type: Account_types) {
        _state.value = _state.value.copy(selectedAccountType = type)
    }

    fun onColorSelected(color: Color, hex: String) {
        _state.value = _state.value.copy(selectedColor = color, selectedColorHex = hex)
    }

    fun onAmountChange(amount: Double) {
        _state.value = _state.value.copy(amount = amount)
    }

    fun saveAccount() {
        val currentState = _state.value
        if (currentState.name.isBlank() || currentState.selectedCurrency == null || currentState.selectedAccountType == null) {
            _state.value = _state.value.copy(error = "Please fill all required fields")
            return
        }

        viewModelScope.launch {
            try {
                repository.insertAccount(
                    bankId = currentState.selectedBank?.id,
                    name = currentState.name,
                    icon = currentState.icon,
                    accountTypeId = currentState.selectedAccountType.id,
                    currencyId = currentState.selectedCurrency.id,
                    color = currentState.selectedColorHex,
                    amount = currentState.amount
                )
                _state.value = _state.value.copy(isSuccess = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
