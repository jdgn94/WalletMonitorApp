@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package app.jdgn.walletmonitor.utils

expect object DateUtils {
    fun formatDate(timestamp: Long, includeTime: Boolean = false): String
    fun getCurrentTimestamp(): Long
}
