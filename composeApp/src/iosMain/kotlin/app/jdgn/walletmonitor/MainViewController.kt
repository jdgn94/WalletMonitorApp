package app.jdgn.walletmonitor

import androidx.compose.ui.window.ComposeUIViewController
import app.jdgn.walletmonitor.di.initKoin

fun MainViewController() = ComposeUIViewController {
    App()
}

object KoinInitializer {
    fun initialize() {
        initKoin()
    }
}
