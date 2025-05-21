package com.my.pocketguard.view.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.my.pocketguard.model.Expense
import com.my.pocketguard.ui.theme.Dimension.SizeM
import com.my.pocketguard.ui.theme.Dimension.SizeS
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.Dimension.TextS
import com.my.pocketguard.ui.theme.Dimension.TextXS
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.ui.theme.WhiteColor
import com.my.pocketguard.ui.theme.appTextStyle
import com.my.pocketguard.util.AppUtils.convertTimestampToLocal

@Composable
fun ExpenseList(expenses: List<Expense>) {
    LazyColumn(modifier = Modifier.padding(horizontal = SizeM, vertical = SizeS)) {
        items(expenses) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = SizeXS),
                colors = CardDefaults.cardColors(containerColor = WhiteColor),
                shape = RoundedCornerShape(SizeXS)
            ) {
                Column(modifier = Modifier.padding(SizeXS)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            it.title.toString(),
                            style = appTextStyle.copy(
                                fontSize = TextS,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            convertTimestampToLocal(it.date),
                            style = appTextStyle.copy(
                                fontSize = TextXS,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${it.fund?.fundName?.replaceFirstChar { it.uppercase() }} | ${it.category?.categoryName?.replaceFirstChar { it.uppercase() }}",
                            style = appTextStyle.copy(fontSize = TextS)
                        )
                        Text(
                            "৳${it.amount}",
                            style = appTextStyle.copy(
                                fontSize = TextS,
                                fontWeight = FontWeight.Bold,
                                color = RedColor
                            )
                        )
                    }
                }
            }
        }
    }
}