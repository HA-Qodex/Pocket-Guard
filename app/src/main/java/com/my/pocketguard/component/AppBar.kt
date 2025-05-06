package com.my.pocketguard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.my.pocketguard.R
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.TextColor
import com.my.pocketguard.ui.theme.appTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navController: NavController,
    title: String,
    route: String
) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Top Bar Background",
            modifier = Modifier
                .fillMaxWidth().size(90.dp), // height of TopAppBar
            contentScale = ContentScale.FillBounds
        )
        TopAppBar(
            title = {
                Text(
                    title,
                    style = appTextStyle.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        color = TextColor
                    )
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//            modifier = Modifier.clip(
//                RoundedCornerShape(
//                    bottomEnd = 20.dp,
//                    bottomStart = 20.dp
//                )
//            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack(route, inclusive = false, saveState = false)

                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription =
                            "back_stack",
                        tint = TextColor
                    )
                }
            })
    }
}