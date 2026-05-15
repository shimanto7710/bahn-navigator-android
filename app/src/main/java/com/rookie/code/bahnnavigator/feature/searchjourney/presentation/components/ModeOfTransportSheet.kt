package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.DirectionsRailway
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Tram
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.ModeOfTransportUiState

private val MODES = listOf("All", "Local/regional transport only", "Long-distance travel only")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeOfTransportSheet(
    state: ModeOfTransportUiState,
    onBack: () -> Unit,
    onDone: () -> Unit,
    onModeSelected: (String) -> Unit,
    onHighSpeedToggle: (Boolean) -> Unit,
    onIntercityToggle: (Boolean) -> Unit,
    onInterregioToggle: (Boolean) -> Unit,
    onRegionalToggle: (Boolean) -> Unit,
    onSBahnToggle: (Boolean) -> Unit,
    onBussesToggle: (Boolean) -> Unit,
    onBoatsToggle: (Boolean) -> Unit,
    onUndergroundToggle: (Boolean) -> Unit,
    onTramToggle: (Boolean) -> Unit,
    onTelServicesToggle: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onBack,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null,
        modifier = Modifier.fillMaxSize()
    ) {
        ModeOfTransportContent(
            state = state,
            onBack = onBack,
            onDone = onDone,
            onModeSelected = onModeSelected,
            onHighSpeedToggle = onHighSpeedToggle,
            onIntercityToggle = onIntercityToggle,
            onInterregioToggle = onInterregioToggle,
            onRegionalToggle = onRegionalToggle,
            onSBahnToggle = onSBahnToggle,
            onBussesToggle = onBussesToggle,
            onBoatsToggle = onBoatsToggle,
            onUndergroundToggle = onUndergroundToggle,
            onTramToggle = onTramToggle,
            onTelServicesToggle = onTelServicesToggle
        )
    }
}

@Composable
private fun ModeOfTransportContent(
    state: ModeOfTransportUiState,
    onBack: () -> Unit,
    onDone: () -> Unit,
    onModeSelected: (String) -> Unit,
    onHighSpeedToggle: (Boolean) -> Unit,
    onIntercityToggle: (Boolean) -> Unit,
    onInterregioToggle: (Boolean) -> Unit,
    onRegionalToggle: (Boolean) -> Unit,
    onSBahnToggle: (Boolean) -> Unit,
    onBussesToggle: (Boolean) -> Unit,
    onBoatsToggle: (Boolean) -> Unit,
    onUndergroundToggle: (Boolean) -> Unit,
    onTramToggle: (Boolean) -> Unit,
    onTelServicesToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { onBack() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Options", fontSize = 17.sp, color = Color.Gray)
            }
            Text(
                text = "Mode of transport",
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

        HorizontalDivider(color = Color(0xFFE0E0E0))

        // Mode radio rows
        MODES.forEach { mode ->
            ModeRadioRow(
                label = mode,
                selected = state.selectedMode == mode,
                onClick = { onModeSelected(mode) }
            )
            HorizontalDivider(color = Color(0xFFE0E0E0))
        }

        // Section separator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFFEEEFF1))
        )

        // Transport type toggle rows
        TransportToggleRow(
            bgColor = Color(0xFF1C1C1E),
            icon = Icons.Default.DirectionsRailway,
            label = "High Speed Trains",
            checked = state.highSpeedTrains,
            onToggle = onHighSpeedToggle
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
        TransportToggleRow(
            bgColor = Color(0xFF48484A),
            icon = Icons.Default.DirectionsRailway,
            label = "Intercity- and Eurocity trains",
            checked = state.intercityTrains,
            onToggle = onIntercityToggle
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
        TransportToggleRow(
            bgColor = Color(0xFF636366),
            icon = Icons.Default.DirectionsRailway,
            label = "Interregio- and Fast trains",
            checked = state.interregioTrains,
            onToggle = onInterregioToggle
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
        TransportToggleRow(
            bgColor = Color(0xFF636366),
            icon = Icons.Default.DirectionsRailway,
            label = "Regional and other trains",
            checked = state.regionalTrains,
            onToggle = onRegionalToggle
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
        TransportToggleRowText(
            bgColor = Color(0xFF248A3D),
            iconText = "S",
            label = "S-Bahn",
            checked = state.sBahn,
            onToggle = onSBahnToggle
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
        TransportToggleRow(
            bgColor = Color(0xFF6F36AD),
            icon = Icons.Default.DirectionsBus,
            label = "Busses",
            checked = state.busses,
            onToggle = onBussesToggle
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
        TransportToggleRow(
            bgColor = Color(0xFF32ADE6),
            icon = Icons.Default.DirectionsBoat,
            label = "Boats",
            checked = state.boats,
            onToggle = onBoatsToggle
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
        TransportToggleRowText(
            bgColor = Color(0xFF0A6DC2),
            iconText = "U",
            label = "Underground",
            checked = state.underground,
            onToggle = onUndergroundToggle
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
        TransportToggleRow(
            bgColor = Color(0xFF8A2C2C),
            icon = Icons.Default.Tram,
            label = "Tram",
            checked = state.tram,
            onToggle = onTramToggle
        )
        HorizontalDivider(color = Color(0xFFE0E0E0))
        TransportToggleRow(
            bgColor = Color(0xFFFF9F0A),
            icon = Icons.Default.LocalTaxi,
            label = "Services requiring tel. registration",
            checked = state.telServices,
            onToggle = onTelServicesToggle
        )

        // Bottom gray fill
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(Color(0xFFEEEFF1))
        )
    }
}

@Composable
private fun ModeRadioRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF34C759),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun TransportToggleRow(
    bgColor: Color,
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, fontSize = 15.sp, color = Color.Black, modifier = Modifier.weight(1f))
        TransportSwitch(checked = checked, onToggle = onToggle)
    }
}

@Composable
private fun TransportToggleRowText(
    bgColor: Color,
    iconText: String,
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconText,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, fontSize = 15.sp, color = Color.Black, modifier = Modifier.weight(1f))
        TransportSwitch(checked = checked, onToggle = onToggle)
    }
}

@Composable
private fun TransportSwitch(checked: Boolean, onToggle: (Boolean) -> Unit) {
    Switch(
        checked = checked,
        onCheckedChange = onToggle,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = Color(0xFF34C759),
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = Color(0xFFE0E0E0),
            uncheckedBorderColor = Color(0xFFE0E0E0)
        )
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, apiLevel = 34)
@Composable
private fun ModeOfTransportContentPreview() {
    ModeOfTransportContent(
        state = ModeOfTransportUiState(),
        onBack = {}, onDone = {}, onModeSelected = {},
        onHighSpeedToggle = {}, onIntercityToggle = {}, onInterregioToggle = {},
        onRegionalToggle = {}, onSBahnToggle = {}, onBussesToggle = {},
        onBoatsToggle = {}, onUndergroundToggle = {}, onTramToggle = {},
        onTelServicesToggle = {}
    )
}
