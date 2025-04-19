package com.ae.mylibrary.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

val LocalColorScheme = compositionLocalOf { AEHealthColorScheme.lightColorScheme }
val LocalShapes = compositionLocalOf { AEHealthShapes }
val LocalTypography = compositionLocalOf { AEHealthTypography() }
val LocalDimens = compositionLocalOf { AEHealthDimens }

@Composable
private fun AEHealthTheme(
    colorScheme: AEHealthColorScheme,
    shapes: AEHealthShapes,
    dimens: AEHealthDimens,
    typography: AEHealthTypography,
    content: @Composable () -> Unit
) {

    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalShapes provides shapes,
        LocalDimens provides dimens,
        content = content
        //LocalTypography provides typography,
    ) /*{
        ProvideTextStyle(value = typography.bodyLarge, content = content)
    }*/
}

object AEHealthTheme {

    val colorScheme: AEHealthColorScheme
        @Composable @ReadOnlyComposable get() = LocalColorScheme.current

    val typography: AEHealthTypography
        @Composable @ReadOnlyComposable get() = LocalTypography.current

    val shapes: AEHealthShapes
        @Composable @ReadOnlyComposable get() = LocalShapes.current

    val dimens: AEHealthDimens
            @Composable @ReadOnlyComposable get() = LocalDimens.current

}
