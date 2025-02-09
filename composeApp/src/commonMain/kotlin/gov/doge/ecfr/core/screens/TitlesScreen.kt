package gov.doge.ecfr.core.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.books_fill
import gov.doge.ecfr.core.LocalAppState
import gov.doge.ecfr.core.screenmodels.TitleScreenModel
import org.jetbrains.compose.resources.DrawableResource

object TitlesScreen : DogeScreen() {
    override val icon: DrawableResource = Res.drawable.books_fill
    override val title: String = "Titles"

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            LocalAppState.current.titles.forEach { title ->
                Text(title.number.toString() + " - " + title.name)
            }
        }
    }
}