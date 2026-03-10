package app.jdgn.walletmonitor.di

import app.jdgn.walletmonitor.database.DatabaseDriverFactory
import app.jdgn.walletmonitor.database.IOSDatabaseDriverFactory
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module

@OptIn(ExperimentalSettingsImplementation::class)
actual fun platformModule() = module {
    single<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
    single<Settings> { KeychainSettings("wallet_monitor_prefs") }
}
