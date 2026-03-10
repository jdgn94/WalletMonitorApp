package app.jdgn.walletmonitor

import android.app.Application
import app.jdgn.walletmonitor.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class WalletApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@WalletApp)
        }
    }
}
