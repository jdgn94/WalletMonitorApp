@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package app.jdgn.walletmonitor.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.autoupdatingCurrentLocale
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970

actual object DateUtils {
    actual fun formatDate(timestamp: Long, includeTime: Boolean): String {
        return try {
            val date = NSDate.dateWithTimeIntervalSince1970(timestamp / 1000.0)
            val formatter = NSDateFormatter()
            formatter.dateFormat = if (includeTime) "dd/MM/yyyy HH:mm" else "dd/MM/yyyy"
            formatter.locale = NSLocale.autoupdatingCurrentLocale
            formatter.stringFromDate(date)
        } catch (e: Exception) {
            "N/A"
        }
    }

    actual fun getCurrentTimestamp(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }
}
