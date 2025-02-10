package gov.doge.ecfr.core.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.doge
import ecfranalyzer.composeapp.generated.resources.doge_logo
import ecfranalyzer.composeapp.generated.resources.question_fill
import gov.doge.ecfr.utils.getCurrentYear
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

object AboutScreen : DogeScreen() {
    override val icon: DrawableResource = Res.drawable.question_fill
    override val title: String = "About"

    @Composable
    override fun Content() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Image(
                    painter = painterResource(Res.drawable.doge),
                    contentDescription = "Doge Logo",
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    text = "Doge eCFR Analyzer",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Created by 1LT Kyle Eichlin © ${getCurrentYear()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}