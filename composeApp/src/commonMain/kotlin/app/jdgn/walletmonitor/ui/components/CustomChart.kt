package app.jdgn.walletmonitor.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.jdgn.walletmonitor.viewmodel.ChartDataSet
import app.jdgn.walletmonitor.viewmodel.ChartViewModel
import org.koin.compose.koinInject
import kotlin.math.abs

@OptIn(ExperimentalTextApi::class)
@Composable
fun CustomChart(
    modifier: Modifier = Modifier,
    dataSets: List<ChartDataSet>,
    title: String? = null,
    viewModel: ChartViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    val textMeasurer = rememberTextMeasurer()
    
    // Animación de entrada (Efecto Lápiz)
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(dataSets) {
        viewModel.setData(dataSets)
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
        )
    }

    CustomBox(
        modifier = modifier.fillMaxWidth(),
        shadowType = ShadowType.Internal,
        shadowElevation = 4.dp,
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val xRatio = offset.x / size.width
                            val xVal = state.minX + xRatio * (state.maxX - state.minX)
                            viewModel.onPointSelected(xVal)
                        }
                    }
            ) {
                val width = constraints.maxWidth.toFloat()
                val height = constraints.maxHeight.toFloat()
                val selectionColor = MaterialTheme.colorScheme.primary
                
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val rangeX = state.maxX - state.minX
                    val rangeY = state.maxY - state.minY
                    
                    if (rangeX <= 0f || rangeY <= 0f) return@Canvas

                    val progress = animationProgress.value
                    val zeroY = height - ((0f - state.minY) / rangeY) * height

                    // Dibujar Ejes y Rejilla
                    if (zeroY in 0f..height) {
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.5f),
                            start = Offset(0f, zeroY),
                            end = Offset(width, zeroY),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    }

                    // Dibujar DataSets
                    state.dataSets.forEach { dataSet ->
                        val fullPath = Path()
                        var prevX = 0f
                        var prevY = 0f
                        var startX = 0f

                        // 1. Construir el Path completo suavizado
                        dataSet.points.forEachIndexed { index, point ->
                            val x = ((point.x - state.minX) / rangeX) * width
                            val y = height - ((point.y - state.minY) / rangeY) * height

                            if (index == 0) {
                                fullPath.moveTo(x, y)
                                startX = x
                            } else {
                                val controlX = prevX + (x - prevX) / 2f
                                fullPath.cubicTo(controlX, prevY, controlX, y, x, y)
                            }
                            prevX = x
                            prevY = y
                        }

                        // 2. Usar PathMeasure para obtener el segmento según el progreso
                        val pathMeasure = PathMeasure()
                        pathMeasure.setPath(fullPath, false)
                        val totalLength = pathMeasure.length
                        val drawPath = Path()
                        pathMeasure.getSegment(0f, progress * totalLength, drawPath, true)

                        // 3. Dibujar Relleno que sigue a la línea
                        if (progress > 0f) {
                            val fillPath = Path()
                            fillPath.addPath(drawPath)
                            val currentPos = pathMeasure.getPosition(progress * totalLength)
                            fillPath.lineTo(currentPos.x, zeroY.coerceIn(0f, height))
                            fillPath.lineTo(startX, zeroY.coerceIn(0f, height))
                            fillPath.close()

                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(dataSet.color.copy(alpha = 0.3f), Color.Transparent),
                                    startY = 0f,
                                    endY = height
                                )
                            )
                        }

                        // 4. Dibujar la línea (Efecto Lápiz)
                        drawPath(
                            path = drawPath,
                            color = dataSet.color,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                        
                        // 5. Brillo/Punta del lápiz
                        if (progress > 0f && progress < 1f) {
                            val tipPos = pathMeasure.getPosition(progress * totalLength)
                            drawCircle(
                                color = dataSet.color,
                                radius = 4.dp.toPx(),
                                center = tipPos,
                                alpha = 0.8f
                            )
                        }
                    }

                    // Tooltip y línea de selección (se habilitan al terminar el dibujo)
                    if (progress > 0.98f) {
                        state.selectedX?.let { selX ->
                            val xPos = ((selX - state.minX) / rangeX) * width
                            
                            drawLine(
                                color = selectionColor,
                                start = Offset(xPos, 0f),
                                end = Offset(xPos, height),
                                strokeWidth = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                            )

                            val tooltipInfo = state.dataSets.mapNotNull { ds ->
                                ds.points.minByOrNull { abs(it.x - selX) }?.let { ds to it }
                            }

                            if (tooltipInfo.isNotEmpty()) {
                                val tooltipPadding = 8.dp.toPx()
                                val colorIndicatorSize = 8.dp.toPx()
                                val lineSpacing = 4.dp.toPx()
                                
                                val measuredLines = tooltipInfo.map { (ds, point) ->
                                    val text = AnnotatedString("${ds.name}: ${point.y}")
                                    val result = textMeasurer.measure(
                                        text = text,
                                        style = TextStyle(
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Triple(ds.color, text, result)
                                }

                                val maxTextWidth = measuredLines.maxOf { it.third.size.width }
                                val totalTextHeight = measuredLines.sumOf { it.third.size.height } + (measuredLines.size - 1) * lineSpacing
                                
                                val boxWidth = maxTextWidth + colorIndicatorSize + (tooltipPadding * 3)
                                val boxHeight = totalTextHeight + (tooltipPadding * 2)
                                
                                val isOnRightSide = xPos > width / 2
                                val boxX = if (isOnRightSide) {
                                    (xPos - boxWidth - 16.dp.toPx()).coerceAtLeast(8.dp.toPx())
                                } else {
                                    (xPos + 16.dp.toPx()).coerceAtMost(width - boxWidth - 8.dp.toPx())
                                }
                                val boxY = 8.dp.toPx()

                                drawRoundRect(
                                    color = Color.Black.copy(alpha = 0.8f),
                                    topLeft = Offset(boxX, boxY),
                                    size = Size(boxWidth, boxHeight),
                                    cornerRadius = CornerRadius(4.dp.toPx())
                                )

                                var currentY = boxY + tooltipPadding
                                measuredLines.forEach { (color, _, result) ->
                                    drawCircle(
                                        color = color,
                                        radius = colorIndicatorSize / 2,
                                        center = Offset(boxX + tooltipPadding + colorIndicatorSize / 2, currentY + result.size.height / 2)
                                    )
                                    drawText(
                                        textLayoutResult = result,
                                        topLeft = Offset(boxX + tooltipPadding * 2 + colorIndicatorSize, currentY)
                                    )
                                    currentY += result.size.height + lineSpacing
                                }

                                tooltipInfo.forEach { (ds, point) ->
                                    val px = ((point.x - state.minX) / rangeX) * width
                                    val py = height - ((point.y - state.minY) / rangeY) * height
                                    
                                    drawCircle(
                                        color = ds.color,
                                        radius = 5.dp.toPx(),
                                        center = Offset(px, py)
                                    )
                                    drawCircle(
                                        color = Color.White,
                                        radius = 2.dp.toPx(),
                                        center = Offset(px, py)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
