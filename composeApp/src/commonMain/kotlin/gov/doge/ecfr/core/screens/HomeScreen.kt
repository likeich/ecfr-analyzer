package gov.doge.ecfr.core.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.doge_logo
import ecfranalyzer.composeapp.generated.resources.house_fill
import org.jetbrains.compose.resources.painterResource

object HomeScreen : DogeScreen() {
    override val icon = Res.drawable.house_fill
    override val title = "Home"

    @Composable
    override fun Content() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.doge_logo),
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )
        }
    }
}