package app.jdgn.walletmonitor.navigation

sealed class Screen {
    data object Home : Screen()

    data object Test : Screen()
    data object Settings : Screen()
    data class WalletDetails(val walletId: String) : Screen()
    
    // Para agregar una página nueva, solo añade un 'data object' o 'data class' aquí
}
