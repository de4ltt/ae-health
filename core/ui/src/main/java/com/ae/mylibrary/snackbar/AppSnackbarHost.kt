package com.ae.mylibrary.snackbar

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.sync.withLock

@Composable
fun AppSnackbarHost(
    modifier: Modifier = Modifier,
    durationMillis: Long = 5000
) {

    var currentMessage by remember { mutableStateOf<SnackbarMessage?>(null) }

    LaunchedEffect(Unit) {
        SnackbarManager.observe().collectLatest { message ->
            SnackbarManager.mutex.withLock {
                currentMessage = message
                delay(durationMillis)
                currentMessage = null
            }
        }
    }

    currentMessage?.let {
        AppSnackbar(
            durationMillis = durationMillis,
            modifier = modifier,
            message = it
        )
    }
}
