package gov.doge.ecfr.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.doge_logo
import gov.doge.ecfr.core.screens.AboutScreen
import gov.doge.ecfr.core.screens.HomeScreen
import gov.doge.ecfr.core.screens.TitlesScreen
import gov.doge.ecfr.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.NotoImageEmoji
import org.kodein.emoji.symbols.other_symbol.CheckMarkGreen
import org.kodein.emoji.symbols.warning.Warning

@Composable
@Preview
fun App() {
    val appState = remember { AppState() }

    LaunchedEffect(appState) {
        appState.load()
    }

    CompositionLocalProvider(LocalAppState provides appState) {
        AppTheme {
            Navigator(HomeScreen) { navigator ->
                Scaffold(
                    topBar = { TopBar() },
                    bottomBar = { BottomBar() },
                    content = { paddingValues ->
                        Surface(
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SlideTransition(
                                navigator = navigator,
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.doge_logo),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "eCFR Analyzer",
                    modifier = Modifier.weight(1f)
                )

                StateView()
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    )
}

@Composable
fun RowScope.StateView() {
    val appState = LocalAppState.current

    when (appState.state) {
        is State.Loading -> CircularProgressIndicator(
            modifier = Modifier.size(24.dp)
        )
        is State.Loaded -> NotoImageEmoji(
            emoji = Emoji.CheckMarkGreen,
            modifier = Modifier.size(24.dp)
        )
        is State.Error -> NotoImageEmoji(
            emoji = Emoji.Warning,
            modifier = Modifier.size(24.dp)
        )
    }

    Text(
        text = appState.state.message,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(end = 12.dp)
    )
}

@Composable
fun BottomBar() {
    val navigator = LocalNavigator.currentOrThrow
    val screens = listOf(HomeScreen, TitlesScreen, AboutScreen)

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = navigator.lastItem == screen,
                onClick = { navigator.push(screen) },
                icon = {
                    Icon(
                        painter = painterResource(screen.icon),
                        contentDescription = null
                    )
                },
                label = { Text(screen.title) }
            )
        }
    }
}