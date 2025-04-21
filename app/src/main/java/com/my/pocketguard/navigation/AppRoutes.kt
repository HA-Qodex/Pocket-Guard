package com.my.pocketguard.navigation

sealed class AppRoutes(
    val route: String
) {
    data object SPLASH : AppRoutes("splash")
    data object DASHBOARD : AppRoutes("dashboard")
}