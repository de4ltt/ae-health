package com.ae.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.res.stringResource

val LocalColorScheme = compositionLocalOf { AEHealthColorScheme.lightColorScheme }
val LocalShapes = compositionLocalOf { AEHealthShapes }
val LocalTypography = compositionLocalOf { AEHealthTypography() }

@Composable
fun AEHealthTheme(
    colorScheme: AEHealthColorScheme,
    shapes: AEHealthShapes ,
    typography: AEHealthTypography,
    content: @Composable () -> Unit
) {
    
    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalShapes provides shapes,
        content = content
        //LocalTypography provides typography,
    ) /*{
        ProvideTextStyle(value = typography.bodyLarge, content = content)
    }*/
}
