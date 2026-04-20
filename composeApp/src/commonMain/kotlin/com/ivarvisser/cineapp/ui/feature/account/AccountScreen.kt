package com.ivarvisser.cineapp.ui.feature.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ivarvisser.cineapp.ui.component.AccountSettingItem

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Profile Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "john.doe@example.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Account Settings Section
        Text(
            text = "Account Settings",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        AccountSettingItem(
            icon = Icons.Default.Person,
            title = "Edit Profile",
            subtitle = "Update your personal information"
        )

        AccountSettingItem(
            icon = Icons.Default.Email,
            title = "Email Preferences",
            subtitle = "Manage notification settings"
        )

        AccountSettingItem(
            icon = Icons.Default.Lock,
            title = "Privacy & Security",
            subtitle = "Password and security settings"
        )

        AccountSettingItem(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            subtitle = "Configure app notifications"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // App Settings Section
        Text(
            text = "App Settings",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        AccountSettingItem(
            icon = Icons.Default.Settings,
            title = "General Settings",
            subtitle = "App preferences and configurations"
        )

        AccountSettingItem(
            icon = Icons.Default.Info,
            title = "About",
            subtitle = "Version 1.0.0"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = { /* Handle logout */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }
}

