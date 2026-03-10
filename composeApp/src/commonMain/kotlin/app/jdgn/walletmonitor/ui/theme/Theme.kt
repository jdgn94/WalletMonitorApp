package app.jdgn.walletmonitor.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = GreenOnPrimary,
    primaryContainer = GreenPrimaryContainer,
    onPrimaryContainer = GreenOnPrimaryContainer,
    secondary = BlueSecondary,
    onSecondary = BlueOnSecondary,
    secondaryContainer = BlueSecondaryContainer,
    onSecondaryContainer = BlueOnSecondaryContainer,
    tertiary = PurpleTertiary,
    onTertiary = PurpleOnTertiary,
    tertiaryContainer = PurpleTertiaryContainer,
    onTertiaryContainer = PurpleOnTertiaryContainer,
    error = RedError,
    onError = RedOnError,
    errorContainer = RedErrorContainer,
    onErrorContainer = RedOnErrorContainer,
    background = NeutralLight,
    onBackground = OnNeutralLight,
    surface = NeutralLight,
    onSurface = OnNeutralLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

internal val DarkColorScheme = darkColorScheme(
    primary = GreenDarkPrimary,
    onPrimary = GreenDarkOnPrimary,
    primaryContainer = GreenDarkPrimaryContainer,
    onPrimaryContainer = GreenDarkOnPrimaryContainer,
    secondary = BlueDarkSecondary,
    onSecondary = BlueDarkOnSecondary,
    secondaryContainer = BlueDarkSecondaryContainer,
    onSecondaryContainer = BlueDarkOnSecondaryContainer,
    tertiary = PurpleDarkTertiary,
    onTertiary = PurpleDarkOnTertiary,
    tertiaryContainer = PurpleDarkTertiaryContainer,
    onTertiaryContainer = PurpleDarkOnTertiaryContainer,
    error = RedDarkError,
    onError = RedDarkOnError,
    errorContainer = RedDarkErrorContainer,
    onErrorContainer = RedDarkOnErrorContainer,
    background = NeutralDark,
    onBackground = OnNeutralDark,
    surface = NeutralDark,
    onSurface = OnNeutralDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

@Immutable
data class ExtendedColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val info: Color,
    val onInfo: Color,
    val infoContainer: Color,
    val onInfoContainer: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        success = Color.Unspecified,
        onSuccess = Color.Unspecified,
        successContainer = Color.Unspecified,
        onSuccessContainer = Color.Unspecified,
        warning = Color.Unspecified,
        onWarning = Color.Unspecified,
        warningContainer = Color.Unspecified,
        onWarningContainer = Color.Unspecified,
        info = Color.Unspecified,
        onInfo = Color.Unspecified,
        infoContainer = Color.Unspecified,
        onInfoContainer = Color.Unspecified
    )
}

@Composable
fun WalletMonitorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    borderRadius: Int = 12,
    content: @Composable () -> Unit
) {
    val colorScheme = platformColorScheme(darkTheme, dynamicColor)
    
    val shapes = Shapes(
        small = RoundedCornerShape((borderRadius / 2).dp),
        medium = RoundedCornerShape(borderRadius.dp),
        large = RoundedCornerShape((borderRadius * 2).dp)
    )

    val extendedColors = if (darkTheme) {
        ExtendedColors(
            success = SuccessDarkGreen,
            onSuccess = OnSuccessDarkGreen,
            successContainer = SuccessDarkGreenContainer,
            onSuccessContainer = OnSuccessDarkGreenContainer,
            warning = WarningDarkAmber,
            onWarning = WarningDarkOnAmber,
            warningContainer = WarningDarkAmberContainer,
            onWarningContainer = WarningDarkOnAmberContainer,
            info = InfoDarkBlue,
            onInfo = InfoDarkOnBlue,
            infoContainer = InfoDarkBlueContainer,
            onInfoContainer = InfoDarkOnBlueContainer
        )
    } else {
        ExtendedColors(
            success = SuccessGreen,
            onSuccess = OnSuccessGreen,
            successContainer = SuccessGreenContainer,
            onSuccessContainer = OnSuccessGreenContainer,
            warning = WarningAmber,
            onWarning = OnWarningAmber,
            warningContainer = WarningAmberContainer,
            onWarningContainer = OnWarningAmberContainer,
            info = InfoBlue,
            onInfo = OnInfoBlue,
            infoContainer = InfoBlueContainer,
            onInfoContainer = OnInfoBlueContainer
        )
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            content = content
        )
    }
}

@Composable
expect fun platformColorScheme(darkTheme: Boolean, dynamicColor: Boolean): androidx.compose.material3.ColorScheme

object WalletMonitorTheme {
    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}
