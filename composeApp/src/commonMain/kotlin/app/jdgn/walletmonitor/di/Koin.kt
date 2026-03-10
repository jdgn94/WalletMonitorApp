package app.jdgn.walletmonitor.di

import app.jdgn.walletmonitor.database.AppDatabase
import app.jdgn.walletmonitor.database.DatabaseDriverFactory
import app.jdgn.walletmonitor.database.SettingsManager
import app.jdgn.walletmonitor.database.WalletRepository
import app.jdgn.walletmonitor.database.AppConfig
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule(), platformModule())
    }

// iOS initialization
fun initKoin() = initKoin {}

fun commonModule() = module {
    single { AppDatabase(get<DatabaseDriverFactory>().createDriver()) }
    single { WalletRepository(get()) }
    single { SettingsManager(get()) }
    single { AppConfig(get()) }
}

expect fun platformModule(): Module
