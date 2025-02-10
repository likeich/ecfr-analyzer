package gov.doge.ecfr.core.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.binoculars_fill
import gov.doge.ecfr.core.components.LimitedColumn
import org.jetbrains.compose.resources.DrawableResource

object SearchScreen : DogeScreen() {
    override val icon = Res.drawable.binoculars_fill
    override val title = "Search"

    @Composable
    override fun Content() {
        LimitedColumn {
            Text("Search screen content")
        }
    }
}