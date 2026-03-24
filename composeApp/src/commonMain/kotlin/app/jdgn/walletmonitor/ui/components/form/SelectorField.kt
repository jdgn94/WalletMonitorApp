package app.jdgn.walletmonitor.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.FadingScroll
import app.jdgn.walletmonitor.ui.components.dialogs.CustomDialog
import androidx.compose.foundation.rememberScrollState

@Composable
fun <T> SelectorField(
    label: String,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    searchFilter: (T) -> String = itemLabel,
    itemIcon: (@Composable (T) -> Unit)? = null,
    customItemContent: (@Composable (T) -> Unit)? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    leftIcon: ImageVector? = null,
    customThemeColor: Color? = null,
    customContainerColor: Color? = null,
    showSearch: Boolean = true,
    itemShadowColor: ((T) -> Color)? = null,
    itemSelectedBackgroundColor: ((T) -> Color)? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    val themeColor = customThemeColor ?: MaterialTheme.colorScheme.primary
    val backgroundColor = customContainerColor ?: MaterialTheme.colorScheme.surface
    val errorColor = MaterialTheme.colorScheme.error
    val shadowColor = if (isError) errorColor else themeColor

    val displayText = selectedItem?.let { itemLabel(it) } ?: placeholder ?: ""

    val filteredItems = remember(items, searchQuery) {
        if (searchQuery.isBlank()) items
        else items.filter { searchFilter(it).contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = modifier) {
        CustomBox(
            modifier = Modifier.fillMaxWidth(),
            onClick = { 
                searchQuery = ""
                showDialog = true 
            },
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
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Select $label",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = themeColor
                    )
                    
                    if (showSearch) {
                        CustomTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = "Search...",
                            leftIcon = Icons.Default.Search,
                            customThemeColor = themeColor,
                            clearable = true
                        )
                    }
                }
            },
            body = {
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val columns = if (maxWidth < 400.dp) 2 else 3
                    
                    FadingScroll(
                        modifier = Modifier.fillMaxWidth(),
                        fadeLength = 20.dp
                    ) { _ ->
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredItems) { item ->
                                val isSelected = item == selectedItem
                                val currentItemShadowColor = itemShadowColor?.invoke(item) ?: themeColor
                                val currentItemBGColor = if (isSelected) {
                                    itemSelectedBackgroundColor?.invoke(item) ?: themeColor.copy(alpha = 0.1f)
                                } else {
                                    backgroundColor
                                }

                                CustomBox(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp),
                                    onClick = {
                                        onItemSelected(item)
                                        showDialog = false
                                    },
                                    backgroundColor = currentItemBGColor,
                                    shadowElevation = 4.dp,
                                    shadowColor = currentItemShadowColor,
                                    borderColor = if (isSelected) currentItemShadowColor else Color.Transparent,
                                    borderWidth = if (isSelected) 1.dp else 0.dp,
                                    padding = 8.dp
                                ) {
                                    if (customItemContent != null) {
                                        customItemContent(item)
                                    } else {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            if (itemIcon != null) {
                                                Box(modifier = Modifier.size(24.dp)) {
                                                    itemIcon(item)
                                                }
                                            }
                                            
                                            Text(
                                                text = itemLabel(item),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = if (isSelected) themeColor else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
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
