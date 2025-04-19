package com.ae.mylibrary.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

data class AEHealthColorScheme(
    val primary: Color,
    val secondary: Color,
    val onPrimary: Color,
    val background: Color,
    val onBackground: Color,
    val onBackgroundContainer: Color
) {

    companion object {

        @Stable
        val lightColorScheme
            get() = AEHealthColorScheme(
                primary = Blue,
                secondary = DizzyBlue,
                onPrimary = White,
                background = White,
                onBackground = Black,
                onBackgroundContainer = LightGray
            )

        @Stable
        val Green
            get() = com.ae.mylibrary.theme.Green

        @Stable
        val Orange
            get() = com.ae.mylibrary.theme.Orange

        @Stable
        val Red
            get() = com.ae.mylibrary.theme.Red
    }

}