package app.jdgn.walletmonitor.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.dialogs.CustomDialog

@Composable
fun <T> SelectorField(
    label: String,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    itemIcon: (@Composable (T) -> Unit)? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    leftIcon: ImageVector? = null,
    customThemeColor: Color? = null,
    customContainerColor: Color? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    
    val themeColor = customThemeColor ?: MaterialTheme.colorScheme.primary
    val backgroundColor = customContainerColor ?: MaterialTheme.colorScheme.surface
    val errorColor = MaterialTheme.colorScheme.error
    val shadowColor = if (isError) errorColor else themeColor

    val displayText = selectedItem?.let { itemLabel(it) } ?: placeholder ?: ""

    Column(modifier = modifier) {
        CustomBox(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showDialog = true },
            padding = 0.dp,
            shadowElevation = if (isError) 6.dp else 4.dp,
            shadowColor = shadowColor
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp)
            ) {
                if (label.isNotEmpty()) {
                    Text(
                        text = label,
                        color = shadowColor,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (leftIcon != null) {
                        Icon(
                            imageVector = leftIcon,
                            contentDescription = null,
                            tint = shadowColor,
                            modifier = Modifier.size(20.dp)
                        )
                    } else if (selectedItem != null && itemIcon != null) {
                        Box(modifier = Modifier.size(20.dp)) {
                            itemIcon(selectedItem)
                        }
                    }

                    Text(
                        text = displayText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedItem == null) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) 
                                else MaterialTheme.colorScheme.onSurface,
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = shadowColor,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = errorColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }

    if (showDialog) {
        CustomDialog(
            onDismissRequest = { showDialog = false },
            shadowColor = themeColor,
            backgroundColor = backgroundColor,
            header = {
                Text(
                    text = "Select $label",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = themeColor
                )
            },
            body = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        val isSelected = item == selectedItem
                        
                        CustomBox(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onItemSelected(item)
                                showDialog = false
                            },
                            backgroundColor = if (isSelected) themeColor.copy(alpha = 0.1f) else backgroundColor,
                            shadowElevation = if (isSelected) 4.dp else 0.dp,
                            shadowColor = themeColor,
                            borderColor = if (isSelected) themeColor else Color.Transparent,
                            borderWidth = if (isSelected) 1.dp else 0.dp,
                            padding = 12.dp
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (itemIcon != null) {
                                    Box(modifier = Modifier.size(24.dp)) {
                                        itemIcon(item)
                                    }
                                }
                                
                                Text(
                                    text = itemLabel(item),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (isSelected) themeColor else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            },
            actions = {
                TextButton(onClick = { showDialog = false }) {
                    Text("CANCEL", color = themeColor)
                }
            }
        )
    }
}
