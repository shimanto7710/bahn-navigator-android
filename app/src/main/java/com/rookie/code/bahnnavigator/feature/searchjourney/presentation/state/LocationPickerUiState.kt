package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state

import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto.SearchStationModelElement

data class LocationPickerUiState(
    val isVisible: Boolean = false,
    val target: PickerTarget = PickerTarget.ORIGIN,
    val title: String = "",
    val searchText: String = "",
    val results: List<SearchStationModelElement> = emptyList(),
    val favorites: List<SearchStationModelElement> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
