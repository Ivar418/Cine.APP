package com.ivarvisser.cineapp

import com.arkivanov.decompose.ComponentContext
import org.jetbrains.compose.resources.StringResource

class NotImplementedComponent(
    componentContext: ComponentContext,
    private val onRetry: () -> Unit,
    val textRes: StringResource,
    val buttonTextRes: StringResource
) : ComponentContext by componentContext {
    fun goBack() {
        onRetry()
    }
}