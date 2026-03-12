package app.jdgn.walletmonitor.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.dialogs.CustomBottomSheet
import app.jdgn.walletmonitor.utils.NumberUtils
import app.jdgn.walletmonitor.viewmodel.AmountInputViewModel
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import walletmonitor.composeapp.generated.resources.Res
import walletmonitor.composeapp.generated.resources.divide
import walletmonitor.composeapp.generated.resources.plus_minus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountInputField(
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    currencyId: Long? = null,
    label: String? = null,
    placeholder: String? = "0.00",
    isError: Boolean = false,
    errorMessage: String? = null,
    customThemeColor: Color? = null,
    customContainerColor: Color? = null
) {
    val viewModel: AmountInputViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(currencyId) {
        viewModel.loadCurrency(currencyId)
    }

    LaunchedEffect(value) {
        if (state.result != value) {
            viewModel.setInitialValue(value)
        }
    }
    
    val errorColor = MaterialTheme.colorScheme.error
    val themeColor = if (isError) errorColor else (customThemeColor ?: MaterialTheme.colorScheme.primary)
    
    val backgroundColor = when {
        isError -> errorColor.copy(alpha = 0.3f)
        customContainerColor != null -> customContainerColor
        else -> MaterialTheme.colorScheme.surface
    }

    val formattedValue = if (value == 0.0) "" else NumberUtils.formatNumber(value)

    Column(modifier = modifier) {
        CustomBox(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.setShowKeypad(true) },
            shadowColor = themeColor,
            padding = 0.dp,
            shadowElevation = if (isError) 6.dp else 4.dp
        ) {
            Box(
                Modifier
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    if (label != null) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = themeColor
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (state.symbolPosition == "prefix") {
                            Text(
                                text = state.symbol,
                                style = MaterialTheme.typography.bodyLarge,
                                color = themeColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Text(
                            text = formattedValue.ifEmpty { placeholder ?: "" },
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (formattedValue.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                        )
                        
                        if (state.symbolPosition == "suffix") {
                            Text(
                                text = state.symbol,
                                style = MaterialTheme.typography.bodyLarge,
                                color = themeColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
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

    if (state.showKeypad) {
        AmountKeypadBottomSheet(
            viewModel = viewModel,
            themeColor = themeColor,
            onDismissRequest = { viewModel.setShowKeypad(false) },
            onDone = {
                onValueChange(it)
                viewModel.setShowKeypad(false)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountKeypadBottomSheet(
    viewModel: AmountInputViewModel,
    themeColor: Color,
    onDismissRequest: () -> Unit,
    onDone: (Double) -> Unit
) {
    val state by viewModel.state.collectAsState()

    BoxWithConstraints {
        val isLandscape = maxWidth > maxHeight
        
        val displayArea = @Composable { isSmall: Boolean ->
            CustomBox(
                modifier = Modifier.fillMaxWidth(),
                shadowColor = themeColor,
                shadowElevation = 4.dp,
                backgroundColor = themeColor.copy(alpha = 0.05f),
                padding = if (isSmall) 12.dp else 16.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = NumberUtils.formatExpression(state.expression),
                        style = if (isSmall) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.End,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(if (isSmall) 2.dp else 4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (state.symbolPosition == "prefix") {
                            Text(
                                text = state.symbol,
                                style = if (isSmall) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = themeColor
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        
                        Text(
                            text = NumberUtils.formatNumber(state.result),
                            style = if (isSmall) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        if (state.symbolPosition == "suffix") {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = state.symbol,
                                style = if (isSmall) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = themeColor
                            )
                        }
                    }
                }
            }
        }

        val keypadArea = @Composable { buttonHeight: Int ->
            CurrencyKeypad(
                themeColor = themeColor,
                expression = state.expression,
                buttonHeight = buttonHeight,
                onKeyClick = { key ->
                    if (key == "DONE") {
                        onDone(state.result)
                    } else {
                        viewModel.onKeyClick(key)
                    }
                }
            )
        }

        CustomBottomSheet(
            onDismissRequest = onDismissRequest,
            skipPartiallyExpanded = true,
            header = if (!isLandscape) { { displayArea(false) } } else null,
            body = {
                if (isLandscape) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(0.4f)) {
                            displayArea(true)
                        }
                        Box(modifier = Modifier.weight(0.6f)) {
                            keypadArea(48)
                        }
                    }
                } else {
                    keypadArea(68)
                }
            }
        )
    }
}

@Composable
fun CurrencyKeypad(
    themeColor: Color,
    expression: String,
    buttonHeight: Int,
    onKeyClick: (String) -> Unit
) {
    val hasOperators = expression.any { it in listOf('+', '*', '/') } || 
                      (expression.lastIndexOf('-') > 0)

    val keys = listOf(
        listOf("AC", "+/-", "%", "DEL"),
        listOf("7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf("0", ".", if (hasOperators) "EQUALS" else "DONE", "+")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { key ->
                    val isAction = key in listOf("DEL", "AC")
                    val isOperation = key in listOf("/", "*", "-", "+", "EQUALS", "DONE", "%", "+/-")
                    
                    val icon: ImageVector? = when(key) {
                        "+" -> Icons.Default.Add
                        "-" -> Icons.Default.Remove
                        "*" -> Icons.Default.Close
                        "/" -> vectorResource(Res.drawable.divide)
                        "%" -> Icons.Default.Percent
                        "+/-" -> vectorResource(Res.drawable.plus_minus)
                        "DEL" -> Icons.AutoMirrored.Filled.Backspace
                        "DONE" -> Icons.Default.Check
                        else -> null
                    }
                    
                    KeyButton(
                        text = if (icon == null && key !in listOf("/", "EQUALS")) {
                            if (key == "+/-") "±" else key
                        } else if (key == "EQUALS") "=" else null,
                        icon = icon,
                        themeColor = themeColor,
                        isOperation = isOperation,
                        isAction = isAction,
                        key = key,
                        height = buttonHeight,
                        onClick = { onKeyClick(key) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun KeyButton(
    text: String?,
    icon: ImageVector? = null,
    themeColor: Color,
    isOperation: Boolean,
    isAction: Boolean,
    key: String,
    height: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDone = key == "DONE"
    
    val containerColor = when {
        isDone -> themeColor
        isOperation -> themeColor.copy(alpha = 0.12f)
        isAction -> MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
        else -> MaterialTheme.colorScheme.surface
    }
    
    val contentColor = when {
        isDone -> MaterialTheme.colorScheme.onPrimary
        isOperation -> themeColor
        isAction -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }

    CustomBox(
        modifier = modifier.height(height.dp),
        onClick = onClick,
        shadowColor = if (isDone) themeColor else themeColor.copy(alpha = 0.3f),
        shadowElevation = if (isDone) 6.dp else 2.dp,
        padding = 0.dp,
        backgroundColor = containerColor,
        rippleColor = if (isDone) Color.White else themeColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(if (height < 60) 22.dp else 26.dp)
                )
            } else if (text != null) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = if (isOperation || isDone) FontWeight.Bold else FontWeight.Medium,
                    fontSize = if (height < 60) 18.sp else 24.sp,
                    color = contentColor
                )
            }
        }
    }
}
