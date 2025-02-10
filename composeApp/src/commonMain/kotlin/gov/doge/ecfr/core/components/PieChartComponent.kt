package gov.doge.ecfr.core.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import gov.doge.ecfr.utils.toColor
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun <T> PieChartComponent(
    items: List<T>,
    selectedItem: T? = null,
    modifier: Modifier = Modifier,
    title: String = "Pie Chart",
    labelExtractor: (T) -> String,
    valueExtractor: (T) -> Double,
    colorExtractor: (T) -> Color = { labelExtractor(it).toColor() },
    onPieClick: (String?) -> Unit = {}
) {
    val data by remember(items) {
        mutableStateOf(
            items.map {
                Pie(
                    label = labelExtractor(it),
                    data = valueExtractor(it),
                    color = colorExtractor(it),
                    selectedColor = colorExtractor(it)
                )
            }
        )
    }

    GraphCard(
        title = title
    ) {
        PieChart(
            modifier = modifier,
            data = data.map {
                val label = if (selectedItem != null) labelExtractor(selectedItem) else null
                it.copy(selected = it.label == label)
            },
            onPieClick = { pie ->
                if (selectedItem != null && labelExtractor(selectedItem) == pie.label) {
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