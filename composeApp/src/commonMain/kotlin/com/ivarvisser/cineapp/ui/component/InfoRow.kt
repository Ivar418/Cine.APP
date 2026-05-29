package com.ivarvisser.cineapp.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.Bold
        )

        Text(text = value)
    }
}