package com.ae.mylibrary.snackbar

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

object SnackbarManager {

    private val messages = MutableSharedFlow<SnackbarMessage>()
    internal val mutex = Mutex()

    fun CoroutineScope.showSnackbarMessage(message: SnackbarMessage) = this.launch {
        messages.emit(message)
    }

    internal fun observe(): Flow<SnackbarMessage> =
        messages.distinctUntilChanged { old, new -> old.id == new.id }

}
