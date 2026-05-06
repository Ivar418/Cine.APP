package com.ivarvisser.cineapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.account
import cineapp.composeapp.generated.resources.app_name
import cineapp.composeapp.generated.resources.movies_overview
import cineapp.composeapp.generated.resources.order_history
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.ui.startScreen.CineAppComponent
import com.ivarvisser.cineapp.ui.startScreen.CineAppScreenEvent
import org.jetbrains.compose.resources.StringResource

enum class CineScreen(val title: StringResource) {
    Start(title = Res.string.app_name),
    Account(title = Res.string.account),
    History(title = Res.string.order_history),
    Overview(title = Res.string.movies_overview)
}


@Composable
fun CineappScreen(
    component: CineAppComponent,
    modifier: Modifier = Modifier
) {
    val state = component.startScreenModel.subscribeAsState()
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // App Title
        Text(
            text = "🎬 CineApp",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Your Ultimate Cinema Experience",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Featured Card
        ElevatedCard(
            onClick = {
                component.onEvent(CineAppScreenEvent.OnOverviewClick)
            },
            modifier = Modifier.fillMaxWidth().height(200.dp), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🍿", style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Now Showing",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Explore the latest movies",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Actions
        Card(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Button(
                    onClick = { component.onEvent(CineAppScreenEvent.OnAccountClick) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("My Account")
                }

                Button(
                    onClick = { component.onEvent(CineAppScreenEvent.OnHistoryClick) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Order History")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer
        Text(
            text = "Welcome! Book your tickets now",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
