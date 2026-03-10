package app.jdgn.walletmonitor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember

class Navigator(initialScreen: Screen) {
    private val backStack = mutableStateListOf<Screen>(initialScreen)
    
    var isBackNavigation by mutableStateOf(false)
        private set
    
    val currentScreen: Screen
        get() = backStack.last()

    fun navigateTo(screen: Screen, clearStack: Boolean = false) {
        isBackNavigation = false
        val shouldClear = clearStack || screen is Screen.Home
        
        if (shouldClear) {
            backStack.clear()
            backStack.add(screen)
            return
        }

        if (currentScreen != screen) {
            backStack.add(screen)
        }
    }

    fun goBack(): Boolean {
        if (backStack.size > 1) {
            isBackNavigation = true
            backStack.removeAt(backStack.size - 1)
            return true
        }
        return false
    }
    
    fun canGoBack(): Boolean = backStack.size > 1
}

@Composable
fun rememberNavigator(initialScreen: Screen = Screen.Home): Navigator {
    return remember { Navigator(initialScreen) }
}
