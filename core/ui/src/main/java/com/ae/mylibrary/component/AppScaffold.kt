package com.ae.mylibrary.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.ae.mylibrary.snackbar.AppSnackbarHost

@Composable
fun AppScaffold(
    topBar: @Composable () -> Unit = { },
    bottomBar: @Composable () -> Unit = { },
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = { AppSnackbarHost() }
    ) { innerPadding -> content(innerPadding) }

}