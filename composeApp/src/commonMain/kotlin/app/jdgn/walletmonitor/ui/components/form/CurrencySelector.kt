package app.jdgn.walletmonitor.ui.components.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.database.SelectAllCurrenciesWithType
import app.jdgn.walletmonitor.ui.components.DiagonalBanner
import org.jetbrains.compose.resources.stringResource
import walletmonitor.composeapp.generated.resources.*

@Composable
fun CurrencySelector(
    currencies: List<SelectAllCurrenciesWithType>,
    selectedCurrency: SelectAllCurrenciesWithType?,
    onCurrencySelected: (SelectAllCurrenciesWithType) -> Unit,
    modifier: Modifier = Modifier,
    customThemeColor: Color? = null
) {
    val errorColor = MaterialTheme.colorScheme.error
    val themeColor = customThemeColor ?: MaterialTheme.colorScheme.primary

    SelectorField(
        label = "Currency",
        items = currencies,
        selectedItem = selectedCurrency,
        onItemSelected = onCurrencySelected,
        itemLabel = { it.code },
        searchFilter = { "${it.code} ${it.name} ${it.name_plural}" },
        modifier = modifier,
        placeholder = "Currency",
        customThemeColor = themeColor,
        itemShadowColor = { currency ->
            if (currency.type_name.lowercase() == "fiat" && currency.countries_ids.isBlank()) {
                errorColor
            } else {
                themeColor
            }
        },
        itemSelectedBackgroundColor = { currency ->
            if (currency.type_name.lowercase() == "fiat" && currency.countries_ids.isBlank()) {
                errorColor.copy(alpha = 0.2f)
            } else {
                themeColor.copy(alpha = 0.1f)
            }
        },
        customItemContent = { currency ->
            val isDeprecated = currency.type_name.lowercase() == "fiat" && currency.countries_ids.isBlank()
            
            val typeNameRes = when(currency.type_name.lowercase()) {
                "fiat" -> Res.string.currency_type_fiat
                "metal" -> Res.string.currency_type_metal
                "crypto" -> Res.string.currency_type_crypto
                else -> null
            }
            val typeName = typeNameRes?.let { stringResource(it) } ?: currency.type_name

            Box(modifier = Modifier.fillMaxSize()) {
                if (isDeprecated) {
                    DiagonalBanner(
                        text = "OLD",
                        modifier = Modifier.align(Alignment.TopEnd).offset(x = 12.dp, y = (3).dp),
                        backgroundColor = errorColor
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currency.symbol_native,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = "${currency.code} • ${typeName.uppercase()}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = currency.name_plural,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    )
}
