package app.jdgn.walletmonitor.di

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import app.jdgn.walletmonitor.database.AndroidDatabaseDriverFactory
import app.jdgn.walletmonitor.database.DatabaseDriverFactory
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.dsl.module

actual fun platformModule() = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<Settings> {
        val masterKey = MasterKey.Builder(get())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            get(),
            "secured_settings",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        SharedPreferencesSettings(sharedPreferences)
    }
}
