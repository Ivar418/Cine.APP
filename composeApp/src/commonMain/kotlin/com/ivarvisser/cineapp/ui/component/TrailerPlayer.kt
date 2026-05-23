package com.ivarvisser.cineapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import net.codinux.log.Log

@Composable
fun TrailerPlayer(
    videoId: String,
    modifier: Modifier = Modifier
) {

    val html = """
        <html>
        <body style="margin:0">
    <iframe width="560" height="315" src="https://www.youtube.com/embed/$videoId"
     title="YouTube video player" frameborder="0" allow="accelerometer; 
     autoplay; 
     clipboard-write; 
     encrypted-media; 
     gyroscope; 
     picture-in-picture; 
     web-share" 
     referrerpolicy="strict-origin-when-cross-origin" 
     allowfullscreen></iframe>
            </body>
            </html>
""".trimIndent()
    Log.debug(loggerName = "TrailerPlayer") { "Video html: $html" }
    val webViewState = rememberWebViewStateWithHTMLData(html)

    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        androidWebSettings.apply {
            domStorageEnabled = true
            supportZoom = false
        }
    }

    WebView(
        state = webViewState,
        modifier = modifier
            .fillMaxWidth().height(400.dp)
            .focusProperties { canFocus = false })
}