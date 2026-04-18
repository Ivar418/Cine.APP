package com.ivarvisser.cineapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform