package com.ae.mylibrary.snackbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ae.mylibrary.theme.AEHealthTheme
import kotlinx.coroutines.delay

@Composable
internal fun <T : SnackbarMessage> AppSnackbar(
    message: T,
    modifier: Modifier = Modifier,
    durationMillis: Long = 1000
) {

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(durationMillis / 4)
        visible = true
        delay(durationMillis / 2)
        visible = false
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Box(
            modifier = modifier
                .wrapContentSize()
                .padding(AEHealthTheme.dimens.defaultEdgePadding)
                .background(
                    color = AEHealthTheme.colorScheme.onBackgroundContainer,
                    shape = AEHealthTheme.shapes.rounded12
                )
                .border(
                    width = AEHealthTheme.dimens.snackbarBorderWidth,
                    color = AEHealthTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    shape = AEHealthTheme.shapes.rounded12
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(AEHealthTheme.dimens.defaultContainerPadding),
                horizontalArrangement = Arrangement.spacedBy(AEHealthTheme.dimens.defaultSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (message is SnackbarMessage.ResultMessage)
                    VerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .clip(AEHealthTheme.shapes.rounded4),
                        thickness = AEHealthTheme.dimens.snackbarDividerThickness,
                        color = message.color
                    )

                Text(message.message)
            }
        }
    }
}
