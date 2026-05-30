package com.ivarvisser.cineapp

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
    override val isMobile: Boolean = false
}

actual fun getPlatform(): Platform = JsPlatform()