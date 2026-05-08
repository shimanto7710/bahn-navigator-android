package com.rookie.code.bahnnavigator.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rookie.code.bahnnavigator.feature.search_journey.SearchJourneyRoute
import com.rookie.code.bahnnavigator.feature.search_journey.SearchJourneyScreen
import com.rookie.code.bahnnavigator.feature.search_journey.SearchJourneyUiState
import com.rookie.code.bahnnavigator.navigation.HomeTabRoute
import com.rookie.code.bahnnavigator.navigation.homeTabRoutes
import com.rookie.code.bahnnavigator.ui.theme.BahnNavigatorTheme
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeRoute() {
    val bottomNavController = rememberNavController()

    HomeScreen(
        navController = bottomNavController,
    )
}

@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    Scaffold(
        bottomBar = {
            HomeBottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeTabRoute.Booking.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(HomeTabRoute.Booking.route) {
                SearchJourneyRoute()
            }
            composable(HomeTabRoute.Nearby.route) {
                HomePlaceholder("Nearby")
            }
            composable(HomeTabRoute.Journeys.route) {
                HomePlaceholder("Journeys")
            }
            composable(HomeTabRoute.Profile.route) {
                HomePlaceholder("Profile")
            }
        }
    }
}

@Composable
private fun HomeBottomBar(
    navController: NavHostController
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF242833),
        contentColor = Color.White
    ) {
        homeTabRoutes.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title
                    )
                },
                label = {
                    Text(text = tab.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color(0xFF8E93A1),
                    unselectedTextColor = Color(0xFF8E93A1),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun HomePlaceholder(title: String) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun HomeScreenPreview() {
    BahnNavigatorTheme {
        HomeRoute()
    }
}
