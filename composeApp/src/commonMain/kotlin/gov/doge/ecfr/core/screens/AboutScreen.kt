package gov.doge.ecfr.core.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.question_fill
import org.jetbrains.compose.resources.DrawableResource

object AboutScreen : DogeScreen() {
    override val icon: DrawableResource = Res.drawable.question_fill
    override val title: String = "About"

    @Composable
    override fun Content() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Text("Kyle Eichlin")
            }
        }
    }
}