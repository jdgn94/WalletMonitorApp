package app.jdgn.walletmonitor.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.jdgn.walletmonitor.data.local.DatabaseDriverFactory
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSUserDomainMask

class IOSDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "wallet.db")
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun deleteDatabase() {
        val fileManager = NSFileManager.defaultManager
        val libraryDirectory = fileManager.URLForDirectory(
            directory = NSLibraryDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val databasePath = libraryDirectory?.path + "/Application Support/databases/wallet.db"
        if (fileManager.fileExistsAtPath(databasePath)) {
            fileManager.removeItemAtPath(databasePath, null)
        }
    }
}
