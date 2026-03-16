package app.jdgn.walletmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.navigation.Navigator
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.CustomScaffold
import app.jdgn.walletmonitor.ui.components.FadingScroll
import app.jdgn.walletmonitor.ui.components.form.*
import app.jdgn.walletmonitor.viewmodel.TestViewModel
import kotlinx.datetime.LocalDate
import org.koin.compose.koinInject

@Composable
fun TestScreen(navigator: Navigator) {
    val viewModel: TestViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    
    var testAmount by remember { mutableStateOf(0.0) }
    var selectedDates by remember { mutableStateOf(emptyList<LocalDate>()) }

    CustomScaffold(
        title = "Test Components",
        navigator = navigator,
        topBarColor = state.componentColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Date Pickers", style = MaterialTheme.typography.titleMedium)
            
            DatePickerField(
                label = "Report Range (Switchable)",
                selectedDates = selectedDates,
                onDatesSelected = { dates, formatted ->
                    selectedDates = dates
                },
                isSwitchable = true,
                initialMode = DateSelectionMode.RANGE,
                customThemeColor = state.componentColor,
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerField(
                label = "Select Month",
                selectedDates = emptyList(),
                onDatesSelected = { _, _ -> },
                granularity = DateGranularity.MONTH,
                customThemeColor = state.componentColor,
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerField(
                label = "Select Year",
                selectedDates = emptyList(),
                onDatesSelected = { _, _ -> },
                granularity = DateGranularity.YEAR,
                customThemeColor = state.componentColor,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Input Fields", style = MaterialTheme.typography.titleMedium)

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

            Text("Scrolling Examples", style = MaterialTheme.typography.titleMedium)

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

            FadingScroll(
                modifier = Modifier.height(300.dp).fillMaxWidth(),
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
