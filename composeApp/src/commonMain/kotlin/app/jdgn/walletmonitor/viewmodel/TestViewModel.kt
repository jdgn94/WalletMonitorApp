package app.jdgn.walletmonitor.viewmodel

import androidx.compose.ui.graphics.Color
import app.jdgn.walletmonitor.utils.ColorUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TestScreenState(
    val searchText: String = "",
    val componentColor: Color = ColorUtils.getRandomColor(),
    val selectedColorHex: String = ColorUtils.colorToHex(componentColor),
    val isLoading: Boolean = false,
    val isSearchError: Boolean = false
)

class TestViewModel : ViewModel() {
    
    private val _state = MutableStateFlow(TestScreenState())
    val state: StateFlow<TestScreenState> = _state.asStateFlow()
    
    fun updateSearchText(text: String) {
        _state.value = _state.value.copy(
            searchText = text,
            isSearchError = text == "error"
        )
    }
    
    fun onColorSelected(color: Color, hex: String) {
        _state.value = _state.value.copy(
            selectedColorHex = hex,
            componentColor = color
        )
    }
    
    fun generateRandomColor() {
        val randomColor = ColorUtils.getRandomColor()
        val hex = ColorUtils.colorToHex(randomColor)
        _state.value = _state.value.copy(
            selectedColorHex = hex,
            componentColor = randomColor
        )
    }
}
