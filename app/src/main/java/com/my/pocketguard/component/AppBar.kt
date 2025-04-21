package com.my.pocketguard.component

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.my.pocketguard.ui.theme.BackgroundColor
import com.my.pocketguard.ui.theme.appTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    context: Context,
    imageUrl: String,
    name: String
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NetworkImage(
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                    context = context, imageUrl = imageUrl, contentDescription = name)
                Spacer(modifier = Modifier.width(5.dp))
                Text(name, style = appTextStyle.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor),
    )
}