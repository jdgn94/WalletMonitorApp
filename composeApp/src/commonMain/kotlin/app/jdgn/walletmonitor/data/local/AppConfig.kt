package app.jdgn.walletmonitor.data.local

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AppConfig(private val settingsManager: SettingsManager) {
    
    // Variables precargadas
    var themeMode by mutableStateOf("auto")
        private set
    
    var borderRadius by mutableStateOf(12)
        private set

    var language by mutableStateOf("auto")
        private set

    var useDynamicColor by mutableStateOf(false)
        private set

    init {
        loadConfig()
    }

    fun loadConfig() {
        themeMode = settingsManager.getString(StorageKeys.THEME_MODE, "auto")
        borderRadius = settingsManager.getInt(StorageKeys.BORDER_RADIUS, 12)
        language = settingsManager.getString(StorageKeys.LANGUAGE, "auto")
        useDynamicColor = settingsManager.getBoolean(StorageKeys.DYNAMIC_COLOR, false)
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
