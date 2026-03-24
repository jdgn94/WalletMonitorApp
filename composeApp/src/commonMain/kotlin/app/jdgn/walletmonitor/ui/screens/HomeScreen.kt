package app.jdgn.walletmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.navigation.Navigator
import app.jdgn.walletmonitor.navigation.Screen
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.CustomScaffold
import app.jdgn.walletmonitor.viewmodel.HomeViewModel
import org.koin.compose.koinInject

@Composable
fun HomeScreen(navigator: Navigator) {
    val viewModel: HomeViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadWallets()
    }

    CustomScaffold(
        title = "Wallet Monitor",
        navigator = navigator,
        rightContent = {
            IconButton(onClick = { navigator.navigateTo(Screen.CreateAccount) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomBox(
                onClick = { navigator.navigateTo(Screen.Test) },
                modifier = Modifier.fillMaxWidth()
            ) { 
                Text("Go to Test Page") 
            }
            
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.wallets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No accounts yet")
                        Button(onClick = { navigator.navigateTo(Screen.CreateAccount) }) {
                            Text("Create your first account")
                        }
                    }
                }
            }
        }
    }
}
