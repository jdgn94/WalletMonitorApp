package app.jdgn.walletmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.navigation.Navigator
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.CustomScaffold
import app.jdgn.walletmonitor.ui.components.FadingScroll
import app.jdgn.walletmonitor.ui.components.form.AmountInputField
import app.jdgn.walletmonitor.ui.components.form.ColorPickerField
import app.jdgn.walletmonitor.ui.components.form.CustomTextField
import app.jdgn.walletmonitor.viewmodel.TestViewModel
import org.koin.compose.koinInject

@Composable
fun TestScreen(navigator: Navigator) {
    val viewModel: TestViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    
    var testAmount by remember { mutableStateOf(0.0) }

    CustomScaffold(
        title = "test",
        navigator = navigator,
        topBarColor = state.componentColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AmountInputField(
                value = testAmount,
                onValueChange = { testAmount = it },
                label = "Test Amount Input",
                customThemeColor = state.componentColor,

                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(
                value = state.searchText,
                onValueChange = { viewModel.updateSearchText(it) },
                label = "Search",
                leftIcon = Icons.Default.Search,
                clearable = true,
                isError = state.searchText == "error",
                customThemeColor = state.componentColor,
                modifier = Modifier.fillMaxWidth(),
            )

            ColorPickerField(
                selectedColorHex = state.selectedColorHex,
                onColorSelected = { color, hex ->
                    viewModel.onColorSelected(color, hex)
                },
                label = "Wallet Color",
                customThemeColor = state.componentColor,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Horizontal Fading")
            FadingScroll(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                isVertical = false,
                fadeLength = 24.dp,
                padding = PaddingValues(horizontal = 8.dp)
            ) { scrollModifier ->
                Row(modifier = scrollModifier) {
                    repeat(10) { index ->
                        CustomBox(
                            width = 120.dp,
                            height = 60.dp,
                            margin = 4.dp,
                            shadowColor = state.componentColor
                        ) {
                            Text("Item #$index")
                        }
                    }
                }
            }

            Text("Vertical Fading")
            FadingScroll(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                isVertical = true,
                fadeLength = 40.dp,
                padding = PaddingValues(vertical = 8.dp)
            ) { scrollModifier ->
                Column(
                    modifier = scrollModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(20) { index ->
                        CustomBox(
                            width = 280.dp,
                            height = 80.dp,
                            margin = 8.dp,
                            shadowColor = state.componentColor
                        ) {
                            Text("Vertical Item #$index")
                        }
                    }
                }
            }
        }
    }
}
