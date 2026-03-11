@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package app.jdgn.walletmonitor.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual object DateUtils {
    actual fun formatDate(timestamp: Long, includeTime: Boolean): String {
        return try {
            val date = Date(timestamp)
            val pattern = if (includeTime) "dd/MM/yyyy HH:mm" else "dd/MM/yyyy"
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            formatter.format(date)
        } catch (e: Exception) {
            "N/A"
        }
    }

    actual fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }
}
