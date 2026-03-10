package app.jdgn.walletmonitor.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.jdgn.walletmonitor.navigation.Navigator
import app.jdgn.walletmonitor.navigation.Screen

@Composable
fun WalletDetailsScreen(walletId: String, navigator: Navigator) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Wallet Details")
        Text("ID: $walletId")
        Button(onClick = { navigator.navigateTo(Screen.Home) }) {
            Text("Back to Home")
        }
    }
}
