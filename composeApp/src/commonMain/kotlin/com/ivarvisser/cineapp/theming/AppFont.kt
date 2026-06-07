package com.ivarvisser.cineapp.theming

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.noto_color_emoji_regular
import cineapp.composeapp.generated.resources.roboto_bold
import cineapp.composeapp.generated.resources.roboto_regular
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.InternalResourceApi


@Composable
@OptIn(InternalResourceApi::class)
private fun robotoFont(): FontFamily {
    val emoji = Font(Res.font.noto_color_emoji_regular)
    return FontFamily(
        Font(Res.font.roboto_regular, FontWeight.Normal),
        Font(Res.font.roboto_bold, FontWeight.Bold), emoji
    )

}

@Composable
fun robotoypography() = Typography().run {

    val fontFamily = robotoFont()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily),
    )
}