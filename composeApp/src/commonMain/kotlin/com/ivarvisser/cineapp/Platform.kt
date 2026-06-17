package com.ivarvisser.cineapp

interface Platform {
    val name: String
    val isMobile: Boolean
    val isAndroid: Boolean
    fun openFile(bytes: ByteArray, fileName: String)
}

expect fun getPlatform(): Platform