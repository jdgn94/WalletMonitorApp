package app.jdgn.walletmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.navigation.Navigator
import app.jdgn.walletmonitor.navigation.Screen
import app.jdgn.walletmonitor.ui.components.CustomScaffold
import app.jdgn.walletmonitor.viewmodel.SettingsViewModel
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(navigator: Navigator) {
    val viewModel: SettingsViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    CustomScaffold(
        title = "Settings",
        navigator = navigator
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Theme Mode: ${state.themeMode}",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("system", "light", "dark").forEach { mode ->
                    FilterChip(
                        selected = state.themeMode == mode,
                        onClick = { viewModel.setThemeMode(mode) },
                        label = { Text(mode) }
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dynamic Color")
                Switch(
                    checked = state.useDynamicColor,
                    onCheckedChange = { viewModel.setDynamicColor(it) }
                )
            }
            
            Text(
                text = "Border Radius: ${state.borderRadius}",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Slider(
                value = state.borderRadius.toFloat(),
                onValueChange = { viewModel.setBorderRadius(it.toInt()) },
                valueRange = 0f..24f,
                steps = 23
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { viewModel.saveSettings() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Save Settings")
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { navigator.navigateTo(Screen.Home) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }
        }
    }
}
