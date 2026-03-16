@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package app.jdgn.walletmonitor.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.*

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

    actual fun formatLocalDate(date: LocalDate, pattern: String): String {
        return try {
            val calendar = NSCalendar.currentCalendar
            val components = date.toNSDateComponents()
            val nsDate = calendar.dateFromComponents(components) ?: return date.toString()
            val formatter = NSDateFormatter()
            formatter.dateFormat = pattern
            formatter.locale = NSLocale.autoupdatingCurrentLocale
            formatter.stringFromDate(nsDate)
        } catch (e: Exception) {
            date.toString()
        }
    }

    actual fun today(): LocalDate {
        val calendar = NSCalendar.currentCalendar
        val now = NSDate()
        val components = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = now
        )
        return LocalDate(
            year = components.year.toInt(),
            month = Month(components.month.toInt()),
            day = components.day.toInt()
        )
    }
}
