package app.jdgn.walletmonitor.data.local

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.jdgn.walletmonitor.database.AppDatabase
import app.jdgn.walletmonitor.database.Currencies

class AppConfig(
    private val settingsManager: SettingsManager,
    private val database: AppDatabase
) {
    
    // Variables precargadas
    var themeMode by mutableStateOf("auto")
        private set
    
    var borderRadius by mutableStateOf(12)
        private set

    var language by mutableStateOf("auto")
        private set

    var useDynamicColor by mutableStateOf(false)
        private set

    // Cache de la moneda por defecto
    var defaultCurrency by mutableStateOf<Currencies?>(null)
        private set

    init {
        loadConfig()
    }

    fun loadConfig() {
        themeMode = settingsManager.getString(StorageKeys.THEME_MODE, "auto")
        borderRadius = settingsManager.getInt(StorageKeys.BORDER_RADIUS, 12)
        language = settingsManager.getString(StorageKeys.LANGUAGE, "auto")
        useDynamicColor = settingsManager.getBoolean(StorageKeys.DYNAMIC_COLOR, false)
        
        loadDefaultCurrency()
    }

    private fun loadDefaultCurrency() {
        val savedId = settingsManager.getString(StorageKeys.DEFAULT_CURRENCY_ID, "")
        
        val currency = if (savedId.isNotEmpty()) {
            database.currenciesQueries.selectCurrencyById(savedId.toLong()).executeAsOneOrNull()
        } else {
            // Si no hay ID guardado, buscar USD por defecto
            val usd = database.currenciesQueries.selectCurrencyByCode("USD").executeAsOneOrNull()
            usd?.let { 
                settingsManager.putString(StorageKeys.DEFAULT_CURRENCY_ID, it.id.toString())
            }
            usd
        }
        
        defaultCurrency = currency
    }

    fun updateDefaultCurrency(currency: Currencies) {
        defaultCurrency = currency
        settingsManager.putString(StorageKeys.DEFAULT_CURRENCY_ID, currency.id.toString())
    }

    fun updateThemeMode(mode: String) {
        themeMode = mode
        settingsManager.putString(StorageKeys.THEME_MODE, mode)
    }

    fun updateBorderRadius(radius: Int) {
        borderRadius = radius
        settingsManager.putInt(StorageKeys.BORDER_RADIUS, radius)
    }

    fun updateLanguage(lang: String) {
        language = lang
        settingsManager.putString(StorageKeys.LANGUAGE, lang)
    }

    fun updateDynamicColor(use: Boolean) {
        useDynamicColor = use
        settingsManager.putBoolean(StorageKeys.DYNAMIC_COLOR, use)
    }
}
