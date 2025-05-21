package com.my.pocketguard.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChart() {
    LineChart(
        linesChartData = listOf(LineChartData(points = listOf(LineChartData.Point(1f,"Label 1"), ...))),
    // Optional properties.
    modifier = Modifier.fillMaxSize(),
    animation = simpleChartAnimation(),
    pointDrawer = FilledCircularPointDrawer(),
    lineDrawer = SolidLineDrawer(),
    xAxisDrawer = SimpleXAxisDrawer(),
    yAxisDrawer = SimpleYAxisDrawer(),
    horizontalOffset = 5f,
    labels = listOf("label 1" ...)
    )
}
