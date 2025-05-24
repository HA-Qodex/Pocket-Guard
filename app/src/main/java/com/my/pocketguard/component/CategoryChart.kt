package com.my.pocketguard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.model.PieChartData
import com.my.pocketguard.ui.theme.ChartColors
import com.my.pocketguard.ui.theme.Dimension.SizeM
import com.my.pocketguard.ui.theme.Dimension.SizeS
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.Dimension.TextXS
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.ui.theme.appTextStyle

@Composable
fun CategoryChart(data: List<PieChartData>) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().height(150.dp)
    ) {
        PieChart(
            modifier = Modifier.size(150.dp),
            data = { data }, isDonutChart = true
        )
        Spacer(modifier = Modifier.width(SizeS))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(SizeXS),
            verticalArrangement = Arrangement.spacedBy(SizeXS),
            horizontalArrangement = Arrangement.spacedBy(SizeXS)
        ) {
            itemsIndexed(data) { index,chatData ->
                Row {
                    Icon(Icons.Default.Circle,
                        modifier = Modifier.size(SizeM),
                        tint = ChartColors[index], contentDescription = chatData.label)
                    Spacer(modifier = Modifier.width(SizeXS))
                    Text(chatData.label, style = appTextStyle.copy(fontSize = TextXS))
                    Spacer(modifier = Modifier.width(SizeXS))
                    Text("৳${chatData.value}", style = appTextStyle.copy(fontSize = TextXS, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
