package gov.doge.ecfr.core.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.doge
import ecfranalyzer.composeapp.generated.resources.doge_logo
import ecfranalyzer.composeapp.generated.resources.question_fill
import gov.doge.ecfr.theme.Dimensions
import gov.doge.ecfr.utils.getCurrentYear
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.m3.TextWithNotoImageEmoji
import org.kodein.emoji.objects.tool.Link
import org.kodein.emoji.travel_places.place_map.GlobeWithMeridians

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
                verticalArrangement = Arrangement.spacedBy(Dimensions.smallPadding),
            ) {
                Image(
                    painter = painterResource(Res.drawable.doge),
                    contentDescription = "DOGE Logo",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.size(Dimensions.defaultPadding))

                Text(
                    text = "DOGE eCFR Analyzer",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Such Work, Much Skill, Very Hire",
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Created by 1LT Kyle Eichlin © ${getCurrentYear()}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.alpha(.8f)
                )

                Spacer(modifier = Modifier.size(Dimensions.defaultPadding))


                WebLink(
                    text = "DOGE eCFR Analyzer GitHub",
                    url = "https://github.com/likeich/ecfr-analyzer"
                )
                WebLink(
                    text = "eCFR Website",
                    url = "https://www.ecfr.gov/"
                )
                WebLink(
                    text = "CFR Bulk Data",
                    url = "https://www.govinfo.gov/bulkdata/CFR"
                )
            }
        }
    }
}

@Composable
fun WebLink(text: String, url: String) {
    val uriHandler = LocalUriHandler.current

    OutlinedButton(
        onClick = { uriHandler.openUri(url) }
    ) {
        TextWithNotoImageEmoji(
            text = text + " ${Emoji.GlobeWithMeridians}"
        )
    }
}