package app.jdgn.walletmonitor.database

import com.russhwolf.settings.Settings

class SettingsManager(private val settings: Settings) {
    
    fun getString(key: String, defaultValue: String = ""): String {
        return settings.getString(key, defaultValue)
    }

    fun putString(key: String, value: String) {
        settings.putString(key, value)
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return settings.getInt(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        settings.putInt(key, value)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return settings.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
    }

    fun remove(key: String) {
        settings.remove(key)
    }

    fun clear() {
        settings.clear()
    }
}
