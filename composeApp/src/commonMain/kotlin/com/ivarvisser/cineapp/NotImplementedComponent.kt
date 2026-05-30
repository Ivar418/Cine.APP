package com.ivarvisser.cineapp

import com.arkivanov.decompose.ComponentContext

class NotImplementedComponent(
    componentContext: ComponentContext,
    private val onRetry: () -> Unit,
    private val textContent: String = "Not implemented yet. Press to go back.",
    private val buttonText: String = "Go back."
) : ComponentContext by componentContext {
    val text = textContent
    val buttonTextValue = buttonText
    fun goBack() {
        onRetry()
    }

}