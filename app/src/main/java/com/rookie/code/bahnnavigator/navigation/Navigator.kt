package com.rookie.code.bahnnavigator.navigation

import androidx.compose.runtime.Composable
import com.rookie.code.bahnnavigator.feature.home.HomeRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun BahnNavigatorApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route
    ) {
        composable(AppRoute.Home.route) {
            HomeRoute()
        }
    }
}
