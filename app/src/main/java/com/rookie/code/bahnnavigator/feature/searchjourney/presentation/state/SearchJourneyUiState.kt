package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state

data class SearchJourneyUiState(
    val from: String = "",
    val to: String = "",
    val date: String = "Today, 12:17",
    val passengers: String = "1 pers. | 2nd Cl.",
    val optionsLabel: String = "Means of transport",
    val connectionType: String = "Fastest Route",
    val locationPicker: LocationPickerUiState = LocationPickerUiState(),
    val datePicker: DateTimePickerUiState = DateTimePickerUiState(),
    val optionsPicker: OptionsUiState = OptionsUiState(),
    val connectionTypePicker: ConnectionTypeUiState = ConnectionTypeUiState()
)
