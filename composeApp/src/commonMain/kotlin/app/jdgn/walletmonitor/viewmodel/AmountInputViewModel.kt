package app.jdgn.walletmonitor.viewmodel

import app.jdgn.walletmonitor.data.local.SettingsManager
import app.jdgn.walletmonitor.data.local.StorageKeys
import app.jdgn.walletmonitor.data.local.WalletRepository
import app.jdgn.walletmonitor.database.Currencies
import app.jdgn.walletmonitor.utils.NumberUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AmountInputState(
    val expression: String = "",
    val currency: Currencies? = null,
    val symbol: String = "$",
    val symbolPosition: String = "prefix",
    val result: Double = 0.0,
    val showKeypad: Boolean = false
)

class AmountInputViewModel(
    private val settingsManager: SettingsManager,
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AmountInputState())
    val state: StateFlow<AmountInputState> = _state.asStateFlow()

    fun loadCurrency(currencyId: Long?) {
        val defaultCurrencyId = settingsManager.getInt(StorageKeys.DEFAULT_CURRENCY_ID, 1).toLong()
        val targetCurrencyId = currencyId ?: defaultCurrencyId
        val symbolPosition = settingsManager.getString(StorageKeys.CURRENCY_SYMBOL_POSITION, "prefix")

        _state.value = _state.value.copy(symbolPosition = symbolPosition)

        viewModelScope.launch {
            walletRepository.getCurrencyById(targetCurrencyId).collect { currency ->
                _state.value = _state.value.copy(
                    currency = currency,
                    symbol = currency?.symbol ?: "$"
                )
            }
        }
    }

    fun setInitialValue(value: Double) {
        val initialExpression = if (value == 0.0) "" else if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
        _state.value = _state.value.copy(
            expression = initialExpression,
            result = value
        )
    }

    fun setShowKeypad(show: Boolean) {
        _state.value = _state.value.copy(showKeypad = show)
    }

    fun onKeyClick(key: String) {
        val currentExpression = _state.value.expression
        var newExpression = currentExpression

        when (key) {
            "DEL" -> {
                if (currentExpression.isNotEmpty()) {
                    newExpression = currentExpression.dropLast(1)
                }
            }
            "AC" -> {
                newExpression = ""
            }
            "+/-" -> {
                newExpression = toggleSign(currentExpression)
            }
            "%" -> {
                newExpression = applyPercentage(currentExpression)
            }
            "EQUALS" -> {
                val res = NumberUtils.evaluateExpression(currentExpression)
                newExpression = if (res % 1.0 == 0.0) res.toLong().toString() else res.toString()
            }
            else -> {
                val isOp = key in listOf("+", "-", "*", "/")
                val lastIsOp = currentExpression.isNotEmpty() && currentExpression.last() in listOf('+', '-', '*', '/')
                if (isOp && lastIsOp) {
                    newExpression = currentExpression.dropLast(1) + key
                } else {
                    newExpression += key
                }
            }
        }

        val newResult = NumberUtils.evaluateExpression(newExpression)
        _state.value = _state.value.copy(
            expression = newExpression,
            result = newResult
        )
    }

    private fun toggleSign(expr: String): String {
        if (expr.isEmpty()) return "-"
        val operators = listOf('+', '-', '*', '/')
        
        var lastOpIndex = -1
        for (i in expr.length - 1 downTo 0) {
            if (expr[i] in operators) {
                val isSign = i == 0 || expr[i-1] in operators
                if (!isSign) {
                    lastOpIndex = i
                    break
                }
            }
        }
        
        val prefix = if (lastOpIndex == -1) "" else expr.substring(0, lastOpIndex + 1)
        val lastNumber = if (lastOpIndex == -1) expr else expr.substring(lastOpIndex + 1)
        
        return if (lastNumber.startsWith("-")) {
            prefix + lastNumber.substring(1)
        } else {
            "$prefix-$lastNumber"
        }
    }

    private fun applyPercentage(expr: String): String {
        if (expr.isEmpty()) return ""
        val operators = listOf('+', '-', '*', '/')
        
        var lastOpIndex = -1
        for (i in expr.length - 1 downTo 0) {
            if (expr[i] in operators && (i != 0 && expr[i-1] !in operators)) {
                lastOpIndex = i
                break
            }
        }
        
        if (lastOpIndex == -1) {
            val baseValue = NumberUtils.evaluateExpression(expr)
            return (baseValue / 100.0).toString()
        }
        
        val operator = expr[lastOpIndex]
        val firstPart = expr.substring(0, lastOpIndex)
        val lastNumberStr = expr.substring(lastOpIndex + 1)
        
        val lastNumberValue = lastNumberStr.toDoubleOrNull() ?: return expr
        
        return if (operator == '+' || operator == '-') {
            val baseValue = NumberUtils.evaluateExpression(firstPart)
            val percentValue = baseValue * (lastNumberValue / 100.0)
            firstPart + operator + percentValue.toString()
        } else {
            firstPart + operator + (lastNumberValue / 100.0).toString()
        }
    }
}
