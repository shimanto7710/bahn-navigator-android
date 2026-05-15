package com.rookie.code.bahnnavigator.feature.searchjourney.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookie.code.bahnnavigator.core.location.LocationProvider
import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto.SearchStationModelElement
import com.rookie.code.bahnnavigator.feature.searchjourney.data.repository.FavoriteRepository
import com.rookie.code.bahnnavigator.feature.searchjourney.data.repository.LocationRepository
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.LocationPickerUiState
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.PickerTarget
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.SearchJourneyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchJourneyViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val locationProvider: LocationProvider,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchJourneyUiState())
    val uiState: StateFlow<SearchJourneyUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        observeSearchText()
        observeFavorites()
    }

    private fun observeSearchText() {
        _uiState
            .map { it.locationPicker.searchText to it.locationPicker.isVisible }
            .distinctUntilChanged()
            .filter { (_, visible) -> visible }
            .debounce(300)
            .onEach { (query, _) -> performSearch(query) }
            .launchIn(viewModelScope)
    }

    private fun observeFavorites() {
        favoriteRepository.observeFavorites()
            .onEach { favs ->
                _uiState.update {
                    it.copy(
                        locationPicker = it.locationPicker.copy(
                            favorites = favs,
                            favoriteIds = favs.map { f -> f.displayId }.toSet()
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onFromChange(value: String) {
        _uiState.update { it.copy(from = value) }
    }

    fun onToChange(value: String) {
        _uiState.update { it.copy(to = value) }
    }

    fun onSwapRouteClick() {
        _uiState.update { it.copy(from = it.to, to = it.from) }
    }

    fun onDateClick() = Unit
    fun onPassengersClick() = Unit
    fun onOptionsClick() = Unit
    fun onConnectionTypeClick() = Unit
    fun onSearchClick() = Unit

    fun onLocationPickerOpened(target: PickerTarget) {
        _uiState.update {
            it.copy(
                locationPicker = it.locationPicker.copy(
                    isVisible = true,
                    target = target,
                    title = if (target == PickerTarget.ORIGIN)
                        "Select point of departure" else "Select destination",
                    searchText = "",
                    results = emptyList(),
                    errorMessage = null,
                    isLoading = false
                )
            )
        }
    }

    fun onLocationPickerDismiss() {
        searchJob?.cancel()
        _uiState.update {
            it.copy(locationPicker = it.locationPicker.copy(isVisible = false))
        }
    }

    fun onLocationSearchChange(query: String) {
        _uiState.update {
            it.copy(locationPicker = it.locationPicker.copy(searchText = query))
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.update {
                it.copy(
                    locationPicker = it.locationPicker.copy(
                        results = emptyList(),
                        isLoading = false,
                        errorMessage = null
                    )
                )
            }
            return
        }
        searchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    locationPicker = it.locationPicker.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                )
            }
            locationRepository.searchLocations(query)
                .onSuccess { results -> updateResults(results) }
                .onFailure { e -> updateError(e.message) }
        }
    }

    private fun getCurrentLocation() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    locationPicker = it.locationPicker.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                )
            }
            val coords = locationProvider.current()
            if (coords == null) {
                updateError("Unable to get current location")
                return@launch
            }
            val (lat, lon) = coords
            locationRepository.getNearbyLocations(lat, lon)
                .onSuccess { results -> updateResults(results) }
                .onFailure { e -> updateError(e.message) }
        }
    }

    private fun updateResults(results: List<SearchStationModelElement>) {
        _uiState.update {
            it.copy(
                locationPicker = it.locationPicker.copy(
                    results = results,
                    isLoading = false
                )
            )
        }
    }

    private fun updateError(message: String?) {
        _uiState.update {
            it.copy(
                locationPicker = it.locationPicker.copy(
                    isLoading = false,
                    errorMessage = message ?: "Network error"
                )
            )
        }
    }

    fun onLocationSelected(location: SearchStationModelElement) {
        _uiState.update { state ->
            val picker = state.locationPicker
            val closed = picker.copy(isVisible = false)
            if (picker.target == PickerTarget.ORIGIN)
                state.copy(from = location.name, locationPicker = closed)
            else
                state.copy(to = location.name, locationPicker = closed)
        }
    }

    fun onToggleFavorite(location: SearchStationModelElement) {
        viewModelScope.launch { favoriteRepository.toggle(location) }
    }

    fun onCurrentPositionClick() = getCurrentLocation()
}
