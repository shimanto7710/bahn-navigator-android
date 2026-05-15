package com.rookie.code.bahnnavigator.feature.searchjourney.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rookie.code.bahnnavigator.core.location.LocationProvider
import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto.SearchStationModelElement
import com.rookie.code.bahnnavigator.feature.searchjourney.data.repository.FavoriteRepository
import com.rookie.code.bahnnavigator.feature.searchjourney.data.repository.LocationRepository
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.DateTimePickerUiState
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.LocationPickerUiState
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.PickerTarget
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.SearchJourneyUiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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

    fun onDateClick() {
        _uiState.update { it.copy(datePicker = it.datePicker.copy(isVisible = true)) }
    }

    fun onDatePickerDismiss() {
        _uiState.update { it.copy(datePicker = it.datePicker.copy(isVisible = false)) }
    }

    fun onDatePickerDone() {
        val picker = _uiState.value.datePicker
        _uiState.update {
            it.copy(
                date = formatDateLabel(picker),
                datePicker = it.datePicker.copy(isVisible = false)
            )
        }
    }

    fun onDateSelected(year: Int, month: Int, day: Int) {
        _uiState.update {
            it.copy(datePicker = it.datePicker.copy(
                selectedYear = year, selectedMonth = month, selectedDay = day
            ))
        }
    }

    fun onDepartureSelected() {
        _uiState.update { it.copy(datePicker = it.datePicker.copy(isDeparture = true)) }
    }

    fun onArrivalSelected() {
        _uiState.update { it.copy(datePicker = it.datePicker.copy(isDeparture = false)) }
    }

    fun onNowClick() {
        val now = Calendar.getInstance()
        _uiState.update {
            it.copy(datePicker = it.datePicker.copy(
                selectedHour = now.get(Calendar.HOUR_OF_DAY),
                selectedMinute = now.get(Calendar.MINUTE)
            ))
        }
    }

    fun onIn15MinClick() {
        val cal = Calendar.getInstance().apply { add(Calendar.MINUTE, 15) }
        _uiState.update {
            it.copy(datePicker = it.datePicker.copy(
                selectedHour = cal.get(Calendar.HOUR_OF_DAY),
                selectedMinute = cal.get(Calendar.MINUTE)
            ))
        }
    }

    fun onIn1HourClick() {
        val cal = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }
        _uiState.update {
            it.copy(datePicker = it.datePicker.copy(
                selectedHour = cal.get(Calendar.HOUR_OF_DAY),
                selectedMinute = cal.get(Calendar.MINUTE)
            ))
        }
    }

    private fun formatDateLabel(picker: DateTimePickerUiState): String {
        val today = Calendar.getInstance()
        val isToday = picker.selectedYear == today.get(Calendar.YEAR) &&
                picker.selectedMonth == today.get(Calendar.MONTH) + 1 &&
                picker.selectedDay == today.get(Calendar.DAY_OF_MONTH)
        val isAm = picker.selectedHour < 12
        val displayHour = when {
            picker.selectedHour == 0 -> 12
            picker.selectedHour > 12 -> picker.selectedHour - 12
            else -> picker.selectedHour
        }
        val timeStr = String.format(
            Locale.ENGLISH, "%d:%02d %s",
            displayHour, picker.selectedMinute, if (isAm) "AM" else "PM"
        )
        return if (isToday) {
            "Today, $timeStr"
        } else {
            val cal = Calendar.getInstance().apply {
                set(picker.selectedYear, picker.selectedMonth - 1, picker.selectedDay)
            }
            val dateStr = SimpleDateFormat("dd MMM", Locale.ENGLISH).format(cal.time)
            "$dateStr, $timeStr"
        }
    }

    fun onPassengersClick() = Unit

    fun onOptionsClick() {
        _uiState.update { it.copy(optionsPicker = it.optionsPicker.copy(isVisible = true)) }
    }

    fun onOptionsPickerDismiss() {
        _uiState.update { it.copy(optionsPicker = it.optionsPicker.copy(isVisible = false)) }
    }

    fun onOptionsPickerDone() {
        _uiState.update { it.copy(optionsPicker = it.optionsPicker.copy(isVisible = false)) }
    }

    fun onDTicketToggle(value: Boolean) {
        _uiState.update { it.copy(optionsPicker = it.optionsPicker.copy(dTicketOnly = value)) }
    }

    fun onBicycleToggle(value: Boolean) {
        _uiState.update { it.copy(optionsPicker = it.optionsPicker.copy(bicycleTransport = value)) }
    }

    fun onDirectServicesToggle(value: Boolean) {
        _uiState.update { it.copy(optionsPicker = it.optionsPicker.copy(directServicesOnly = value)) }
    }

    fun onOptionsReset() {
        _uiState.update {
            it.copy(optionsPicker = it.optionsPicker.copy(
                dTicketOnly = false,
                bicycleTransport = false,
                directServicesOnly = false,
                modeOfTransport = it.optionsPicker.modeOfTransport.copy(
                    selectedMode = "Local/regional transport only",
                    highSpeedTrains = false, intercityTrains = false,
                    interregioTrains = false, regionalTrains = true,
                    sBahn = true, busses = true, boats = true,
                    underground = true, tram = true, telServices = true
                ),
                transferTime = "Standard",
                stopovers = "None"
            ))
        }
    }

    fun onModeOfTransportClick() {
        _uiState.update {
            it.copy(optionsPicker = it.optionsPicker.copy(
                modeOfTransport = it.optionsPicker.modeOfTransport.copy(isVisible = true)
            ))
        }
    }

    fun onModeOfTransportDismiss() {
        _uiState.update {
            it.copy(optionsPicker = it.optionsPicker.copy(
                modeOfTransport = it.optionsPicker.modeOfTransport.copy(isVisible = false)
            ))
        }
    }

    fun onModeSelected(mode: String) {
        _uiState.update {
            val current = it.optionsPicker.modeOfTransport
            val updated = when (mode) {
                "All" -> current.copy(
                    selectedMode = mode,
                    highSpeedTrains = true, intercityTrains = true,
                    interregioTrains = true, regionalTrains = true,
                    sBahn = true, busses = true, boats = true,
                    underground = true, tram = true, telServices = true
                )
                "Long-distance travel only" -> current.copy(
                    selectedMode = mode,
                    highSpeedTrains = true, intercityTrains = true,
                    interregioTrains = true, regionalTrains = false,
                    sBahn = false, busses = false, boats = false,
                    underground = false, tram = false, telServices = false
                )
                "Local/regional transport only" -> current.copy(
                    selectedMode = mode,
                    highSpeedTrains = false, intercityTrains = false,
                    interregioTrains = false, regionalTrains = true,
                    sBahn = true, busses = true, boats = true,
                    underground = true, tram = true, telServices = true
                )
                else -> current.copy(selectedMode = mode)
            }
            it.copy(optionsPicker = it.optionsPicker.copy(modeOfTransport = updated))
        }
    }

    private fun updateTransport(block: (com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.ModeOfTransportUiState) -> com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.ModeOfTransportUiState) {
        _uiState.update {
            it.copy(optionsPicker = it.optionsPicker.copy(
                modeOfTransport = block(it.optionsPicker.modeOfTransport)
            ))
        }
    }

    fun onHighSpeedToggle(v: Boolean) = updateTransport { it.copy(highSpeedTrains = v) }
    fun onIntercityToggle(v: Boolean) = updateTransport { it.copy(intercityTrains = v) }
    fun onInterregioToggle(v: Boolean) = updateTransport { it.copy(interregioTrains = v) }
    fun onRegionalToggle(v: Boolean) = updateTransport { it.copy(regionalTrains = v) }
    fun onSBahnToggle(v: Boolean) = updateTransport { it.copy(sBahn = v) }
    fun onBussesToggle(v: Boolean) = updateTransport { it.copy(busses = v) }
    fun onBoatsToggle(v: Boolean) = updateTransport { it.copy(boats = v) }
    fun onUndergroundToggle(v: Boolean) = updateTransport { it.copy(underground = v) }
    fun onTramToggle(v: Boolean) = updateTransport { it.copy(tram = v) }
    fun onTelServicesToggle(v: Boolean) = updateTransport { it.copy(telServices = v) }
    fun onConnectionTypeClick() {
        _uiState.update { it.copy(connectionTypePicker = it.connectionTypePicker.copy(isVisible = true)) }
    }

    fun onConnectionTypeDismiss() {
        _uiState.update { it.copy(connectionTypePicker = it.connectionTypePicker.copy(isVisible = false)) }
    }

    fun onConnectionTypeSelected(option: String) {
        _uiState.update {
            it.copy(
                connectionType = option,
                connectionTypePicker = it.connectionTypePicker.copy(selected = option, isVisible = false)
            )
        }
    }
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
