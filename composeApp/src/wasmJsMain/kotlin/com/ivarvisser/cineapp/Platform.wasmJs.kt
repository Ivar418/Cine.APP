package com.ivarvisser.cineapp

import kotlinx.browser.window
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val isMobile: Boolean = false

    @OptIn(ExperimentalWasmJsInterop::class)
    override fun openFile(bytes: ByteArray, fileName: String) {
        val uint8Array = Uint8Array(bytes.size)
        for (i in bytes.indices) {
            uint8Array[i] = bytes[i]
        }
        val blob = Blob(
            arrayOf<JsAny?>(uint8Array).toJsArray(),
            BlobPropertyBag(type = "application/pdf")
        )
        val url = URL.createObjectURL(blob)
        window.open(url, "_blank")
    }
}

actual fun getPlatform(): Platform = WasmPlatform()
