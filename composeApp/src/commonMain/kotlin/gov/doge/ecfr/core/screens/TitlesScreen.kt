package gov.doge.ecfr.core.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.books_fill
import gov.doge.ecfr.core.LocalAppState
import gov.doge.ecfr.core.components.graphs.BarChart
import gov.doge.ecfr.core.components.EnumDropdownButton
import gov.doge.ecfr.core.components.LimitedColumn
import gov.doge.ecfr.core.components.graphs.LineGraph
import gov.doge.ecfr.core.components.graphs.PieChartComponent
import gov.doge.ecfr.core.components.SimpleCard
import gov.doge.ecfr.core.screenmodels.FilterBy
import gov.doge.ecfr.core.screenmodels.SortBy
import gov.doge.ecfr.core.screenmodels.TitlesScreenModel
import gov.doge.ecfr.theme.Dimensions
import gov.doge.ecfr.utils.toColor
import org.jetbrains.compose.resources.DrawableResource

object TitlesScreen : DogeScreen() {
    override val icon: DrawableResource = Res.drawable.books_fill
    override val title: String = "Titles"

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val appState = LocalAppState.current
        val screenModel = rememberScreenModel { TitlesScreenModel() }
        val topTitles by remember(
            appState.titles.size,
            appState.corrections.size,
            appState.titleCorrections.size,
            screenModel.selectedTitle,
            screenModel.filterBy,
            screenModel.sortBy
        ) {
            mutableStateOf(screenModel.getFilteredTitles(appState.titles, appState))
        }
        val sortedTitles = screenModel.sortTitles(appState.titles, appState)

        LimitedColumn {
            SortAndFilterOptions(screenModel = screenModel)

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                PieChartComponent(
                    items = topTitles,
                    selectedItem = screenModel.selectedTitle,
                    modifier = Modifier.size(Dimensions.chartSize),
                    title = "Corrections by Title",
                    labelExtractor = { it.number.toString() },
                    valueExtractor = { title ->
                        appState.titleCorrections[title]?.size?.toDouble() ?: 0.0
                                     },
                    colorExtractor = { it.name.toColor() },
                    onPieClick = { label ->
                        if (label == null) {
                            screenModel.onTitleSelected(null)
                        } else {
                            screenModel.onTitleSelected(appState.titles.find { it.number.toString() == label })
                        }
                    }
                )

                BarChart(
                    items = topTitles,
                    selectedItem = screenModel.selectedTitle,
                    modifier = Modifier.size(Dimensions.chartSize),
                    title = "Corrections by Title",
                    labelExtractor = { it.number.toString() },
                    valueExtractor = { title ->
                        appState.titleCorrections[title]?.size?.toDouble() ?: 0.0
                    },
                    colorExtractor = { it.name.toColor() },
                    onBarClick = { label ->
                        if (label == null) {
                            screenModel.onTitleSelected(null)
                        } else {
                            screenModel.onTitleSelected(appState.titles.find { it.number.toString() == label })
                        }
                    }
                )

                val corrections = screenModel.getFilteredCorrections(appState).groupBy { it.date.year }
                LineGraph(
                    items = corrections.entries.sortedBy { it.key }.map { it.value },
                    labels = corrections.keys.toList().sorted().map { it.toString() },
                    modifier = Modifier.size(Dimensions.chartSize),
                    title = "Corrections Over Time",
                    valueExtractor = { it.size.toDouble() },
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(200.dp),
                userScrollEnabled = true,
                verticalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                modifier = Modifier.heightIn(max = 6000.dp)
            ) {
                items(sortedTitles, key = { it.number }) { title ->
                    val corrections = appState.titleCorrections[title] ?: emptyList()
                    SimpleCard(
                        title = "${title.number} - ${title.name}",
                        subtitle = "${corrections.size} corrections",
                        active = topTitles.contains(title),
                        selected = screenModel.selectedTitle == title,
                        onClick = { screenModel.onTitleSelected(title) }
                    )
                }
            }
        }
    }
}

@Composable
fun SortAndFilterOptions(screenModel: TitlesScreenModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        EnumDropdownButton(
            label = "Sort by",
            options = arrayOf(SortBy.NAME, SortBy.CORRECTION_COUNT, SortBy.DATE),
            onOptionSelected = { screenModel.sortBy = it },
            selectedOption = screenModel.sortBy
        )
        EnumDropdownButton(
            label = "Filter by",
            options = FilterBy.entries.toTypedArray(),
            onOptionSelected = { screenModel.filterBy = it },
            selectedOption = screenModel.filterBy
        )
    }
}
