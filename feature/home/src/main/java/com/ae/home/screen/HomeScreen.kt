package com.ae.home.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ae.home.viewmodel.SearchViewModel
import com.ae.mylibrary.snackbar.SnackbarManager.showSnackbarMessage
import com.ae.mylibrary.snackbar.SnackbarMessage

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    searchViewModel: SearchViewModel
) {

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable(
                onClick = {
                    coroutineScope.showSnackbarMessage(
                        message = SnackbarMessage.ResultMessage.SuccessMessage(
                            "AMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAAAMERICAA"
                        )
                    )
                }
            )
    ) {
        Text("PUSH ME")
    }

}