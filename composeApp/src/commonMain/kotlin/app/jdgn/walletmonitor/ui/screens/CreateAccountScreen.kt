package app.jdgn.walletmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.navigation.Navigator
import app.jdgn.walletmonitor.ui.components.CustomScaffold
import app.jdgn.walletmonitor.ui.components.FadingScroll
import app.jdgn.walletmonitor.ui.components.form.*
import app.jdgn.walletmonitor.viewmodel.CreateAccountViewModel
import org.koin.compose.koinInject
import walletmonitor.composeapp.generated.resources.Res
import walletmonitor.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateAccountScreen(navigator: Navigator) {
    val viewModel: CreateAccountViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navigator.goBack()
        }
    }

    CustomScaffold(
        title = "Create Account",
        navigator = navigator,
        topBarColor = state.selectedColor
    ) { padding ->
        FadingScroll(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            scrollState = rememberScrollState(),
            fadeLength = 16.dp
        ) { scrollModifier ->
            BoxWithConstraints(
                modifier = scrollModifier.fillMaxWidth()
            ) {
                val isWide = maxWidth > 600.dp
                
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.Start // Cambiado a Start para alineación perfecta a la izquierda
                ) {
                    // First Row: Icon and Name + Bank
                    if (isWide) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.weight(0.5f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                IconPickerField(
                                    selectedIconName = state.icon,
                                    onIconSelected = viewModel::onIconChange,
                                    color = state.selectedColor
                                )
                                CustomTextField(
                                    value = state.name,
                                    onValueChange = viewModel::onNameChange,
                                    label = "Account Name",
                                    modifier = Modifier.weight(1f),
                                    customThemeColor = state.selectedColor
                                )
                            }
                            SelectorField(
                                label = "Bank",
                                items = state.banks,
                                selectedItem = state.selectedBank,
                                onItemSelected = viewModel::onBankSelected,
                                itemLabel = { it.name },
                                modifier = Modifier.weight(0.5f),
                                placeholder = "Select Bank",
                                customThemeColor = state.selectedColor
                            )
                        }
                    } else {
                        // En móvil, agrupamos el IconPicker y el TextField en un Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconPickerField(
                                selectedIconName = state.icon,
                                onIconSelected = viewModel::onIconChange,
                                color = state.selectedColor
                            )
                            CustomTextField(
                                value = state.name,
                                onValueChange = viewModel::onNameChange,
                                label = "Account Name",
                                modifier = Modifier.weight(1f),
                                customThemeColor = state.selectedColor
                            )
                        }
                        
                        SelectorField(
                            label = "Bank",
                            items = state.banks,
                            selectedItem = state.selectedBank,
                            onItemSelected = viewModel::onBankSelected,
                            itemLabel = { it.name },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = "Select Bank",
                            customThemeColor = state.selectedColor
                        )
                    }

                    // Second Row: Currency, Account Type, Color
                    if (isWide) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CurrencySelector(
                                currencies = state.currencies,
                                selectedCurrency = state.selectedCurrency,
                                onCurrencySelected = viewModel::onCurrencySelected,
                                modifier = Modifier.weight(1f / 3f),
                                customThemeColor = state.selectedColor
                            )
                            SelectorField(
                                label = "Account Type",
                                items = state.accountTypes,
                                selectedItem = state.selectedAccountType,
                                onItemSelected = viewModel::onAccountTypeSelected,
                                itemLabel = { it.name },
                                modifier = Modifier.weight(1f / 3f),
                                placeholder = "Select Type",
                                customThemeColor = state.selectedColor,
                                customItemContent = { type ->
                                    val typeRes = when(type.name.lowercase()) {
                                        "bank" -> Res.string.account_type_bank
                                        "cash" -> Res.string.account_type_cash
                                        "digital" -> Res.string.account_type_digital
                                        else -> null
                                    }
                                    val typeName = typeRes?.let { stringResource(it) } ?: type.name
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = typeName,
                                            style = MaterialTheme.typography.bodyLarge,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            )
                            ColorPickerField(
                                selectedColorHex = state.selectedColorHex,
                                onColorSelected = viewModel::onColorSelected,
                                label = "Color",
                                modifier = Modifier.weight(1f / 3f),
                                customThemeColor = state.selectedColor
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CurrencySelector(
                                currencies = state.currencies,
                                selectedCurrency = state.selectedCurrency,
                                onCurrencySelected = viewModel::onCurrencySelected,
                                modifier = Modifier.weight(0.5f),
                                customThemeColor = state.selectedColor
                            )
                            SelectorField(
                                label = "Account Type",
                                items = state.accountTypes,
                                selectedItem = state.selectedAccountType,
                                onItemSelected = viewModel::onAccountTypeSelected,
                                itemLabel = { it.name },
                                modifier = Modifier.weight(0.5f),
                                placeholder = "Type",
                                customThemeColor = state.selectedColor,
                                customItemContent = { type ->
                                    val typeRes = when(type.name.lowercase()) {
                                        "bank" -> Res.string.account_type_bank
                                        "cash" -> Res.string.account_type_cash
                                        "digital" -> Res.string.account_type_digital
                                        else -> null
                                    }
                                    val typeName = typeRes?.let { stringResource(it) } ?: type.name
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = typeName,
                                            style = MaterialTheme.typography.bodyLarge,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            )
                        }
                        ColorPickerField(
                            selectedColorHex = state.selectedColorHex,
                            onColorSelected = viewModel::onColorSelected,
                            label = "Account Color",
                            modifier = Modifier.fillMaxWidth(),
                            customThemeColor = state.selectedColor
                        )
                    }

                    // Third Row: Amount
                    AmountInputField(
                        value = state.amount,
                        onValueChange = viewModel::onAmountChange,
                        label = "Initial Balance",
                        currencyId = state.selectedCurrency?.id,
                        modifier = if (isWide) Modifier.fillMaxWidth(0.5f) else Modifier.fillMaxWidth(),
                        customThemeColor = state.selectedColor
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Centrar el botón manualmente
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = { viewModel.saveAccount() },
                            modifier = Modifier
                                .fillMaxWidth(if (isWide) 0.4f else 0.8f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = state.selectedColor
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CREATE ACCOUNT", fontWeight = FontWeight.Bold)
                        }
                    }

                    if (state.error != null) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                text = state.error!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
