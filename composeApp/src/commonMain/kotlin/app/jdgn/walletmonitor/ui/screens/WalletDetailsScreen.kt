package app.jdgn.walletmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.navigation.Navigator
import app.jdgn.walletmonitor.navigation.Screen
import app.jdgn.walletmonitor.ui.components.CustomScaffold
import app.jdgn.walletmonitor.viewmodel.WalletDetailsViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDetailsScreen(walletId: String, navigator: Navigator) {
    val viewModel: WalletDetailsViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(walletId) {
        viewModel.loadWallet(walletId)
    }

    CustomScaffold(
        title = "Wallet Details",
        navigator = navigator,
        rightContent = {
            IconButton(onClick = { viewModel.toggleEditing() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Edit"
                )
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
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "ID: ${state.walletId}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        OutlinedTextField(
                            value = state.walletName,
                            onValueChange = { viewModel.updateWalletName(it) },
                            label = { Text("Wallet Name") },
                            readOnly = !state.isEditing,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        OutlinedTextField(
                            value = state.walletAddress,
                            onValueChange = { viewModel.updateWalletAddress(it) },
                            label = { Text("Wallet Address") },
                            readOnly = !state.isEditing,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Text(
                            text = "Color: ${state.walletColor}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                if (state.isEditing) {
                    Button(
                        onClick = { viewModel.saveWallet() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Changes")
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                OutlinedButton(
                    onClick = { navigator.navigateTo(Screen.Home) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Home")
                }
            }
        }
    }
}
