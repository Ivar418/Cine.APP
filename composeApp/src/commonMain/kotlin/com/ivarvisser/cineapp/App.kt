package com.ivarvisser.cineapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ivarvisser.cineapp.theming.CineAppTheme

@Composable
@Preview
fun App() {
    CineAppTheme(darkTheme = true) {
        CineApp()
    }
}