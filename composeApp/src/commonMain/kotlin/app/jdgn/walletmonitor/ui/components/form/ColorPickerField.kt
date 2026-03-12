package app.jdgn.walletmonitor.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.dialogs.CustomDialog
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.github.skydoves.colorpicker.compose.BrightnessSlider

@Composable
fun ColorPickerField(
    selectedColorHex: String,
    onColorSelected: (Color, String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Color",
    customThemeColor: Color? = null,
    customContainerColor: Color? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    
    val currentColor = remember(selectedColorHex) {
        try {
            val hex = selectedColorHex.removePrefix("#")
            if (hex.length == 8) Color(hex.toLong(16))
            else Color(hex.toLong(16) or 0xFF000000L)
        } catch (e: Exception) {
            Color.Gray
        }
    }

    val themeColor = customThemeColor ?: MaterialTheme.colorScheme.primary
    val backgroundColor = customContainerColor ?: MaterialTheme.colorScheme.surface
    val onThemeColor = if (themeColor == MaterialTheme.colorScheme.primary) MaterialTheme.colorScheme.onPrimary else Color.White

    Column(modifier = modifier) {
        CustomBox(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { showDialog = true },
            padding = 0.dp,
            shadowElevation = 4.dp,
            shadowColor = themeColor
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = label,
                    color = themeColor,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(currentColor)
                    )
                    val displayHex = if (selectedColorHex.startsWith("#")) selectedColorHex else "#$selectedColorHex"
                    Text(
                        text = displayHex.uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }

    if (showDialog) {
        val controller = rememberColorPickerController()
        var tempColor by remember { mutableStateOf(currentColor) }
        
        LaunchedEffect(currentColor) {
            controller.selectByColor(currentColor, fromUser = false)
        }

        CustomDialog(
            onDismissRequest = { showDialog = false },
            shadowColor = tempColor,
            backgroundColor = tempColor,
            header = {
                Text(
                    text = "Pick a color", 
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            body = {
                BoxWithConstraints {
                    val isLandscape = maxWidth > maxHeight
                    
                    if (isLandscape) {
                        // En horizontal reorganizamos a Row para que no se corte
                        Row(
                            modifier = Modifier.fillMaxWidth().height(160.dp),
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HsvColorPicker(
                                modifier = Modifier.size(140.dp),
                                controller = controller,
                                onColorChanged = { envelope ->
                                    tempColor = envelope.color
                                },
                                initialColor = currentColor
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Intensity",
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                    BrightnessSlider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(24.dp),
                                        controller = controller
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(tempColor)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    } else {
                        // En vertical se mantiene como estaba originalmente
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            HsvColorPicker(
                                modifier = Modifier.size(200.dp).padding(8.dp),
                                controller = controller,
                                onColorChanged = { envelope ->
                                    tempColor = envelope.color
                                },
                                initialColor = currentColor
                            )

                            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                                Text(
                                    text = "Intensity",
                                    style = MaterialTheme.typography.labelMedium,
                                )
                                BrightnessSlider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(35.dp),
                                    controller = controller
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(tempColor)
                            )
                        }
                    }
                }
            },
            actions = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = tempColor)
                ) {
                    Text("CANCEL") 
                }
                Button(
                    onClick = {
                        val argb = tempColor.toArgb()
                        val hex = "#" + (argb.toUInt().toString(16).padStart(8, '0').takeLast(6)).uppercase()
                        onColorSelected(tempColor, hex)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tempColor,
                        contentColor = onThemeColor
                    )
                ) {
                    Text("SELECT")
                }
            }
        )
    }
}
