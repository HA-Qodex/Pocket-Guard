package com.my.pocketguard.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.my.pocketguard.component.AppBar
import com.my.pocketguard.component.AppDropdown
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.Dimension.LargePadding

@Composable
fun ExpenseView(navController: NavController) {
    Scaffold(topBar = { AppBar(navController, "Expense", route = AppRoutes.DASHBOARD.route) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = LargePadding)
        ) {
//            AppDropdown()
        }
    }
}