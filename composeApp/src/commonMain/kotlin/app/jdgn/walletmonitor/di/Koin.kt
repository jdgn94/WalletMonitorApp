package app.jdgn.walletmonitor.di

import app.jdgn.walletmonitor.data.local.AppConfig
import app.jdgn.walletmonitor.data.local.DatabaseDriverFactory
import app.jdgn.walletmonitor.data.local.DatabaseSeeder
import app.jdgn.walletmonitor.data.local.SettingsManager
import app.jdgn.walletmonitor.data.local.WalletRepository
import app.jdgn.walletmonitor.database.AppDatabase
import app.jdgn.walletmonitor.viewmodel.AmountInputViewModel
import app.jdgn.walletmonitor.viewmodel.ChartViewModel
import app.jdgn.walletmonitor.viewmodel.CreateAccountViewModel
import app.jdgn.walletmonitor.viewmodel.DatePickerViewModel
import app.jdgn.walletmonitor.viewmodel.HomeViewModel
import app.jdgn.walletmonitor.viewmodel.SettingsViewModel
import app.jdgn.walletmonitor.viewmodel.TestViewModel
import app.jdgn.walletmonitor.viewmodel.WalletDetailsViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    enableNetworkLogging: Boolean = false,
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(commonModule(), platformModule())
}

// iOS initialization
fun initKoin() = initKoin(enableNetworkLogging = false) {}

fun commonModule() = module {
    single { 
        val driverFactory = get<DatabaseDriverFactory>()
        // Aquí podrías leer un flag de configuración si lo deseas
        // Por ahora, si necesitas forzar el borrado para pruebas, puedes descomentar:
        driverFactory.deleteDatabase()
        AppDatabase(driverFactory.createDriver())
    }
    single { DatabaseSeeder(get()) }
    single { WalletRepository(get()) }
    single { SettingsManager(get()) }
    single { AppConfig(get(), get()) }
    
    factory { HomeViewModel(get()) }
    factory { SettingsViewModel() }
    factory { WalletDetailsViewModel() }
    factory { TestViewModel() }
    factory { AmountInputViewModel(get(), get()) }
    factory { ChartViewModel() }
    factory { DatePickerViewModel() }
    factory { CreateAccountViewModel(get()) }
}

expect fun platformModule(): Module

/**
 * Función para reiniciar la base de datos si es necesario.
 * Debe llamarse antes de inicializar Koin o manejarse con cuidado.
 */
fun checkAndResetDatabase(shouldReset: Boolean, driverFactory: DatabaseDriverFactory) {
    if (shouldReset) {
        driverFactory.deleteDatabase()
    }
}
