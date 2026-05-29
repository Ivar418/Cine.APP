package com.ivarvisser.cineapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val root = retainedComponent { RootComponent(it) }

        setContent {
            App(
                root = root
            )
        }
    }
}

