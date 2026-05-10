package com.ivarvisser.cineapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import com.ivarvisser.cineapp.di.initKoin
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        initKoin {
            androidContext(this@MainActivity)
            androidLogger()
        }
        val root = retainedComponent { RootComponent(it) }

        setContent {
            App(
                root = root
            )
        }
    }
}

