package com.ae.mylibrary.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ae.mylibrary.theme.AEHealthTheme

@Composable
fun TitleSubtitleText(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
) {

    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.Companion.Start,
        verticalArrangement = Arrangement.spacedBy(AEHealthTheme.dimens.verticalTextSpacing)
    ) {

        AppText(text = title, style = AEHealthTheme.typography.tileTitle)

        subtitle?.let { text ->
            AppText(text = text, style = AEHealthTheme.typography.tileTitle)
        }

    }

}