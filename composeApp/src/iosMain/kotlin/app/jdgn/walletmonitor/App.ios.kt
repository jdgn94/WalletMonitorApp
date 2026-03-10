package app.jdgn.walletmonitor

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
actual fun PlatformBackHandler(onBack: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { change, dragAmount ->
                // Si el toque empieza cerca del borde izquierdo (ej. < 40dp)
                // y se desliza hacia la derecha (> 50px), disparamos onBack
                if (change.position.x < 40.dp.toPx() && dragAmount > 50f) {
                    onBack()
                }
            }
        }
    ) {
        content()
    }
}
