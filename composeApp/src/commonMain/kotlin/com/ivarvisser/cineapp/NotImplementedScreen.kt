package com.ivarvisser.cineapp

import androidx.compose.runtime.Composable
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotImplemented(
    component: NotImplementedComponent,
) {

    ErrorMessage(
        message = stringResource(component.textRes),
        onRetry = { component.goBack() },
        buttonText = stringResource(component.buttonTextRes)
    )
}