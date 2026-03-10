package app.jdgn.walletmonitor.ui.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import walletmonitor.composeapp.generated.resources.*

@Composable
fun ExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CustomDialog(
        onDismissRequest = onDismiss,
        header = {
            Text(
                text = stringResource(Res.string.exit_app_title),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        body = {
            Text(
                text = stringResource(Res.string.exit_app_message),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        actions = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.no))
            }
            TextButton(onClick = onConfirm) {
                Text(stringResource(Res.string.yes))
            }
        }
    )
}
