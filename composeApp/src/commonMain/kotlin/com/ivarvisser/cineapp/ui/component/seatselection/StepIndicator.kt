package com.ivarvisser.cineapp.ui.component.seatselection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StepIndicator(
    currentStep: Int
) {
    val labels = listOf("Seats", "Tickets", "Overview", "Payment", "Confirmation")
    if (currentStep != 6)
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        labels.forEachIndexed { index, label ->
            FilterChip(
                selected = currentStep == index + 1,
                onClick = {},
                label = { Text(label) }
            )
        }
    }
}