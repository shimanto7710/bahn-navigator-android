package com.rookie.code.bahnnavigator.feature.searchjourney.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rookie.code.bahnnavigator.core.ui.theme.BahnNavigatorTheme
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.SearchJourneyUiState

@Composable
fun SearchJourneyScreen(
    modifier: Modifier = Modifier,
    uiState: SearchJourneyUiState,
    onFromChange: (String) -> Unit = {},
    onToChange: (String) -> Unit = {},
    onFromClick: () -> Unit = {},
    onToClick: () -> Unit = {},
    onSwapRouteClick: () -> Unit = {},
    onDateClick: () -> Unit = {},
    onPassengersClick: () -> Unit = {},
    onOptionsClick: () -> Unit = {},
    onConnectionTypeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header()
        Spacer(modifier = Modifier.height(24.dp))

        RouteFields(
            from = uiState.from,
            to = uiState.to,
            onFromChange = onFromChange,
            onToChange = onToChange,
            onFromClick = onFromClick,
            onToClick = onToClick,
            onSwapRouteClick = onSwapRouteClick
        )
        Spacer(modifier = Modifier.height(24.dp))

        FilterRows(
            date = uiState.date,
            passengers = uiState.passengers,
            options = uiState.options,
            connectionType = uiState.connectionType,
            onDateClick = onDateClick,
            onPassengersClick = onPassengersClick,
            onOptionsClick = onOptionsClick,
            onConnectionTypeClick = onConnectionTypeClick,
            onSearchClick = onSearchClick
        )
    }
}

@Composable
private fun Header() {
    Column {
        Text(
            text = "Bahn Navigator",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Box(
            modifier = Modifier
                .width(145.dp)
                .height(3.dp)
                .background(Color.Red)
        )
    }
}

@Composable
private fun RouteFields(
    from: String,
    to: String,
    onFromChange: (String) -> Unit,
    onToChange: (String) -> Unit,
    onFromClick: () -> Unit,
    onToClick: () -> Unit,
    onSwapRouteClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            StationTextField(
                value = from,
                onValueChange = onFromChange,
                placeholder = "From",
                onClick = onFromClick
            )
            Spacer(modifier = Modifier.height(12.dp))
            StationTextField(
                value = to,
                onValueChange = onToChange,
                placeholder = "To",
                onClick = onToClick
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-16).dp)
                .size(42.dp)
                .shadow(4.dp, CircleShape)
                .background(Color.White, CircleShape)
                .clickable(onClick = onSwapRouteClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = "Swap route",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun StationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onClick: () -> Unit = {}
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        readOnly = true,
        enabled = false,
        textStyle = TextStyle(color = Color.Black, fontSize = 22.sp),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFFF3F3F7), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.LightGray,
                        fontSize = 22.sp
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
private fun FilterRows(
    date: String,
    passengers: String,
    options: String,
    connectionType: String,
    onDateClick: () -> Unit,
    onPassengersClick: () -> Unit,
    onOptionsClick: () -> Unit,
    onConnectionTypeClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Column {
        FilterRow(Icons.Default.CalendarMonth, "Date", date, onDateClick)
        FilterDivider()
        FilterRow(Icons.Default.PersonOutline, "Passengers, bicycles", passengers, onPassengersClick)
        FilterDivider()
        FilterRow(Icons.Default.Tune, "Options", options, onOptionsClick)
        FilterDivider()
        FilterRow(
            Icons.AutoMirrored.Filled.DirectionsWalk,
            "Type Of Connection",
            connectionType,
            onConnectionTypeClick
        )
        Spacer(modifier = Modifier.height(32.dp))
        SearchButton(onClick = onSearchClick)
    }
}

@Composable
private fun FilterDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 42.dp),
        color = Color(0xFFE0E0E0),
        thickness = 1.dp
    )
}

@Composable
private fun FilterRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.size(42.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.Black,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(text = title, fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Box(modifier = Modifier.size(42.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Open $title",
                tint = Color.Black,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun SearchButton(onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF24262B),
            contentColor = Color.White
        )
    ) {
        Text(text = "Search", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun SearchJourneyScreenPreview() {
    BahnNavigatorTheme {
        SearchJourneyScreen(uiState = SearchJourneyUiState())
    }
}
