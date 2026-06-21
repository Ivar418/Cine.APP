package com.ivarvisser.cineapp.ui.component.ticketselection

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.ticket_adult
import cineapp.composeapp.generated.resources.ticket_child
import cineapp.composeapp.generated.resources.ticket_senior
import cineapp.composeapp.generated.resources.ticket_student
import cineapp.composeapp.generated.resources.ticket_type_select
import org.jetbrains.compose.resources.stringResource

@Composable
fun TicketTypeDropdown(
    selected: String?,
    onSelected: (String) -> Unit
) {
    val items = listOf(
        stringResource(Res.string.ticket_adult),
        stringResource(Res.string.ticket_student),
        stringResource(Res.string.ticket_child),
        stringResource(Res.string.ticket_senior)
    )
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(
                selected ?: stringResource(Res.string.ticket_type_select)
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        expanded = false
                        onSelected(it)
                    }
                )
            }
        }
    }
}
