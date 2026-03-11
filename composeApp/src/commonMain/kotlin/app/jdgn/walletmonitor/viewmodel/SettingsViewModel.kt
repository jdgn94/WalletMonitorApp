package app.jdgn.walletmonitor.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsScreenState(
    val themeMode: String = "system",
    val useDynamicColor: Boolean = false,
    val borderRadius: Int = 12,
    val isLoading: Boolean = false
)

class SettingsViewModel : ViewModel() {
    
    private val _state = MutableStateFlow(SettingsScreenState())
    val state: StateFlow<SettingsScreenState> = _state.asStateFlow()
    
    fun setThemeMode(mode: String) {
        _state.value = _state.value.copy(themeMode = mode)
    }
    
    fun setDynamicColor(enabled: Boolean) {
        _state.value = _state.value.copy(useDynamicColor = enabled)
    }
    
    fun setBorderRadius(radius: Int) {
        _state.value = _state.value.copy(borderRadius = radius)
    }
    
    fun saveSettings() {
        _state.value = _state.value.copy(isLoading = true)
        // Aquí puedes guardar la configuración
        _state.value = _state.value.copy(isLoading = false)
    }
}
