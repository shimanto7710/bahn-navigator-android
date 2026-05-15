package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state

import java.util.Calendar

data class DateTimePickerUiState(
    val isVisible: Boolean = false,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val selectedDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    val selectedHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    val selectedMinute: Int = Calendar.getInstance().get(Calendar.MINUTE),
    val isDeparture: Boolean = true
)
