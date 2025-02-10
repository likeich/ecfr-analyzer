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
import gov.doge.ecfr.utils.toReadableString
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun <T> LineGraph(
    items: List<T>,
    labels: List<String> = emptyList(),
    modifier: Modifier = Modifier,
    title: String = "Chart",
    valueExtractor: (T) -> Double,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    if (items.isEmpty()) return

    val data by remember(items) {
        mutableStateOf(
            listOf(
                Line(
                    label = title,
                    values = items.map { valueExtractor(it) },
                    color = SolidColor(color),
                    firstGradientFillColor = color.copy(alpha = 0.5f),
                    secondGradientFillColor = Color.Transparent
                )
            )
        )
    }

    GraphCard(title = title, modifier = modifier) {
        LineChart(
            modifier = Modifier.fillMaxSize(),
            data = data,
            animationMode = AnimationMode.Together { it * 50L },
            labelProperties = LabelProperties(
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onSurface, fontSize = 10.sp),
                enabled = true,
                labels = labels,
                padding = 2.dp,
                rotation = LabelProperties.Rotation(mode = LabelProperties.Rotation.Mode.Force)
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
                enabled = true,
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        )
    }
}