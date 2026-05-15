package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.rookie.code.bahnnavigator.core.ui.theme.TextLightColor
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.DateTimePickerUiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val TOTAL_PAGES = 25
private const val INITIAL_PAGE = 12

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSheet(
    state: DateTimePickerUiState,
    onDismiss: () -> Unit,
    onDone: () -> Unit,
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit,
    onDepartureSelected: () -> Unit,
    onArrivalSelected: () -> Unit,
    onNowClick: () -> Unit,
    onIn15MinClick: () -> Unit,
    onIn1HourClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null,
        modifier = Modifier.fillMaxSize()
    ) {
        DatePickerContent(
            state = state,
            onDone = onDone,
            onDateSelected = onDateSelected,
            onDepartureSelected = onDepartureSelected,
            onArrivalSelected = onArrivalSelected,
            onNowClick = onNowClick,
            onIn15MinClick = onIn15MinClick,
            onIn1HourClick = onIn1HourClick
        )
    }
}

@Composable
private fun DatePickerContent(
    state: DateTimePickerUiState,
    onDone: () -> Unit,
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit,
    onDepartureSelected: () -> Unit,
    onArrivalSelected: () -> Unit,
    onNowClick: () -> Unit,
    onIn15MinClick: () -> Unit,
    onIn1HourClick: () -> Unit
) {
    val today = remember { Calendar.getInstance() }
    val todayYear = today.get(Calendar.YEAR)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayDay = today.get(Calendar.DAY_OF_MONTH)

    val pagerState = rememberPagerState(initialPage = INITIAL_PAGE) { TOTAL_PAGES }

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Date / time",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "Done",
                fontSize = 17.sp,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onDone() }
            )
        }

        // Calendar month pager
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = 24.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val monthOffset = page - INITIAL_PAGE
            val pageCal = Calendar.getInstance().apply {
                add(Calendar.MONTH, monthOffset)
                set(Calendar.DAY_OF_MONTH, 1)
            }
            val year = pageCal.get(Calendar.YEAR)
            val month = pageCal.get(Calendar.MONTH) + 1
            val monthLabel = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(pageCal.time)

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = monthLabel,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                WeekdayHeaders()
                Spacer(modifier = Modifier.height(4.dp))
                MonthGrid(
                    year = year,
                    month = month,
                    selectedYear = state.selectedYear,
                    selectedMonth = state.selectedMonth,
                    selectedDay = state.selectedDay,
                    todayYear = todayYear,
                    todayMonth = todayMonth,
                    todayDay = todayDay,
                    onDateSelected = onDateSelected
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(12.dp))

        // Departure / Arrival tabs + time chip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DepartureArrivalTab(
                label = "Departure",
                selected = state.isDeparture,
                onClick = onDepartureSelected
            )
            Spacer(modifier = Modifier.width(20.dp))
            DepartureArrivalTab(
                label = "Arrival",
                selected = !state.isDeparture,
                onClick = onArrivalSelected
            )
            Spacer(modifier = Modifier.weight(1f))
            TimeChip(hour = state.selectedHour, minute = state.selectedMinute)
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(150.dp)
                .height(2.dp)
                .background(Color.Red)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Quick time buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickTimeButton(label = "Now", modifier = Modifier.weight(1f), onClick = onNowClick)
            QuickTimeButton(label = "in 15min", modifier = Modifier.weight(1f), onClick = onIn15MinClick)
            QuickTimeButton(label = "in 1h", modifier = Modifier.weight(1f), onClick = onIn1HourClick)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun WeekdayHeaders() {
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun MonthGrid(
    year: Int,
    month: Int,
    selectedYear: Int,
    selectedMonth: Int,
    selectedDay: Int,
    todayYear: Int,
    todayMonth: Int,
    todayDay: Int,
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit
) {
    val firstDayCal = Calendar.getInstance().apply { set(year, month - 1, 1) }
    val daysInMonth = firstDayCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    // Calendar.DAY_OF_WEEK: Sun=1, Mon=2..Sat=7 → Mon-first offset: (dow - 2 + 7) % 7
    val offset = (firstDayCal.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7
    val rows = (offset + daysInMonth + 6) / 7

    Column(modifier = Modifier.fillMaxWidth()) {
        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val day = row * 7 + col - offset + 1
                    val isValid = day in 1..daysInMonth
                    val isSelected = isValid &&
                            day == selectedDay && month == selectedMonth && year == selectedYear
                    val isToday = isValid &&
                            day == todayDay && month == todayMonth && year == todayYear

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isValid) {
                            val bgColor = when {
                                isSelected -> Color.Red
                                else -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(bgColor)
                                    .clickable { onDateSelected(year, month, day) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (isSelected) Color.White else Color.Black,
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DepartureArrivalTab(label: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color.Black else Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun TimeChip(hour: Int, minute: Int) {
    val isAm = hour < 12
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val timeStr = String.format(Locale.ENGLISH, "%d:%02d %s", displayHour, minute, if (isAm) "AM" else "PM")

    Box(
        modifier = Modifier
            .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(text = timeStr, fontSize = 16.sp, color = TextLightColor)
    }
}

@Composable
private fun QuickTimeButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .border(1.dp, Color(0xFFD0D0D0), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 15.sp, color = Color.Black)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, apiLevel = 34)
@Composable
private fun DatePickerContentPreview() {
    DatePickerContent(
        state = DateTimePickerUiState(),
        onDone = {},
        onDateSelected = { _, _, _ -> },
        onDepartureSelected = {},
        onArrivalSelected = {},
        onNowClick = {},
        onIn15MinClick = {},
        onIn1HourClick = {}
    )
}