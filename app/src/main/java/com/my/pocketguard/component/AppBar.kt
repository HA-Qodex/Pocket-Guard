package com.my.pocketguard.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.my.pocketguard.R
import com.my.pocketguard.ui.theme.BackgroundColor
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.appTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    context: Context,
    imageUrl: String,
    name: String,
    fundClick: () -> Unit,
    addClick: () -> Unit,
    categoryClick: () -> Unit
) {
    Box(modifier = Modifier.height(140.dp)) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Top Bar Background",
            modifier = Modifier
                .fillMaxSize(), // height of TopAppBar
            contentScale = ContentScale.FillBounds
        )
        TopAppBar(
            expandedHeight = 140.dp,
            title = {
                Column(
                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NetworkImage(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            context = context, imageUrl = imageUrl, contentDescription = name
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            name,
                            style = appTextStyle.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxSize().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        RoundedButton(
                            icon = Icons.Filled.AccountBalanceWallet,
                            onClick = fundClick)
                        RoundedButton(
                            icon = Icons.Filled.Add,
                            onClick = addClick)
                        RoundedButton(
                            icon = Icons.Filled.Category,
                            onClick = categoryClick)
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        )
    }
}