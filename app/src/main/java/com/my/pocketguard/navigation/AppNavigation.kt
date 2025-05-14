package com.my.pocketguard.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.my.pocketguard.view.CategoryView
import com.my.pocketguard.view.dashboard.DashboardView
import com.my.pocketguard.view.ExpenseView
import com.my.pocketguard.view.FundView
import com.my.pocketguard.view.SplashView

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = AppRoutes.SPLASH.route) {
        composable(AppRoutes.SPLASH.route) { SplashView(navController) }
        composable(AppRoutes.DASHBOARD.route) { DashboardView(navController) }
        composable(AppRoutes.EXPENSE.route) { ExpenseView(navController) }
        composable(AppRoutes.FUND.route) { FundView(navController) }
        composable(AppRoutes.CATEGORY.route) { CategoryView(navController) }
    }

}