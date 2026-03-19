package app.jdgn.walletmonitor.viewmodel

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChartPoint(
    val x: Float, // Representa el índice o tiempo
    val y: Float, // El valor real
    val label: String
)

data class ChartDataSet(
    val name: String,
    val points: List<ChartPoint>,
    val color: Color
)

data class ChartState(
    val dataSets: List<ChartDataSet> = emptyList(),
    val selectedX: Float? = null,
    val minX: Float = 0f,
    val maxX: Float = 1f,
    val minY: Float = 0f,
    val maxY: Float = 1f
)

class ChartViewModel : ViewModel() {
    private val _state = MutableStateFlow(ChartState())
    val state: StateFlow<ChartState> = _state.asStateFlow()

    fun setData(dataSets: List<ChartDataSet>) {
        if (dataSets.isEmpty()) return
        
        val allPoints = dataSets.flatMap { it.points }
        val minX = allPoints.minOfOrNull { it.x } ?: 0f
        val maxX = allPoints.maxOfOrNull { it.x } ?: 1f
        val minY = allPoints.minOfOrNull { it.y } ?: 0f
        val maxY = allPoints.maxOfOrNull { it.y } ?: 1f

        // Añadir un margen al eje Y
        val yRange = maxY - minY
        val margin = if (yRange == 0f) 1f else yRange * 0.1f

        _state.update { 
            it.copy(
                dataSets = dataSets,
                minX = minX,
                maxX = maxX,
                minY = minY - margin,
                maxY = maxY + margin
            ) 
        }
    }

    fun onPointSelected(x: Float?) {
        _state.update { it.copy(selectedX = x) }
    }
}
