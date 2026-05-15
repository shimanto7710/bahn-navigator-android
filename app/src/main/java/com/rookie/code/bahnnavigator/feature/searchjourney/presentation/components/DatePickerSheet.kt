/*
package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto.SearchStationModelElement
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.LocationPickerUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSheet(
    state: LocationPickerUiState,
    onDismiss: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSelect: (SearchStationModelElement) -> Unit,
    onToggleFavorite: (SearchStationModelElement) -> Unit,
    onCurrentPositionClick: () -> Unit,
    onNearbyStopsClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null
    ) {
        DatePickerContent(
            state = state,
            focusRequester = focusRequester,
            onDismiss = onDismiss,
            onSearchChange = onSearchChange,
            onSelect = onSelect,
            onToggleFavorite = onToggleFavorite,
            onCurrentPositionClick = onCurrentPositionClick,
            onNearbyStopsClick = onNearbyStopsClick
        )
    }
}

@Composable
private fun DatePickerContent(
    state: LocationPickerUiState,
    focusRequester: FocusRequester,
    onDismiss: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSelect: (SearchStationModelElement) -> Unit,
    onToggleFavorite: (SearchStationModelElement) -> Unit,
    onCurrentPositionClick: () -> Unit,
    onNearbyStopsClick: () -> Unit
){

}*/
