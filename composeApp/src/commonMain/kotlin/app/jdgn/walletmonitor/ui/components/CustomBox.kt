package app.jdgn.walletmonitor.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomBox(
    modifier: Modifier = Modifier,
    shadowColor: Color = MaterialTheme.colorScheme.primary,
    shadowElevation: Dp = 8.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    padding: Dp = 16.dp,
    margin: Dp = 0.dp,
    width: Dp? = null,
    height: Dp? = null,
    maxWidth: Dp = Dp.Unspecified,
    maxHeight: Dp = Dp.Unspecified,
    onClick: (() -> Unit)? = null,
    rippleColor: Color? = null,
    content: @Composable () -> Unit
) {
    val shape = MaterialTheme.shapes.medium
    
    val finalRippleColor = rippleColor ?: shadowColor

    // Modificador base con tamaño, margen y sombra
    val boxModifier = modifier
        .padding(margin)
        .then(if (width != null) Modifier.width(width) else Modifier)
        .then(if (height != null) Modifier.height(height) else Modifier)
        .widthIn(max = maxWidth)
        .heightIn(max = maxHeight)
        .shadow(
            elevation = shadowElevation,
            shape = shape,
            clip = false,
            ambientColor = shadowColor,
            spotColor = shadowColor
        )

    // Si se pasa una función onClick, añadimos el recorte y el comportamiento clickable con ripple personalizado
    val clickableModifier = if (onClick != null) {
        Modifier
            .clip(shape) // Asegura que el ripple respete los bordes redondeados
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = finalRippleColor),
                onClick = onClick
            )
    } else {
        Modifier
    }

    Surface(
        modifier = boxModifier.then(clickableModifier),
        shape = shape,
        color = backgroundColor,
        border = if (borderWidth > 0.dp) BorderStroke(borderWidth, borderColor) else null
    ) {
        Box(
            modifier = Modifier.padding(padding)
        ) {
            content()
        }
    }
}
