package com.ae.mylibrary.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.ae.mylibrary.theme.AEHealthTheme

@Composable
fun DataItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    link: String? = null,
    imageUri: String?,
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AEHealthTheme.dimens.defaultSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = imageUri,
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth(0.1f),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )

        TitleSubtitleText(
            title = title,
            subtitle = subtitle
        )

    }

}

