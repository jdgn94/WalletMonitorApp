package app.jdgn.walletmonitor.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.ui.components.CustomBox

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    clearable: Boolean = false,
    customThemeColor: Color? = null,
    customContainerColor: Color? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE
) {
    val errorColor = MaterialTheme.colorScheme.error
    val shadowColor = if (isError) errorColor else (customThemeColor ?: MaterialTheme.colorScheme.primary)
    
    // El color de fondo es surface por defecto, a menos que sea error o se pase un color específico
    val backgroundColor = when {
        isError -> errorColor.copy(alpha = 0.3f)
        customContainerColor != null -> customContainerColor
        else -> MaterialTheme.colorScheme.surface
    }
    
    val iconColor = shadowColor // Color sólido igual a la sombra sin alpha

    val leadingIconCompose: @Composable (() -> Unit)? = leftIcon?.let {
        {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = iconColor
            )
        }
    }

    val trailingIconCompose: @Composable (() -> Unit)? = {
        if (clearable && value.isNotEmpty()) {
            IconButton(onClick = { onValueChange("") }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        } else if (rightIcon != null) {
            Icon(
                imageVector = rightIcon,
                contentDescription = null,
                tint = iconColor
            )
        }
    }

    Column(modifier = modifier) {
        CustomBox(
            modifier = Modifier.fillMaxWidth(),
            shadowColor = shadowColor,
            padding = 0.dp,
            shadowElevation = if (isError) 6.dp else 4.dp
        ) {
            Box(
                Modifier.background(backgroundColor)
            ) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = label?.let { { Text(it) } },
                    placeholder = placeholder?.let { { Text(it) } },
                    isError = isError,
                    leadingIcon = leadingIconCompose,
                    trailingIcon = if ((clearable && value.isNotEmpty()) || rightIcon != null) trailingIconCompose else null,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedLabelColor = shadowColor,
                        unfocusedLabelColor = shadowColor.copy(alpha = 0.7f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = shadowColor,
                        errorLabelColor = errorColor,
                        errorCursorColor = errorColor,
                        errorSupportingTextColor = errorColor
                    )
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
}
