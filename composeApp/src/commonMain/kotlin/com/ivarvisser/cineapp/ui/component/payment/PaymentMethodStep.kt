package com.ivarvisser.cineapp.ui.component.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.account_balance
import cineapp.composeapp.generated.resources.card_giftcard
import cineapp.composeapp.generated.resources.language
import cineapp.composeapp.generated.resources.schedule
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingAction
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingUiState
import org.jetbrains.compose.resources.painterResource

@Composable
fun PaymentMethodStep(
    state: OrderingUiState, onAction: (OrderingAction) -> Unit
) {
    Column {
        Text(text = "Choose Payment Method", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().height(
                IntrinsicSize.Min
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            state.paymentMethods.forEach {
                Card(
                    onClick = { onAction(OrderingAction.PaymentMethodSelected(it)) },
                    modifier = Modifier.weight(1f).fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(6.dp).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val icon = when (it) {
                            "iDeal" -> Res.drawable.account_balance
                            "Credit Card Online" -> Res.drawable.language
                            "Reserveren" -> Res.drawable.schedule
                            "Cadeaubon" -> Res.drawable.card_giftcard
                            else -> Res.drawable.account_balance
                        }

                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(it)
                    }

                }
            }
        }
    }
}