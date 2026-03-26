package app.jdgn.walletmonitor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class BorderType {
    Solid, Dotted, Dashed, Double
}

enum class ShadowType {
    External, Internal
}

@Composable
fun CustomBox(
    modifier: Modifier = Modifier,
    shadowColor: Color = MaterialTheme.colorScheme.primary,
    shadowElevation: Dp = 8.dp,
    shadowType: ShadowType = ShadowType.External,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    borderType: BorderType = BorderType.Solid,
    padding: Dp = 16.dp,
    margin: Dp = 0.dp,
    width: Dp? = null,
    height: Dp? = null,
    maxWidth: Dp = Dp.Unspecified,
    maxHeight: Dp = Dp.Unspecified,
    onClick: (() -> Unit)? = null,
    rippleColor: Color? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    val shape = MaterialTheme.shapes.medium
    
    val finalRippleColor = rippleColor ?: shadowColor

    // External shadow applies to the whole component
    val shadowModifier = if (shadowType == ShadowType.External && shadowElevation > 0.dp) {
        Modifier.shadow(
            elevation = shadowElevation,
            shape = shape,
            clip = false,
            ambientColor = shadowColor,
            spotColor = shadowColor
        )
    } else {
        Modifier
    }

    val boxModifier = modifier
        .padding(margin)
        .then(if (width != null) Modifier.width(width) else Modifier)
        .then(if (height != null) Modifier.height(height) else Modifier)
        .widthIn(max = maxWidth)
        .heightIn(max = maxHeight)
        .then(shadowModifier)

    val clickableModifier = if (onClick != null) {
        Modifier
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = finalRippleColor),
                onClick = onClick
            )
    } else {
        Modifier
    }

    Surface(
        modifier = boxModifier
            .then(clickableModifier)
            .background(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.shapes.medium
            ),
        shape = shape,
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier
                .drawWithContent {
                    drawContent()

                    val outline = shape.createOutline(size, layoutDirection, this)

                    // Internal Shadow (Inset effect)
                    if (shadowType == ShadowType.Internal && shadowElevation > 0.dp) {
                        val shadowPx = shadowElevation.toPx()
                        val path = Path().apply {
                            when (outline) {
                                is Outline.Rectangle -> addRect(outline.rect)
                                is Outline.Rounded -> addRoundRect(outline.roundRect)
                                is Outline.Generic -> addPath(outline.path)
                            }
                        }
                        clipPath(path) {
                            // Top-left internal shadow
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(shadowColor.copy(alpha = 0.3f), Color.Transparent),
                                    startY = 0f,
                                    endY = shadowPx
                                )
                            )
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(shadowColor.copy(alpha = 0.3f), Color.Transparent),
                                    startX = 0f,
                                    endX = shadowPx
                                )
                            )
                        }
                    }

                    // Custom Borders
                    if (borderWidth > 0.dp && borderColor != Color.Transparent) {
                        val bw = borderWidth.toPx()
                        when (borderType) {
                            BorderType.Solid -> drawOutline(outline, borderColor, style = Stroke(bw))
                            BorderType.Dotted -> drawOutline(
                                outline, borderColor,
                                style = Stroke(
                                    width = bw,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(bw, bw), 0f)
                                )
                            )
                            BorderType.Dashed -> drawOutline(
                                outline, borderColor,
                                style = Stroke(
                                    width = bw,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(bw * 3, bw * 3), 0f)
                                )
                            )
                            BorderType.Double -> {
                                // Draw main stroke and a thinner stroke in the middle with background color
                                drawOutline(outline, borderColor, style = Stroke(bw))
                                drawOutline(outline, backgroundColor, style = Stroke(bw / 3f))
                            }
                        }
                    }
                }
                .padding(padding),
            contentAlignment = contentAlignment
        ) {
            content()
        }
    }
}
