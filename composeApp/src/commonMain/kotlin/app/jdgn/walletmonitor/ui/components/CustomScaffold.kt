package app.jdgn.walletmonitor.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import app.jdgn.walletmonitor.navigation.Navigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    title: String,
    navigator: Navigator? = null,
    leftContent: @Composable (() -> Unit)? = null,
    rightContent: @Composable (RowScope.() -> Unit)? = null,
    containerColor: Color? = null,
    topBarColor: Color = MaterialTheme.colorScheme.primary,
    isLarge: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = if (isLarge) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    } else {
        null
    }

    // Aplicamos transparencia al color de la barra independientemente del color recibido
    val translucentTopBarColor = topBarColor.copy(alpha = 0.7f)

    val topAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = translucentTopBarColor,
        scrolledContainerColor = translucentTopBarColor
    )

    Scaffold(
        modifier = Modifier.then(
            if (scrollBehavior != null) Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            else Modifier
        ),
        containerColor = containerColor ?: MaterialTheme.colorScheme.background,
        topBar = {
            if (isLarge) {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        if (navigator?.canGoBack() == true) {
                            IconButton(onClick = { navigator.goBack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        } else {
                            leftContent?.invoke()
                        }
                    },
                    actions = {
                        rightContent?.invoke(this)
                    },
                    scrollBehavior = scrollBehavior,
                    colors = topAppBarColors
                )
            } else {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        if (navigator?.canGoBack() == true) {
                            IconButton(onClick = { navigator.goBack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        } else {
                            leftContent?.invoke()
                        }
                    },
                    actions = {
                        rightContent?.invoke(this)
                    },
                    scrollBehavior = scrollBehavior,
                    colors = topAppBarColors
                )
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
