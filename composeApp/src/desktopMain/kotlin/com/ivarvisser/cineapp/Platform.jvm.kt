package com.ivarvisser.cineapp

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val isMobile: Boolean = false
    override fun openFile(bytes: ByteArray, fileName: String) {
        val tempDir = System.getProperty("java.io.tmpdir")
        val file = java.io.File(tempDir, fileName)
        file.writeBytes(bytes)
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop.getDesktop().open(file)
        }
    }

}

actual fun getPlatform(): Platform = JVMPlatform()