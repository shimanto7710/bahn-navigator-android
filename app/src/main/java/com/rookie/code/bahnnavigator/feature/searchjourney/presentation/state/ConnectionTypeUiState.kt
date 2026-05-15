package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state

data class ConnectionTypeUiState(
    val isVisible: Boolean = false,
    val selected: String = "Fastest Route"
) {
    companion object {
        val OPTIONS = listOf("Fastest Route", "Reliable Route")
    }
}
