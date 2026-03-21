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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.navigation.Navigator
import app.jdgn.walletmonitor.ui.components.*
import app.jdgn.walletmonitor.ui.components.form.*
import app.jdgn.walletmonitor.viewmodel.ChartDataSet
import app.jdgn.walletmonitor.viewmodel.ChartPoint
import app.jdgn.walletmonitor.viewmodel.TestViewModel
import kotlinx.datetime.LocalDate
import org.koin.compose.koinInject

data class TestCurrency(val code: String, val name: String, val symbol: String)

@Composable
fun TestScreen(navigator: Navigator) {
    val viewModel: TestViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    
    var testAmount by remember { mutableStateOf(0.0) }
    var selectedDates by remember { mutableStateOf(emptyList<LocalDate>()) }

    val currencies = remember {
        listOf(
            TestCurrency("USD", "US Dollar", "$"),
            TestCurrency("EUR", "Euro", "€"),
            TestCurrency("GBP", "British Pound", "£"),
            TestCurrency("JPY", "Japanese Yen", "¥"),
            TestCurrency("MXN", "Mexican Peso", "$"),
            TestCurrency("ARS", "Argentine Peso", "$"),
            TestCurrency("BRL", "Brazilian Real", "R$"),
            TestCurrency("CAD", "Canadian Dollar", "$"),
            TestCurrency("AUD", "Australian Dollar", "$"),
            TestCurrency("CHF", "Swiss Franc", "Fr"),
            TestCurrency("CNY", "Chinese Yuan", "¥"),
            TestCurrency("INR", "Indian Rupee", "₹"),
            TestCurrency("RUB", "Russian Ruble", "₽"),
            TestCurrency("ZAR", "South African Rand", "R"),
            TestCurrency("TRY", "Turkish Lira", "₺"),
            TestCurrency("KRW", "South Korean Won", "₩"),
            TestCurrency("BTC", "Bitcoin", "₿"),
            TestCurrency("ETH", "Ethereum", "Ξ"),
            TestCurrency("USDT", "Tether", "₮")
        )
    }
    var selectedCurrency by remember { mutableStateOf<TestCurrency?>(null) }

    val chartData = remember(state.componentColor) {
        listOf(
            ChartDataSet(
                name = "Ingresos",
                color = Color(0xFF4CAF50),
                points = listOf(
                    ChartPoint(0f, 100f, "Ene"),
                    ChartPoint(1f, 150f, "Feb"),
                    ChartPoint(2f, 120f, "Mar"),
                    ChartPoint(3f, 200f, "Abr"),
                    ChartPoint(4f, 180f, "May"),
                )
            ),
            ChartDataSet(
                name = "Gastos",
                color = Color(0xFFF44336),
                points = listOf(
                    ChartPoint(0f, 80f, "Ene"),
                    ChartPoint(1f, 160f, "Feb"),
                    ChartPoint(2f, 90f, "Mar"),
                    ChartPoint(3f, 150f, "Abr"),
                    ChartPoint(4f, 210f, "May"),
                )
            ),
            ChartDataSet(
                name = "Balance",
                color = state.componentColor,
                points = listOf(
                    ChartPoint(0f, 20f, "Ene"),
                    ChartPoint(1f, -10f, "Feb"),
                    ChartPoint(2f, 30f, "Mar"),
                    ChartPoint(3f, 50f, "Abr"),
                    ChartPoint(4f, -30f, "May"),
                )
            )
        )
    }

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
            Text("Interactive Custom Chart", style = MaterialTheme.typography.titleMedium)
            
            CustomChart(
                modifier = Modifier.fillMaxWidth(),
                title = "Estadísticas Mensuales",
                dataSets = chartData
            )

            Text("Custom Box Variants", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomBox(
                    modifier = Modifier.weight(1f),
                    shadowType = ShadowType.External,
                    shadowElevation = 8.dp,
                    shadowColor = state.componentColor,
                    borderColor = state.componentColor,
                    borderWidth = 1.dp,
                    borderType = BorderType.Solid
                ) {
                    Text("External Shadow + Solid Border", style = MaterialTheme.typography.bodySmall)
                }

                CustomBox(
                    modifier = Modifier.weight(1f),
                    shadowType = ShadowType.Internal,
                    shadowElevation = 10.dp,
                    shadowColor = state.componentColor,
                    backgroundColor = state.componentColor.copy(alpha = 0.1f),
                    onClick = {}
                ) {
                    Text("Internal Shadow (Hundido)", style = MaterialTheme.typography.bodySmall)
                }
            }

            Text("Selectors & Forms", style = MaterialTheme.typography.titleMedium)

            SelectorField(
                label = "Currency",
                items = currencies,
                selectedItem = selectedCurrency,
                onItemSelected = { selectedCurrency = it },
                itemLabel = { "${it.code} - ${it.name}" },
                itemIcon = { currency ->
                    Text(
                        text = currency.symbol,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = state.componentColor
                    )
                },
                placeholder = "Select a currency",
                customThemeColor = state.componentColor,
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerField(
                label = "Report Range (Switchable)",
                selectedDates = selectedDates,
                onDatesSelected = { dates, _ ->
                    selectedDates = dates
                },
                isSwitchable = true,
                initialMode = DateSelectionMode.RANGE,
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
