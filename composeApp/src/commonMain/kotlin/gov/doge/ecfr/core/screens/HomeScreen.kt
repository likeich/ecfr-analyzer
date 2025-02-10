package gov.doge.ecfr.core.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import ecfranalyzer.composeapp.generated.resources.house_fill
import gov.doge.ecfr.core.LocalAppState
import gov.doge.ecfr.core.components.AgencyInformation
import gov.doge.ecfr.core.components.graphs.BarChart
import gov.doge.ecfr.core.components.EnumDropdownButton
import gov.doge.ecfr.core.components.LimitedColumn
import gov.doge.ecfr.core.components.graphs.PieChartComponent
import gov.doge.ecfr.core.components.SimpleCard
import gov.doge.ecfr.core.screenmodels.FilterBy
import gov.doge.ecfr.core.screenmodels.HomeScreenModel
import gov.doge.ecfr.core.screenmodels.SortBy
import gov.doge.ecfr.theme.Dimensions
import gov.doge.ecfr.utils.toColor
import gov.doge.ecfr.utils.toReadableString

object HomeScreen : DogeScreen() {
    override val icon = Res.drawable.house_fill
    override val title = "Home"

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { HomeScreenModel() }
        val appState = LocalAppState.current
        val topAgencies by remember(appState.agencies.map { it.wordCount }, screenModel.sortBy, screenModel.filterBy) {
            mutableStateOf(screenModel.getFilteredAgencies(appState.agencies))
        }
        val sortedAgencies = screenModel.sortAgencies(appState.agencies)

        LimitedColumn {
            SortAndFilterOptions(screenModel = screenModel)

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                PieChartComponent(
                    items = topAgencies,
                    selectedItem = screenModel.selectedAgency,
                    modifier = Modifier.size(Dimensions.chartSize),
                    title = "Agency Word Count",
                    labelExtractor = { it.shortName ?: it.displayName },
                    valueExtractor = { it.wordCount.toDouble() },
                    colorExtractor = { it.displayName.toColor() },
                    onPieClick = { label ->
                        if (label == null) {
                            screenModel.onAgencySelected(null)
                        } else {
                            screenModel.onAgencySelected(appState.agencies.find { (it.shortName ?: it.displayName) == label })
                        }
                    }
                )

                BarChart(
                    items = topAgencies,
                    selectedItem = screenModel.selectedAgency,
                    modifier = Modifier.size(Dimensions.chartSize),
                    title = "Agency Word Count",
                    labelExtractor = { it.shortName ?: it.displayName },
                    valueExtractor = { it.wordCount.toDouble() },
                    colorExtractor = { it.displayName.toColor() },
                    onBarClick = { label ->
                        if (label == null) {
                            screenModel.onAgencySelected(null)
                        } else {
                            screenModel.onAgencySelected(appState.agencies.find { (it.shortName ?: it.displayName) == label })
                        }
                    }
                )
            }

            screenModel.selectedAgency?.let {
                AgencyInformation(
                    agency = it,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(200.dp),
                userScrollEnabled = true,
                verticalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                modifier = Modifier.heightIn(max = 6000.dp)
            ) {
                items(sortedAgencies, key = { it.displayName }) { agency ->
                    SimpleCard(
                        title = agency.displayName,
                        subtitle = "${agency.wordCount.toDouble().toReadableString()} words",
                        active = topAgencies.contains(agency),
                        selected = screenModel.selectedAgency == agency,
                        onClick = { screenModel.onAgencySelected(agency) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun SortAndFilterOptions(screenModel: HomeScreenModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        EnumDropdownButton(
            label = "Sort by",
            options = arrayOf(SortBy.WORD_COUNT, SortBy.NAME),
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
