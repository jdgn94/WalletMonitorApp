package app.jdgn.walletmonitor.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.random.Random

object ColorUtils {
    /**
     * Genera un color aleatorio con opacidad total.
     */
    fun getRandomColor(): Color {
        return Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1.0f
        )
    }

    /**
     * Convierte un String hexadecimal (ej: "#FF0000" o "FF0000") a un objeto Color.
     */
    fun hexToColor(hex: String): Color {
        return try {
            val cleanHex = hex.removePrefix("#")
            if (cleanHex.length == 8) {
                Color(cleanHex.toLong(16))
            } else {
                Color(cleanHex.toLong(16) or 0xFF000000L)
            }
        } catch (e: Exception) {
            Color.Gray
        }
    }

    /**
     * Convierte un objeto Color a su representación hexadecimal (ej: "#FF0000").
     */
    fun colorToHex(color: Color): String {
        val argb = color.toArgb()
        // Retornamos solo los últimos 6 caracteres para ignorar el alpha si no es necesario
        // o los 8 si quieres incluir transparencia. Aquí usamos 6 por consistencia.
        return "#" + (argb.toUInt().toString(16).padStart(8, '0').takeLast(6)).uppercase()
    }
}
