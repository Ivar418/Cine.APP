package com.ivarvisser.cineapp

import androidx.compose.runtime.Composable
import com.ivarvisser.cineapp.ui.component.ErrorMessage

@Composable
fun NotImplemented(
    component: NotImplementedComponent,
) {

    ErrorMessage(
        message = component.text,
        onRetry = { component.goBack() },
        buttonText = component.buttonTextValue
    )
}