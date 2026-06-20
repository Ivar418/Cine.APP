package com.ivarvisser.cineapp.ui.feature.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.account_settings_title
import cineapp.composeapp.generated.resources.back_button
import cineapp.composeapp.generated.resources.edit_profile
import cineapp.composeapp.generated.resources.edit_profile_desc
import cineapp.composeapp.generated.resources.edit_profile_not_implemented
import cineapp.composeapp.generated.resources.location_notifications
import cineapp.composeapp.generated.resources.location_notifications_desc
import cineapp.composeapp.generated.resources.logout_button
import cineapp.composeapp.generated.resources.notifications_settings
import cineapp.composeapp.generated.resources.profile_email_placeholder
import cineapp.composeapp.generated.resources.profile_name_placeholder
import cineapp.composeapp.generated.resources.profile_picture_desc
import cineapp.composeapp.generated.resources.show_time_notifications
import cineapp.composeapp.generated.resources.show_time_notifications_desc
import cineapp.composeapp.generated.resources.unknown_error
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.ui.component.AccountSettingItem
import com.ivarvisser.cineapp.ui.component.AccountSwitchSettingItem
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier, component: AccountComponent
) {

    val state by component.state.subscribeAsState()
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        if (!state.isLoggedIn) {
            if (state.isRegistering) {
                RegisterScreen(
                    onRegisterClick = { username, firstName, lastName, email, password ->
                        component.register(username, firstName, lastName, email, password)
                    },
                    onBackClick = { component.setRegistering(false) }
                )
            } else {
                LoginScreen(
                    onLoginClick = { username, password -> component.login(username, password) },
                    onRegisterClick = { component.setRegistering(true) })
            }
        } else if (state.hasError) {
            ErrorMessage(
                message = state.error ?: stringResource(Res.string.unknown_error),
                onRetry = { component.clearError() },
                buttonText = stringResource(Res.string.back_button),
                isOverlay = true
            )
        } else {

            // Profile Section
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.user?.photoUrl != null) {
                        AsyncImage(
                            model = state.user!!.photoUrl,
                            contentDescription = stringResource(Res.string.profile_picture_desc),
                            modifier = Modifier.size(80.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = stringResource(Res.string.profile_picture_desc),
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.user?.let { "${it.firstName} ${it.lastName}" }
                            ?: stringResource(Res.string.profile_name_placeholder),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = state.user?.email
                            ?: stringResource(Res.string.profile_email_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Account Settings Section
            Text(
                text = stringResource(Res.string.account_settings_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            val editProfileNotImplemented = stringResource(Res.string.edit_profile_not_implemented)

            AccountSettingItem(
                icon = Icons.Default.Person,
                title = stringResource(Res.string.edit_profile),
                onclick = {
                    component.setError(editProfileNotImplemented)
                },
                subtitle = stringResource(Res.string.edit_profile_desc)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notifications Section
            Text(
                text = stringResource(Res.string.notifications_settings),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            AccountSwitchSettingItem(
                icon = Icons.Default.Notifications,
                title = stringResource(Res.string.location_notifications),
                subtitle = stringResource(Res.string.location_notifications_desc),
                checked = state.locationNotificationsEnabled,
                onCheckedChange = { component.onEvent(AccountAction.OnToggleLocationNotifications(it)) }
            )

            AccountSwitchSettingItem(
                icon = Icons.Default.Notifications,
                title = stringResource(Res.string.show_time_notifications),
                subtitle = stringResource(Res.string.show_time_notifications_desc),
                checked = state.showTimeNotificationsEnabled,
                onCheckedChange = { component.onEvent(AccountAction.OnToggleShowTimeNotifications(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(
                onClick = { component.logout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(Res.string.logout_button))
            }
        }
    }
}
