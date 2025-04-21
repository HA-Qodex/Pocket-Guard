package com.my.pocketguard.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.my.pocketguard.component.AppBar
import com.my.pocketguard.ui.theme.BackgroundColorLite
import com.my.pocketguard.viewmodel.DashboardViewModel

@Composable
fun DashboardView(navController: NavController) {
    val context = LocalContext.current
    val viewModel: DashboardViewModel = hiltViewModel()
    val currentUser = viewModel.currentUser.collectAsState()
    LaunchedEffect(Unit) {
    }
    Scaffold(
        containerColor = BackgroundColorLite,
        topBar = {
            AppBar(
                context = context,
                imageUrl = currentUser.value?.photoUrl.toString(),
                name = currentUser.value?.displayName.toString()
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}