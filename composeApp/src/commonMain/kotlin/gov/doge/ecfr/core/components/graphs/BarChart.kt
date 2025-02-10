package gov.doge.ecfr.core.components.graphs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gov.doge.ecfr.utils.toColor
import gov.doge.ecfr.utils.toReadableString
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties

@Composable
fun <T> BarChart(
    items: List<T>,
    selectedItem: T? = null,
    modifier: Modifier = Modifier,
    title: String = "Chart",
    labelExtractor: (T) -> String,
    valueExtractor: (T) -> Double,
    colorExtractor: (T) -> Color = { labelExtractor(it).toColor() },
    onBarClick: (String?) -> Unit = {}
) {
    if (items.isEmpty()) return

    val data by remember(items) {
        mutableStateOf(
            items.map {
                Bars(
                    label = labelExtractor(it),
                    values = listOf(
                        Bars.Data(
                            label = labelExtractor(it),
                            value = valueExtractor(it),
                            color = SolidColor(colorExtractor(it))
                        )
                    )
                )
            }
        )
    }

    GraphCard(title = title, modifier = modifier) {
        ColumnChart(
            modifier = Modifier.fillMaxSize(),
            data = data,
            onBarClick = { bar ->
                if (selectedItem != null && labelExtractor(selectedItem) == bar.label) {
                    onBarClick(null)
                } else {
                    onBarClick(bar.label)
                }
            },
            animationMode = AnimationMode.Together { it * 50L },
            labelProperties = LabelProperties(
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onSurface),
                padding = 2.dp,
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
