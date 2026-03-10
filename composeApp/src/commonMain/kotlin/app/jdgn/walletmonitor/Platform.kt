package app.jdgn.walletmonitor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun exitApp()

expect fun getSystemLanguage(): String
