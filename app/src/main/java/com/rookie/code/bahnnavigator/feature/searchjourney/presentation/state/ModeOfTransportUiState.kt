package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state

data class ModeOfTransportUiState(
    val isVisible: Boolean = false,
    val selectedMode: String = "Local/regional transport only",
    val highSpeedTrains: Boolean = false,
    val intercityTrains: Boolean = false,
    val interregioTrains: Boolean = false,
    val regionalTrains: Boolean = true,
    val sBahn: Boolean = true,
    val busses: Boolean = true,
    val boats: Boolean = true,
    val underground: Boolean = true,
    val tram: Boolean = true,
    val telServices: Boolean = true
)
