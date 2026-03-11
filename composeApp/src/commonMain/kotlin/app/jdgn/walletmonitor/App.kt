package app.jdgn.walletmonitor

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.jdgn.walletmonitor.data.local.AppConfig
import app.jdgn.walletmonitor.data.local.DatabaseSeeder
import app.jdgn.walletmonitor.navigation.Screen
import app.jdgn.walletmonitor.navigation.rememberNavigator
import app.jdgn.walletmonitor.ui.components.dialogs.ExitDialog
import app.jdgn.walletmonitor.ui.screens.HomeScreen
import app.jdgn.walletmonitor.ui.screens.SettingsScreen
import app.jdgn.walletmonitor.ui.screens.TestScreen
import app.jdgn.walletmonitor.ui.screens.WalletDetailsScreen
import app.jdgn.walletmonitor.ui.theme.WalletMonitorTheme
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val appConfig: AppConfig = koinInject()
    val seeder: DatabaseSeeder = koinInject()
    
    // Ejecutamos la semilla al iniciar la aplicación
    // La función seed() internamente verifica si las tablas están vacías
    LaunchedEffect(Unit) {
        seeder.seed()
    }
    
    val isDarkTheme = when (appConfig.themeMode) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    val navigator = rememberNavigator(Screen.Home)
    var showExitDialog by remember { mutableStateOf(false) }

    WalletMonitorTheme(
        darkTheme = isDarkTheme,
        dynamicColor = appConfig.useDynamicColor,
        borderRadius = appConfig.borderRadius
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PlatformBackHandler(
                onBack = {
                    if (navigator.canGoBack()) {
                        navigator.goBack()
                    } else {
                        showExitDialog = true
                    }
                }
            ) {
                if (showExitDialog) {
                    ExitDialog(
                        onConfirm = { exitApp() },
                        onDismiss = { showExitDialog = false }
                    )
                }

                AnimatedContent(
                    targetState = navigator.currentScreen,
                    transitionSpec = {
                        // Animación de desvanecimiento puro (Cross-fade)
                        fadeIn(animationSpec = tween(400)) togetherWith 
                        fadeOut(animationSpec = tween(400))
                    }
                ) { screen ->
                    when (screen) {
                        is Screen.Home -> HomeScreen(navigator)
                        is Screen.Test -> TestScreen(navigator)
                        is Screen.Settings -> SettingsScreen(navigator)
                        is Screen.WalletDetails -> WalletDetailsScreen(screen.walletId, navigator)
                    }
                }
            }
        }
    }
}

@Composable
expect fun PlatformBackHandler(onBack: () -> Unit, content: @Composable () -> Unit)
