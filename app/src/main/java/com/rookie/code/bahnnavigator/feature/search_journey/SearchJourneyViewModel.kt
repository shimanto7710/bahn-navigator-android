package com.rookie.code.bahnnavigator.feature.search_journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookie.code.bahnnavigator.core.LocationProvider
import com.rookie.code.bahnnavigator.feature.search_journey.data.LocationRepository
import com.rookie.code.bahnnavigator.feature.search_journey.data.SearchStationModelElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch


data class SearchJourneyUiState(
    val from: String = "",
    val to: String = "",
    val date: String = "Today, 12:17",
    val passengers: String = "1 pers. | 2nd Cl.",
    val options: String = "Means of transport",
    val connectionType: String = "Fastest Route",
    val locationPicker: LocationPickerUiState = LocationPickerUiState()
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchJourneyViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchJourneyUiState())
    val uiState: StateFlow<SearchJourneyUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        observeSearchText()
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



    fun onFromChange(value: String) {
        _uiState.update { it.copy(from = value) }
    }

    fun onToChange(value: String) {
        _uiState.update { it.copy(to = value) }
    }

    fun onSwapRouteClick() {
        _uiState.update {
            it.copy(
                from = it.to,
                to = it.from
            )
        }
    }

    fun onDateClick() {
        // CHANGE: Connect date picker navigation/dialog here later.
    }

    fun onPassengersClick() {
        // CHANGE: Connect passenger options here later.
    }

    fun onOptionsClick() {
        // CHANGE: Connect transport options here later.
    }

    fun onConnectionTypeClick() {
        // CHANGE: Connect connection type picker here later.
    }

    fun onSearchClick() {
        // CHANGE: Start journey search here later.
    }

    // --- Location picker sheet ---
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
                it.copy(locationPicker = it.locationPicker.copy(
                    results = emptyList(),
                    isLoading = false,
                    errorMessage = null
                ))
            }
            return
        }
        searchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(locationPicker = it.locationPicker.copy(
                    isLoading = true,
                    errorMessage = null
                ))
            }
            locationRepository.searchLocations(query)
                .onSuccess { results ->
                    _uiState.update {
                        it.copy(locationPicker = it.locationPicker.copy(
                            results = results,
                            isLoading = false
                        ))
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(locationPicker = it.locationPicker.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Network error"
                        ))
                    }
                }
        }
    }

    private fun getCurrentLocation() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(locationPicker = it.locationPicker.copy(
                    isLoading = true,
                    errorMessage = null
                ))
            }

            val coords = locationProvider.current()
            if (coords == null) {
                _uiState.update {
                    it.copy(locationPicker = it.locationPicker.copy(
                        isLoading = false,
                        errorMessage = "Unable to get current location"
                    ))
                }
                return@launch
            }

            val (lat, lon) = coords
            locationRepository.getNearbyLocations(lat, lon)
                .onSuccess { results ->
                    _uiState.update {
                        it.copy(locationPicker = it.locationPicker.copy(
                            results = results,
                            isLoading = false
                        ))
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(locationPicker = it.locationPicker.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Network error"
                        ))
                    }
                }
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
        _uiState.update {
            val ids = it.locationPicker.favoriteIds.toMutableSet()
            val id = location.displayId
            if (!ids.add(id)) ids.remove(id)
            it.copy(locationPicker = it.locationPicker.copy(favoriteIds = ids))
        }
    }

    fun onCurrentPositionClick() {
        getCurrentLocation()
    }

}

private inline fun <T> MutableStateFlow<T>.update(transform: (T) -> T) {
    value = transform(value)
}

enum class PickerTarget { ORIGIN, DESTINATION }

data class LocationPickerUiState(
    val isVisible: Boolean = false,
    val target: PickerTarget = PickerTarget.ORIGIN,
    val title: String = "",
    val searchText: String = "",
    val results: List<SearchStationModelElement> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)