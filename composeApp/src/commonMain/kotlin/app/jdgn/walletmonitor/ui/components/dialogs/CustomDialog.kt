package app.jdgn.walletmonitor.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.jdgn.walletmonitor.ui.components.CustomBox

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    shadowColor: Color = MaterialTheme.colorScheme.primary,
    header: @Composable (() -> Unit)? = null,
    body: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        CustomBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            padding = 0.dp, // Manejamos el padding internamente para separar secciones
            shadowElevation = 12.dp,
            shadowColor = shadowColor,
            backgroundColor = shadowColor.copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cabecera
                header?.let {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        it()
                    }
                }

                // Cuerpo
                body?.let {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        it()
                    }
                }

                // Acciones (Row)
                actions?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it()
                    }
                }
            }
        }
    }
}
