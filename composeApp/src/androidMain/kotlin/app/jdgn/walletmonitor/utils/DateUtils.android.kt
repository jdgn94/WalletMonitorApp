@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package app.jdgn.walletmonitor.utils

import android.annotation.SuppressLint
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate as JavaLocalDate

actual object DateUtils {
    @SuppressLint("NewApi")
    actual fun formatDate(timestamp: Long, includeTime: Boolean): String {
        return try {
            val instant = Instant.ofEpochMilli(timestamp)
            val pattern = if (includeTime) "dd/MM/yyyy HH:mm" else "dd/MM/yyyy"
            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                .withZone(ZoneId.systemDefault())
            formatter.format(instant)
        } catch (e: Exception) {
            "N/A"
        }
    }

    @SuppressLint("NewApi")
    actual fun getCurrentTimestamp(): Long {
        return Clock.systemDefaultZone().millis()
    }

    @SuppressLint("NewApi")
    actual fun formatLocalDate(date: LocalDate, pattern: String): String {
        return try {
            val javaDate = date.toJavaLocalDate()
            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            javaDate.format(formatter)
        } catch (e: Exception) {
            date.toString()
        }
    }

    @SuppressLint("NewApi")
    actual fun today(): LocalDate {
        return JavaLocalDate.now(Clock.systemDefaultZone()).toKotlinLocalDate()
    }
}
