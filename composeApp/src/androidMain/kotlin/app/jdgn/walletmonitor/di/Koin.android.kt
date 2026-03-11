package app.jdgn.walletmonitor.di

import android.content.Context
import app.jdgn.walletmonitor.data.local.DatabaseDriverFactory
import app.jdgn.walletmonitor.database.AndroidDatabaseDriverFactory
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.dsl.module

actual fun platformModule() = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<Settings> {
        val context: Context = get()
        val sharedPreferences = context.getSharedPreferences("wallet_monitor_prefs", Context.MODE_PRIVATE)
        SharedPreferencesSettings(sharedPreferences)
    }
}
