package app.jdgn.walletmonitor.ui.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import walletmonitor.composeapp.generated.resources.*

@Composable
fun ExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val themeColor = MaterialTheme.colorScheme.error
    
    CustomDialog(
        onDismissRequest = onDismiss,
        shadowColor = themeColor,
        header = {
            Text(
                text = stringResource(Res.string.exit_app_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onError
            )
        },
        body = {
            Text(
                text = stringResource(Res.string.exit_app_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onError
            )
        },
        actions = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onError)
            ) {
                Text(stringResource(Res.string.no))
            }
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onError,
                    contentColor = themeColor
                )
            ) {
                Text(stringResource(Res.string.yes))
            }
        }
    )
}
