package com.ivarvisser.cineapp

interface Platform {
    val name: String
    val isMobile: Boolean
    fun openFile(bytes: ByteArray, fileName: String)
}

expect fun getPlatform(): Platform