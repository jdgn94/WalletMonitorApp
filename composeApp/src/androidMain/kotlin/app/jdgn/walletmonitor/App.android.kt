package app.jdgn.walletmonitor

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(onBack: () -> Unit, content: @Composable () -> Unit) {
    BackHandler(onBack = onBack)
    content()
}
