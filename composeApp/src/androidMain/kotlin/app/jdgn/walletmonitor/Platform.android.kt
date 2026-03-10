package app.jdgn.walletmonitor

import java.util.Locale
import kotlin.system.exitProcess

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun exitApp() {
    exitProcess(0)
}

actual fun getSystemLanguage(): String {
    return Locale.getDefault().language
}
