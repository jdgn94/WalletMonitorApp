package app.jdgn.walletmonitor.di

import app.jdgn.walletmonitor.data.local.AppConfig
import app.jdgn.walletmonitor.data.local.DatabaseDriverFactory
import app.jdgn.walletmonitor.data.local.DatabaseSeeder
import app.jdgn.walletmonitor.data.local.SettingsManager
import app.jdgn.walletmonitor.data.local.WalletRepository
import app.jdgn.walletmonitor.database.AppDatabase
import app.jdgn.walletmonitor.viewmodel.HomeViewModel
import app.jdgn.walletmonitor.viewmodel.SettingsViewModel
import app.jdgn.walletmonitor.viewmodel.TestViewModel
import app.jdgn.walletmonitor.viewmodel.WalletDetailsViewModel
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
    single { DatabaseSeeder(get()) }
    single { WalletRepository(get()) }
    single { SettingsManager(get()) }
    single { AppConfig(get()) }
    
    factory { HomeViewModel() }
    factory { SettingsViewModel() }
    factory { WalletDetailsViewModel() }
    factory { TestViewModel() }
}

expect fun platformModule(): Module
