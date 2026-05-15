package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state

data class OptionsUiState(
    val isVisible: Boolean = false,
    val modeOfTransport: ModeOfTransportUiState = ModeOfTransportUiState(),
    val dTicketOnly: Boolean = false,
    val bicycleTransport: Boolean = false,
    val directServicesOnly: Boolean = false,
    val transferTime: String = "Standard",
    val stopovers: String = "None"
)
