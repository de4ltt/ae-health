package com.ae.mylibrary.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ae.mylibrary.theme.AEHealthTheme

@Composable
fun <T : List<T>> DataItems(
    modifier: Modifier = Modifier,
    items: StateFlow<T>,
) {

    val items by items.collectAsStateWithLifecycle()

    val contentAlignment by remember {
        derivedStateOf { if (items.isNotEmpty()) Alignment.TopCenter else Alignment.Center }
    }

    val lazyListState = rememberLazyListState()

    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {

        if (items.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier.wrapContentHeight().fillMaxWidth(),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(AEHealthTheme.dimens.defaultSpacing),
            ) {

                items(items) { item ->

                }

            }

        }

    }

}