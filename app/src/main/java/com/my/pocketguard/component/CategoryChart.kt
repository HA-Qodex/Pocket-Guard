package com.my.pocketguard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.type.DateTime
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.model.PieChartData
import com.my.pocketguard.ui.theme.ChartColors
import com.my.pocketguard.ui.theme.Dimension.SizeM
import com.my.pocketguard.ui.theme.Dimension.SizeS
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.Dimension.TextM
import com.my.pocketguard.ui.theme.Dimension.TextS
import com.my.pocketguard.ui.theme.Dimension.TextXS
import com.my.pocketguard.ui.theme.appTextStyle
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CategoryChart(data: List<PieChartData>) {
    val gridState = rememberLazyGridState()
    val currentMonth = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("MMM, yyyy", Locale.getDefault()))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = SizeM)
    ) {
        Box(contentAlignment = Alignment.Center) {
            PieChart(
                modifier = Modifier.size(150.dp),
                data = { data }, isDonutChart = true
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    currentMonth,
                    style = appTextStyle.copy(fontSize = TextS, fontWeight = FontWeight.Bold)
                )
                Text(
                    "৳${
                        NumberFormat.getInstance(Locale("en", "IN"))
                            .format(
                                data.sumOf { it.value.toInt() })
                    }",
                    style = appTextStyle.copy(fontSize = TextM, fontWeight = FontWeight.Bold)
                )
            }
        }
        Spacer(modifier = Modifier.width(SizeS))
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(SizeXS),
            verticalArrangement = Arrangement.Center
        ) {
            itemsIndexed(data) { index, chatData ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = SizeXS),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Circle,
                            modifier = Modifier.size(SizeS),
                            tint = ChartColors[index], contentDescription = chatData.label
                        )
//                        Spacer(modifier = Modifier.width(SizeXS))
                        Text(chatData.label, style = appTextStyle.copy(fontSize = TextXS))
                        Spacer(modifier = Modifier.width(SizeXS))
                    }
                    Text(
                        "৳${chatData.value.toInt()}",
                        style = appTextStyle.copy(fontSize = TextXS, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
