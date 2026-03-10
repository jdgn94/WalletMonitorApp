package app.jdgn.walletmonitor.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun platformColorScheme(darkTheme: Boolean, dynamicColor: Boolean): ColorScheme {
    // iOS no soporta Dynamic Color (Material You) nativo de la misma forma que Android 12+
    return if (darkTheme) DarkColorScheme else LightColorScheme
}
