package gov.doge.ecfr.core.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.binoculars_fill
import gov.doge.ecfr.core.LocalAppState
import gov.doge.ecfr.core.components.LimitedColumn
import gov.doge.ecfr.core.components.SimpleCard
import gov.doge.ecfr.core.screenmodels.SearchScreenModel
import gov.doge.ecfr.theme.Dimensions
import org.jetbrains.compose.resources.DrawableResource
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.m3.TextWithNotoImageEmoji
import org.kodein.emoji.objects.light_video.MagnifyingGlassTiltedRight
import org.kodein.emoji.objects.tool.Link

object SearchScreen : DogeScreen() {
    override val icon = Res.drawable.binoculars_fill
    override val title = "Search"

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { SearchScreenModel() }
        val appState = LocalAppState.current
        val uriHandler = LocalUriHandler.current

        LimitedColumn {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                    modifier = Modifier.padding(Dimensions.smallPadding)
                ) {
                    TextField(
                        value = screenModel.searchQuery,
                        onValueChange = { screenModel.searchQuery = it },
                        label = { Text("Search eCFR") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions {
                            defaultKeyboardAction(ImeAction.Search)
                        }
                    )
                    Button(onClick = { screenModel.search(appState.client) }) {
                        TextWithNotoImageEmoji("${Emoji.MagnifyingGlassTiltedRight} Search")
                    }
                }
            }

            screenModel.searchResults.forEach { result ->
                Box(
                    modifier = Modifier.padding(horizontal = Dimensions.defaultPadding)
                ) {
                    SimpleCard(
                        title = result.hierarchy.toReadableString() + " ${Emoji.Link}",
                        subtitle = result.fullTextExcerpt ?: result.type,
                        onClick = { uriHandler.openUri(result.hierarchy.toUrl()) }
                    )
                }
            }
        }
    }
}