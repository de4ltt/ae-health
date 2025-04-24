package com.ae.mylibrary.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ae.ui.R

private val rubikFontFamily = FontFamily(
    Font(R.font.rubik_regular, weight = FontWeight.Normal),
    Font(R.font.rubik_bold, weight = FontWeight.Bold),
    Font(R.font.rubik_black, weight = FontWeight.Black),
    Font(R.font.rubik_light, weight = FontWeight.Light),
    Font(R.font.rubik_medium, weight = FontWeight.Medium),
    Font(R.font.rubik_semi_bold, weight = FontWeight.SemiBold),
)

object AEHealthTypography {

    val tileTitle: TextStyle
        @Composable get() = TextStyle(
            color = AEHealthTheme.colorScheme.onBackground,
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 27.sp,
        )

    val tileSubtitle: TextStyle
        @Composable get() = TextStyle(
            color = AEHealthTheme.colorScheme.onBackgroundContainer,
            fontFamily = rubikFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 23.sp,
        )

}