package gov.doge.ecfr.core.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.books_fill
import gov.doge.ecfr.core.LocalAppState
import gov.doge.ecfr.core.components.LimitedColumn
import org.jetbrains.compose.resources.DrawableResource

object TitlesScreen : DogeScreen() {
    override val icon: DrawableResource = Res.drawable.books_fill
    override val title: String = "Titles"

    @Composable
    override fun Content() {
        LimitedColumn {
            LocalAppState.current.titles.forEach { title ->
                Text(title.number.toString() + " - " + title.name + " - " + title.latestIssueDate)
            }
        }
    }
}