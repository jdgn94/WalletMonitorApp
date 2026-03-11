package app.jdgn.walletmonitor.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Stable
abstract class ViewModel : KoinComponent {
    val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}

@Composable
fun <T : ViewModel> rememberViewModel(getViewModel: () -> T): T {
    return remember { getViewModel() }
}
