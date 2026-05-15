package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.OptionsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsSheet(
    state: OptionsUiState,
    onDismiss: () -> Unit,
    onDone: () -> Unit,
    onModeOfTransportClick: () -> Unit,
    onDTicketToggle: (Boolean) -> Unit,
    onBicycleToggle: (Boolean) -> Unit,
    onDirectServicesToggle: (Boolean) -> Unit,
    onReset: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null,
        modifier = Modifier.fillMaxSize()
    ) {
        OptionsContent(
            state = state,
            onDone = onDone,
            onModeOfTransportClick = onModeOfTransportClick,
            onDTicketToggle = onDTicketToggle,
            onBicycleToggle = onBicycleToggle,
            onDirectServicesToggle = onDirectServicesToggle,
            onReset = onReset
        )
    }
}

@Composable
private fun OptionsContent(
    state: OptionsUiState,
    onDone: () -> Unit,
    onModeOfTransportClick: () -> Unit,
    onDTicketToggle: (Boolean) -> Unit,
    onBicycleToggle: (Boolean) -> Unit,
    onDirectServicesToggle: (Boolean) -> Unit,
    onReset: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Options",
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

        // White options section
        Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
            OptionRowChevron(
                label = "Mode of transport",
                value = state.modeOfTransport.selectedMode,
                onClick = onModeOfTransportClick
            )
            HorizontalDivider(color = Color(0xFFE0E0E0))
            OptionRowToggle(
                label = "D-Ticket services only",
                checked = state.dTicketOnly,
                onToggle = onDTicketToggle
            )
            HorizontalDivider(color = Color(0xFFE0E0E0))
            OptionRowToggle(
                label = "Bicycle transport possible",
                checked = state.bicycleTransport,
                onToggle = onBicycleToggle
            )
            HorizontalDivider(color = Color(0xFFE0E0E0))
            OptionRowToggle(
                label = "Direct services only",
                checked = state.directServicesOnly,
                onToggle = onDirectServicesToggle
            )
            HorizontalDivider(color = Color(0xFFE0E0E0))
            OptionRowChevron(label = "Transfer time", value = state.transferTime)
            HorizontalDivider(color = Color(0xFFE0E0E0))
            OptionRowChevron(label = "Stopovers", value = state.stopovers)
        }

        // Gray area with Reset button
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEEEFF1))
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(1.dp, Color(0xFFD0D0D0), RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .clickable { onReset() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Reset", fontSize = 16.sp, color = Color.Black)
            }
        }
    }
}

@Composable
private fun OptionRowChevron(label: String, value: String, onClick: () -> Unit = {}) {
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
        Text(text = value, fontSize = 15.sp, color = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun OptionRowToggle(label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))
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
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, apiLevel = 34)
@Composable
private fun OptionsContentPreview() {
    OptionsContent(
        state = OptionsUiState(),
        onDone = {},
        onModeOfTransportClick = {},
        onDTicketToggle = {},
        onBicycleToggle = {},
        onDirectServicesToggle = {},
        onReset = {}
    )
}
