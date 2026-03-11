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
import androidx.compose.ui.text.font.FontWeight
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
    
    // Convertimos el Hexadecimal de entrada a un objeto Color usable
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
    // Por defecto siempre es surface, solo cambia si se pasa customContainerColor
    val backgroundColor = customContainerColor ?: MaterialTheme.colorScheme.surface

    Column(modifier = modifier) {
        CustomBox(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
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
                // Label estilo focus interno
                Text(
                    text = label,
                    color = themeColor,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
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
        
        // Sincronizar el controlador con el color actual cuando se abre
        LaunchedEffect(currentColor) {
            controller.selectByColor(currentColor, fromUser = false)
        }

        CustomDialog(
            onDismissRequest = { showDialog = false },
            header = {
                Text(
                    text = "Pick a color", 
                    style = MaterialTheme.typography.headlineSmall,
                    color = themeColor
                )
            },
            body = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                        HsvColorPicker(
                            modifier = Modifier.size(220.dp).padding(8.dp),
                            controller = controller,
                            onColorChanged = { envelope ->
                                tempColor = envelope.color
                            },
                            initialColor = currentColor
                        )

                        // Slider de Intensidad (Brillo) de la librería
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                            Text(
                                text = "Intensity",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            BrightnessSlider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(35.dp),
                                controller = controller
                            )
                        }

                        // Previsualización circular en el diálogo
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(tempColor)
                        )
                }
            },
            actions = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = themeColor)
                ) { 
                    Text("CANCEL") 
                }
                Button(
                    onClick = {
                        // Generamos el Hex para la base de datos
                        val argb = tempColor.toArgb()
                        val hex = "#" + (argb.toUInt().toString(16).padStart(8, '0').takeLast(6)).uppercase()
                        onColorSelected(tempColor, hex)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor)
                ) {
                    Text("SELECT")
                }
            }
        )
    }
}
