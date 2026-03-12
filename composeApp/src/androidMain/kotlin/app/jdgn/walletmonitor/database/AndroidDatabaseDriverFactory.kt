package app.jdgn.walletmonitor.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.jdgn.walletmonitor.data.local.DatabaseDriverFactory

class AndroidDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "wallet.db")
    }

    override fun deleteDatabase() {
        context.deleteDatabase("wallet.db")
    }
}
