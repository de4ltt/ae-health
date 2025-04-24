package com.ae.home.component

import android.service.autofill.OnClickAction
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.ae.home.model.SearchItemCategoryUI
import com.ae.home.viewmodel.event.HomeUIEvent
import com.ae.mylibrary.theme.AEHealthTheme

@Composable
fun SmallFilterTile(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .wrapContentHeight()
            .background(
                color = AEHealthTheme.colorScheme.primary,
                shape = AEHealthTheme.shapes.rounded4
            )
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            modifier = Modifier.padding(AEHealthTheme.dimens.containerTextPadding),
            text = text,
            style = TextStyle(
                color = AEHealthTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        )
    }

}