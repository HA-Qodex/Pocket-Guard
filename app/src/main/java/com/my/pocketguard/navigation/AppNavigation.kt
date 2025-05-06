package com.my.pocketguard.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.my.pocketguard.view.CategoryView
import com.my.pocketguard.view.DashboardView
import com.my.pocketguard.view.FundView
import com.my.pocketguard.view.SplashView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = AppRoutes.SPLASH.route) {
        composable(AppRoutes.SPLASH.route) { SplashView(navController) }
        composable(AppRoutes.DASHBOARD.route) { DashboardView(navController) }
        composable(AppRoutes.FUND.route) { FundView(navController) }
        composable(AppRoutes.CATEGORY.route) { CategoryView(navController) }
    }

}