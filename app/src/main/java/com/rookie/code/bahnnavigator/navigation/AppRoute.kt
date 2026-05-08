package com.rookie.code.bahnnavigator.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.ui.graphics.vector.ImageVector


sealed class AppRoute(val route: String) {
    data object Home : AppRoute("home")
}

sealed class HomeTabRoute(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Booking : HomeTabRoute(
        route = "booking",
        title = "Booking",
        icon = Icons.Default.SwapHoriz
    )

    data object Nearby : HomeTabRoute(
        route = "nearby",
        title = "Nearby",
        icon = Icons.Default.LocationOn
    )

    data object Journeys : HomeTabRoute(
        route = "journeys",
        title = "Journeys",
        icon = Icons.Default.ConfirmationNumber
    )

    data object Profile : HomeTabRoute(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.AccountCircle
    )
}

val homeTabRoutes = listOf(
    HomeTabRoute.Booking,
    HomeTabRoute.Nearby,
    HomeTabRoute.Journeys,
    HomeTabRoute.Profile
)
