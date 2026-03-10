package app.jdgn.walletmonitor.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Un contenedor con scroll que aplica un efecto de difuminado (fade) en los extremos.
 *
 * @param modifier Modificador para el contenedor externo.
 * @param isVertical Define si el scroll es vertical (true) u horizontal (false).
 * @param fadeLength La longitud del área de difuminado en los extremos.
 * @param scrollState El estado del scroll para controlar la posición.
 * @param padding Espaciado interno general. Permite separar margen horizontal y vertical en una sola propiedad.
 * @param content El contenido scrollable. Recibe un modificador que ya incluye el scroll y el safe area automático.
 */
@Composable
fun FadingScroll(
    modifier: Modifier = Modifier,
    isVertical: Boolean = true,
    fadeLength: Dp = 24.dp,
    scrollState: ScrollState = rememberScrollState(),
    padding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (Modifier) -> Unit
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val fadeLengthPx = with(density) { fadeLength.toPx() }

    Box(
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()
                
                if (fadeLengthPx > 0) {
                    if (isVertical) {
                        // Difuminado Superior
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 0f,
                                endY = fadeLengthPx
                            ),
                            blendMode = BlendMode.DstIn
                        )
                        // Difuminado Inferior
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Black, Color.Transparent),
                                startY = size.height - fadeLengthPx,
                                endY = size.height
                            ),
                            blendMode = BlendMode.DstIn
                        )
                    } else {
                        // Difuminado Izquierdo
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startX = 0f,
                                endX = fadeLengthPx
                            ),
                            blendMode = BlendMode.DstIn
                        )
                        // Difuminado Derecho
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Black, Color.Transparent),
                                startX = size.width - fadeLengthPx,
                                endX = size.width
                            ),
                            blendMode = BlendMode.DstIn
                        )
                    }
                }
            }
    ) {
        // Calculamos el padding final sumando el fadeLength como safe area solo en el eje del scroll
        val finalPadding = if (isVertical) {
            PaddingValues(
                top = padding.calculateTopPadding() + fadeLength,
                bottom = padding.calculateBottomPadding() + fadeLength,
                start = padding.calculateStartPadding(layoutDirection),
                end = padding.calculateEndPadding(layoutDirection)
            )
        } else {
            PaddingValues(
                start = padding.calculateStartPadding(layoutDirection) + fadeLength,
                end = padding.calculateEndPadding(layoutDirection) + fadeLength,
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            )
        }

        val scrollModifier = if (isVertical) {
            Modifier
                .verticalScroll(scrollState)
                .padding(finalPadding)
        } else {
            Modifier
                .horizontalScroll(scrollState)
                .padding(finalPadding)
        }
        
        content(scrollModifier)
    }
}
