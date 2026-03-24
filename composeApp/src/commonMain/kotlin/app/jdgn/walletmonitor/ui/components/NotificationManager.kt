package app.jdgn.walletmonitor.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

enum class NotificationType(
    val defaultIcon: ImageVector,
    val color: Color
) {
    SUCCESS(Icons.Default.CheckCircle, Color(0xFF4CAF50)),
    ERROR(Icons.Default.Error, Color(0xFFF44336)),
    WARNING(Icons.Default.Warning, Color(0xFFFF9800)),
    INFO(Icons.Default.Info, Color(0xFF2196F3))
}

data class NotificationData(
    val title: String,
    val message: String,
    val type: NotificationType,
    val icon: ImageVector? = null,
    val duration: Long = 3000L
)

private val LocalNotificationManager = staticCompositionLocalOf<NotificationState> {
    error("No NotificationManager provided")
}

class NotificationState {
    var currentNotification by mutableStateOf<NotificationData?>(null)
        private set
    
    // Flag interno para controlar la visibilidad del AnimatedVisibility
    var isVisible by mutableStateOf(false)
        private set

    fun show(
        title: String,
        message: String,
        type: NotificationType = NotificationType.INFO,
        icon: ImageVector? = null,
        duration: Long = 3000L
    ) {
        currentNotification = NotificationData(title, message, type, icon, duration)
        isVisible = true
    }

    fun dismiss() {
        isVisible = false
    }
    
    fun clearData() {
        currentNotification = null
    }
}

@Composable
fun NotificationProvider(content: @Composable () -> Unit) {
    val state = remember { NotificationState() }

    CompositionLocalProvider(LocalNotificationManager provides state) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
            
            val notification = state.currentNotification
            
            LaunchedEffect(notification, state.isVisible) {
                if (notification != null && state.isVisible) {
                    delay(notification.duration)
                    state.dismiss()
                }
            }

            AnimatedVisibility(
                visible = state.isVisible,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(durationMillis = 400)
                ) + fadeIn(animationSpec = tween(durationMillis = 400)),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(durationMillis = 400)
                ) + fadeOut(animationSpec = tween(durationMillis = 400)),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(9999f)
            ) {
                // Al terminar la animación de salida, limpiamos los datos
                DisposableEffect(Unit) {
                    onDispose {
                        state.clearData()
                    }
                }

                notification?.let { data ->
                    NotificationContent(
                        data = data,
                        onDismiss = { state.dismiss() }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationContent(data: NotificationData, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .widthIn(max = 500.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        color = data.type.color,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Contenido Principal
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 24.dp, end = 48.dp, top = 16.dp, bottom = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = data.icon ?: data.type.defaultIcon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                    
                    Column {
                        Text(
                            text = data.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = data.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Botón X para cerrar
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 4.dp, end = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun rememberNotification(): NotificationState {
    return LocalNotificationManager.current
}
