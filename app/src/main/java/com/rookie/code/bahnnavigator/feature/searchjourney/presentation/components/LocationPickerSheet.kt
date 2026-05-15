package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Signpost
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto.SearchStationModelElement
import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto.SearchStationModelType
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.LocationPickerUiState

private val AppDark = Color(0xFF26292E)
private val TextLightColor = Color(0xFF2E3036)
private val FieldGrey = Color(0xFFF2F2F7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerSheet(
    state: LocationPickerUiState,
    onDismiss: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSelect: (SearchStationModelElement) -> Unit,
    onToggleFavorite: (SearchStationModelElement) -> Unit,
    onCurrentPositionClick: () -> Unit,
    onNearbyStopsClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null
    ) {
        LocationPickerContent(
            state = state,
            focusRequester = focusRequester,
            onDismiss = onDismiss,
            onSearchChange = onSearchChange,
            onSelect = onSelect,
            onToggleFavorite = onToggleFavorite,
            onCurrentPositionClick = onCurrentPositionClick,
            onNearbyStopsClick = onNearbyStopsClick
        )
    }
}

@Composable
private fun LocationPickerContent(
    state: LocationPickerUiState,
    focusRequester: FocusRequester,
    onDismiss: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSelect: (SearchStationModelElement) -> Unit,
    onToggleFavorite: (SearchStationModelElement) -> Unit,
    onCurrentPositionClick: () -> Unit,
    onNearbyStopsClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Header(title = state.title, onCancel = onDismiss)

        SearchField(
            value = state.searchText,
            onValueChange = onSearchChange,
            modifier = Modifier.focusRequester(focusRequester)
        )

        Spacer(Modifier.height(8.dp))

        OptionRow(
            icon = Icons.Default.MyLocation,
            title = "Current position",
            onClick = onCurrentPositionClick
        )
//        HorizontalDivider()
        FilterDivider()
        OptionRow(
            icon = Icons.Default.NearMe,
            title = "Stops nearby",
            onClick = onNearbyStopsClick
        )
//        HorizontalDivider()
        FilterDivider()
        if (state.isLoading) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage?.let { msg ->
            MessageRow(Icons.Default.WifiOff, msg, Color.Red)
        }

        Spacer(Modifier.height(6.dp))

        val nonFavResults = state.results.filterNot { state.favoriteIds.contains(it.displayId) }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {

            // Favorites — always visible
            if (state.favorites.isNotEmpty()) {
                items(state.favorites, key = { "fav-${it.displayId}" }) { fav ->
                    LocationRow(
                        location = fav,
                        isFavorite = true,
                        onSelect = { onSelect(fav) },
                        onToggleFavorite = { onToggleFavorite(fav) }
                    )
                }
                item { FilterDivider() }
            }

            // API results
            if (nonFavResults.isEmpty() && state.favorites.isEmpty()
                && !state.isLoading && state.errorMessage == null
            ) {
                item {
                    MessageRow(Icons.Default.Search, "No locations found", Color.Gray)
                }
            } else {
                itemsIndexed(
                    nonFavResults,
                    key = { index, loc -> "res-${loc.displayId}-$index" }
                ) { _, loc ->
                    LocationRow(
                        location = loc,
                        isFavorite = false,
                        onSelect = { onSelect(loc) },
                        onToggleFavorite = { onToggleFavorite(loc) }
                    )
                    FilterDivider()
                }
            }
        }
    }
}

@Composable
private fun Header(title: String, onCancel: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Cancel",
            fontSize = 18.sp,
            color = Color.DarkGray,
            modifier = Modifier.clickable(onClick = onCancel)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AppDark
        )
    }
}

@Composable
private fun FilterDivider(padding:Int = 0) {
    HorizontalDivider(
        modifier = Modifier.padding(start = padding.dp),
        color = Color(0xFFE0E0E0),
        thickness = 1.dp
    )
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(52.dp)
            .background(FieldGrey)
            .drawBehind {
                val stroke = 1.dp.toPx()
                drawLine(
                    color = AppDark,
                    start = Offset(0f, size.height - stroke / 2),
                    end = Offset(size.width, size.height - stroke / 2),
                    strokeWidth = stroke
                )
            }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = AppDark,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Box(Modifier.fillMaxWidth()) {
            if (value.isEmpty()) {
                Text(
                    text = "Station, search address",
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Blue),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun OptionRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextLightColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, color = TextLightColor)
    }
}

@Composable
private fun MessageRow(icon: ImageVector, message: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color)
        Text(text = message, color = color, fontSize = 15.sp)
    }
}

@Composable
private fun LocationRow(
    location: SearchStationModelElement,
    isFavorite: Boolean,
    onSelect: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onSelect)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconFor(location),
                contentDescription = null,
                tint = TextLightColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = location.name,
                    fontSize = 17.sp,
                    color = TextLightColor,
//                    fontWeight = FontWeight.SemiBold
                )
                val subtitle = location.address ?: location.station?.name
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = TextLightColor
                    )
                }
            }
        }
        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                contentDescription = if (isFavorite) "Remove favorite" else "Add favorite",
                tint = if (isFavorite) Color(0xFFFFA000) else Color.DarkGray
            )
        }
    }
}

private fun iconFor(location: SearchStationModelElement): ImageVector =
    when (location.type) {
        SearchStationModelType.LOCATION -> Icons.Default.Signpost
        SearchStationModelType.STATION,
        SearchStationModelType.STOP -> Icons.Default.Train
    }

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LocationPickerSheetPreview() {
    val sample = { id: String, name: String, type: SearchStationModelType, address: String? ->
        SearchStationModelElement(id = id, name = name, type = type, address = address)
    }
    LocationPickerContent(
        state = LocationPickerUiState(),
        focusRequester = FocusRequester(),
        onDismiss = {},
        onSearchChange = {},
        onSelect = {},
        onToggleFavorite = {},
        onCurrentPositionClick = {},
        onNearbyStopsClick = {}
    )
}
