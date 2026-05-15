package com.rookie.code.bahnnavigator.feature.searchjourney.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import com.rookie.code.bahnnavigator.feature.searchjourney.presentation.state.ConnectionTypeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionTypeSheet(
    state: ConnectionTypeUiState,
    onDismiss: () -> Unit,
    onDone: () -> Unit,
    onSelect: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null
    ) {
        ConnectionTypeContent(state = state, onDone = onDone, onSelect = onSelect)
    }
}

@Composable
private fun ConnectionTypeContent(
    state: ConnectionTypeUiState,
    onDone: () -> Unit,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Type of Connection",
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

        ConnectionTypeUiState.OPTIONS.forEach { option ->
            ConnectionOptionRow(
                label = option,
                selected = state.selected == option,
                onClick = { onSelect(option) }
            )
            HorizontalDivider(color = Color(0xFFE0E0E0))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ConnectionOptionRow(label: String, selected: Boolean, onClick: () -> Unit) {
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

@Preview(showBackground = true, widthDp = 390, heightDp = 400, apiLevel = 34)
@Composable
private fun ConnectionTypeContentPreview() {
    ConnectionTypeContent(
        state = ConnectionTypeUiState(),
        onDone = {},
        onSelect = {}
    )
}
