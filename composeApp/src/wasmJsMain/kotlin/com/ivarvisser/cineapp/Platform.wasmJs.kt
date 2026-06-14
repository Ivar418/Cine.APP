package com.ivarvisser.cineapp

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val isMobile: Boolean = false
    override fun openFile(bytes: ByteArray, fileName: String) {
        val uint8Array = org.khronos.webgl.Uint8Array(bytes.size)
        for (i in bytes.indices) {
            uint8Array[i] = bytes[i]
        }
        val blob = org.w3c.files.Blob(
            arrayOf(uint8Array),
            org.w3c.files.BlobPropertyBag(type = "application/pdf")
        )
        val url = org.w3c.dom.url.URL.createObjectURL(blob)
        kotlinx.browser.window.open(url, "_blank")
    }
}

actual fun getPlatform(): Platform = WasmPlatform()