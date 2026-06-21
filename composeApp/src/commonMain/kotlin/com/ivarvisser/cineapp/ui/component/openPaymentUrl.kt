package com.ivarvisser.cineapp.ui.component

import androidx.compose.ui.platform.UriHandler

fun openPaymentUrl(uriHandler: UriHandler, url: String) {
    uriHandler.openUri(url)
}