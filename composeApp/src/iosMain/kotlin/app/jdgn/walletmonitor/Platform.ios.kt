package app.jdgn.walletmonitor

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.UIKit.UIDevice
import platform.posix.exit

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun exitApp() {
    exit(0)
}

actual fun getSystemLanguage(): String {
    return NSLocale.currentLocale.languageCode
}
