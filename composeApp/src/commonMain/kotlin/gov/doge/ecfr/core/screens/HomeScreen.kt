package gov.doge.ecfr.core.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import ecfranalyzer.composeapp.generated.resources.Res
import ecfranalyzer.composeapp.generated.resources.house_fill
import gov.doge.ecfr.api.data.models.Agency
import gov.doge.ecfr.core.LocalAppState
import gov.doge.ecfr.core.components.EnumDropdownButton
import gov.doge.ecfr.core.components.GraphCard
import gov.doge.ecfr.core.components.LimitedColumn
import gov.doge.ecfr.core.screenmodels.FilterBy
import gov.doge.ecfr.core.screenmodels.HomeScreenModel
import gov.doge.ecfr.core.screenmodels.SortBy
import gov.doge.ecfr.theme.Dimensions
import gov.doge.ecfr.utils.toColor
import gov.doge.ecfr.utils.toReadableString
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Pie
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.m3.TextWithNotoImageEmoji
import org.kodein.emoji.symbols.warning.Warning

object HomeScreen : DogeScreen() {
    override val icon = Res.drawable.house_fill
    override val title = "Home"

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { HomeScreenModel() }
        val appState = LocalAppState.current
        val agencies by remember(appState.agencies.map { it.wordCount }, screenModel.sortBy, screenModel.filterBy) {
            mutableStateOf(screenModel.getFilteredAgencies(appState.agencies))
        }
        val sortedAgencies = screenModel.sortAgencies(appState.agencies)
        val topAgencies = screenModel.getFilteredAgencies(appState.agencies)
        val gridState = rememberLazyGridState()

        LaunchedEffect(screenModel.sortBy, screenModel.filterBy) {
            gridState.scrollToItem(0)
        }

        LimitedColumn {
            SortAndFilterOptions(screenModel = screenModel)

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                WordCountPieChart(
                    agencies = agencies,
                    selectedAgency = screenModel.selectedAgency,
                    modifier = Modifier.size(250.dp),
                    onPieClick = { label ->
                        if (label == null) {
                            screenModel.onAgencySelected(null)
                        } else {
                            screenModel.onAgencySelected(appState.agencies.find { it.displayName == label })
                        }
                    }
                )

                WordCountBarChart(
                    agencies = agencies,
                    selectedAgency = screenModel.selectedAgency,
                    modifier = Modifier.size(250.dp),
                    onPieClick = { label ->
                        if (label == null) {
                            screenModel.onAgencySelected(null)
                        } else {
                            screenModel.onAgencySelected(appState.agencies.find { it.displayName == label })
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
                state = gridState,
                userScrollEnabled = true,
                verticalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.defaultPadding),
                modifier = Modifier.heightIn(max = 6000.dp)
            ) {
                items(sortedAgencies, key = { it.displayName }) { agency ->
                    AgencyCard(
                        agency = agency,
                        active = topAgencies.contains(agency),
                        selected = screenModel.selectedAgency == agency,
                        onAgencySelected = { screenModel.onAgencySelected(it) },
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
            options = SortBy.entries.toTypedArray(),
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

@Composable
fun WordCountPieChart(
    agencies: List<Agency>,
    selectedAgency: Agency? = null,
    modifier: Modifier = Modifier,
    onPieClick: (String?) -> Unit = {}
) {
    val data by remember(agencies) {
        mutableStateOf(
            agencies.map {
                Pie(
                    label = it.displayName,
                    data = it.wordCount.toDouble(),
                    color = it.displayName.toColor(),
                    selectedColor = it.displayName.toColor()
                )
            }
        )
    }

    GraphCard(
        title = "Agency Word Count",
    ) {
        PieChart(
            modifier = modifier,
            data = data.map {
                it.copy(selected = it.label == selectedAgency?.displayName)
            },
            onPieClick = { pie ->
                if (selectedAgency?.displayName == pie.label) {
                    onPieClick(null)
                } else {
                    onPieClick(pie.label)
                }
            },
            selectedScale = 1.2f,
            scaleAnimEnterSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            colorAnimEnterSpec = tween(300),
            colorAnimExitSpec = tween(300),
            scaleAnimExitSpec = tween(300),
            spaceDegreeAnimExitSpec = tween(300),
            style = Pie.Style.Fill
        )
    }
}

@Composable
fun WordCountBarChart(
    agencies: List<Agency>,
    selectedAgency: Agency? = null,
    modifier: Modifier = Modifier,
    onPieClick: (String?) -> Unit = {}
) {
    if (agencies.isEmpty()) {
        return
    }

    val data by remember(agencies) {
        mutableStateOf(
            agencies.map {
                Bars(
                    label = it.shortName ?: it.displayName,
                    values = listOf(
                        Bars.Data(
                            label = it.displayName,
                            value = it.wordCount.toDouble(),
                            color = SolidColor(it.displayName.toColor())
                        )
                    )
                )
            }
        )
    }

    GraphCard(
        title = "Agency Word Count",
    ) {
        ColumnChart(
            modifier = modifier,
            data = data,
            onBarClick = { pie ->
                if (selectedAgency?.displayName == pie.label) {
                    onPieClick(null)
                } else {
                    onPieClick(pie.label)
                }
            },
            animationMode = AnimationMode.Together { it * 50L },
            labelProperties = LabelProperties(
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onSurface),
                enabled = true,
            ),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                textStyle = TextStyle.Default.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.background,
                        blurRadius = 10f
                    )
                ),
                count = IndicatorCount.CountBased(5),
                contentBuilder = { double ->
                    double.toReadableString()
                }
            ),
            labelHelperProperties = LabelHelperProperties(
                enabled = false,
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onSurface)
            ),
        )
    }
}

@Composable
fun AgencyInformation(agency: Agency, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.smallPadding),
            modifier = Modifier.padding(Dimensions.defaultPadding)
        ) {
            Text(
                text = "${agency.displayName} - ${agency.shortName ?: "Information"}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            if (agency.children.isNotEmpty()) {
                Text(
                    text = "Child Agencies: ${agency.children.joinToString("\n") { "${it.displayName} - ${it.shortName}" }}",
                )
            }
            Text(
                text = "CFR References:\n${agency.cfrReferences.joinToString(separator = "\n") { it.toCfrHierarchy().toString() }}"
            )
        }
    }
}

@Composable
fun AgencyCard(
    agency: Agency,
    active: Boolean = true,
    selected: Boolean = false,
    modifier: Modifier = Modifier,
    onAgencySelected: (Agency) -> Unit = {}
) {
    val appState = LocalAppState.current
    val alpha = if (active) 1f else 0.4f
    val containerColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified
    )

    ElevatedCard(
        onClick = { onAgencySelected(agency) },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = Modifier.alpha(alpha)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimensions.smallPadding),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = agency.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2,
                    maxLines = 2
                )
                val emoji: String = if (agency.wordCount > appState.averageWordCount.times(1.25)) {
                    "${Emoji.Warning} "
                } else {
                    ""
                }
                TextWithNotoImageEmoji(
                    text = "$emoji${agency.wordCount.toDouble().toReadableString()} words",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}