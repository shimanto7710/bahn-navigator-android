package com.rookie.code.bahnnavigator.feature.searchjourney.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.components.LocationPickerSheet
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.PickerTarget

@Composable
fun SearchJourneyRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchJourneyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) viewModel.onCurrentPositionClick()
    }

    val requestLocation = {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            viewModel.onCurrentPositionClick()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    SearchJourneyScreen(
        modifier = modifier,
        uiState = uiState,
        onFromChange = viewModel::onFromChange,
        onToChange = viewModel::onToChange,
        onFromClick = { viewModel.onLocationPickerOpened(PickerTarget.ORIGIN) },
        onToClick = { viewModel.onLocationPickerOpened(PickerTarget.DESTINATION) },
        onSwapRouteClick = viewModel::onSwapRouteClick,
        onDateClick = viewModel::onDateClick,
        onPassengersClick = viewModel::onPassengersClick,
        onOptionsClick = viewModel::onOptionsClick,
        onConnectionTypeClick = viewModel::onConnectionTypeClick,
        onSearchClick = viewModel::onSearchClick
    )

    if (uiState.locationPicker.isVisible) {
        LocationPickerSheet(
            state = uiState.locationPicker,
            onDismiss = viewModel::onLocationPickerDismiss,
            onSearchChange = viewModel::onLocationSearchChange,
            onSelect = viewModel::onLocationSelected,
            onToggleFavorite = viewModel::onToggleFavorite,
            onCurrentPositionClick = requestLocation,
            onNearbyStopsClick = requestLocation
        )
    }
}
