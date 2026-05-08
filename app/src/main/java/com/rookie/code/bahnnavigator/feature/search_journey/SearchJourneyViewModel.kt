package com.rookie.code.bahnnavigator.feature.search_journey

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SearchJourneyUiState(
    val from: String = "",
    val to: String = "",
    val date: String = "Today, 12:17",
    val passengers: String = "1 pers. | 2nd Cl.",
    val options: String = "Means of transport",
    val connectionType: String = "Fastest Route"
)

class SearchJourneyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchJourneyUiState())
    val uiState: StateFlow<SearchJourneyUiState> = _uiState.asStateFlow()

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
}
