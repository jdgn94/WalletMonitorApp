package app.jdgn.walletmonitor.ui.components.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.FadingScroll
import app.jdgn.walletmonitor.ui.components.dialogs.CustomDialog
import app.jdgn.walletmonitor.utils.IconUtils

@Composable
fun IconPickerField(
    selectedIconName: String,
    onIconSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    iconSize: Dp = 32.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    defaultIcon: ImageVector = Icons.Default.AccountBalance
) {
    var showDialog by remember { mutableStateOf(false) }
    
    val groupedIcons = IconUtils.getCategoryIcons()
    val currentIcon = IconUtils.getIconByName(selectedIconName) ?: defaultIcon

    // Envolvemos en Column para mantener la consistencia de alineación con otros campos
    Column(modifier = modifier) {
        CustomBox(
            modifier = Modifier.size(size),
            onClick = { showDialog = true },
            padding = 0.dp,
            margin = 0.dp,
            shadowColor = color,
            shadowElevation = 4.dp, // Sincronizado con el resto del formulario
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = currentIcon,
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )
        }
    }

    if (showDialog) {
        CustomDialog(
            onDismissRequest = { showDialog = false },
            header = { 
                Text(
                    "Select Icon", 
                    style = MaterialTheme.typography.titleLarge,
                    color = color
                )
            },
            body = {
                FadingScroll(
                    modifier = Modifier.fillMaxWidth(),
                    fadeLength = 20.dp
                ) { _ ->
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 60.dp),
                        modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        groupedIcons.forEach { (category, icons) ->
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = color,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                )
                            }
                            
                            items(icons) { customIcon ->
                                val isSelected = customIcon.name == selectedIconName
                                CustomBox(
                                    modifier = Modifier.size(60.dp),
                                    onClick = {
                                        onIconSelected(customIcon.name)
                                        showDialog = false
                                    },
                                    backgroundColor = if (isSelected) color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
                                    shadowColor = if (isSelected) color else Color.Gray.copy(alpha = 0.3f),
                                    shadowElevation = if (isSelected) 4.dp else 2.dp,
                                    borderColor = if (isSelected) color else Color.Transparent,
                                    borderWidth = if (isSelected) 1.dp else 0.dp,
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        imageVector = customIcon.icon,
                                        contentDescription = customIcon.name,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            actions = {
                TextButton(onClick = { showDialog = false }) {
                    Text("CLOSE", color = color)
                }
            },
            shadowColor = color
        )
    }
}
