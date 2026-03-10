package app.jdgn.walletmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jdgn.walletmonitor.navigation.Navigator
import app.jdgn.walletmonitor.ui.components.CustomBox
import app.jdgn.walletmonitor.ui.components.CustomScaffold
import app.jdgn.walletmonitor.ui.components.FadingScroll
import app.jdgn.walletmonitor.ui.components.form.CustomTextField

@Composable
fun HomeScreen(navigator: Navigator) {
    CustomScaffold(
        title = "Home",
        navigator = navigator
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CustomTextField(
                value = "",
                onValueChange = {},
                label = "Search",
                isError = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            // Ejemplo 1: Scroll Horizontal con Fading y Safe Area automático
            Text("Horizontal Fading", modifier = Modifier.padding(16.dp))
            FadingScroll(
                modifier = Modifier.fillMaxWidth(),
                isVertical = false,
                fadeLength = 24.dp,
                padding = PaddingValues(horizontal = 8.dp, vertical = 4.dp) // Margen extra externo
            ) { scrollModifier ->
                Row(modifier = scrollModifier) {
                    repeat(10) { index ->
                        CustomBox(
                            width = 150.dp,
                            height = 80.dp,
                            margin = 4.dp
                        ) {
                            Text("Card #$index")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ejemplo 2: Scroll Vertical con Fading y Safe Area automático
            Text("Vertical Fading", modifier = Modifier.padding(horizontal = 16.dp))
            FadingScroll(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                isVertical = true,
                fadeLength = 40.dp,
                padding = PaddingValues(horizontal = 16.dp, vertical = 8.dp) // Diferentes márgenes
            ) { scrollModifier ->
                Column(
                    modifier = scrollModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(20) { index ->
                        CustomBox(
                            width = 280.dp,
                            height = 100.dp,
                            margin = 8.dp
                        ) {
                            Text("Vertical Item #$index")
                        }
                    }
                }
            }
        }
    }
}
