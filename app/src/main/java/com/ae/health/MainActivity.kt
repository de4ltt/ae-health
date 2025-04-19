package com.ae.health

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ae.health.viewmodel_provider.AppViewModelProvider
import com.ae.mylibrary.component.AppScaffold

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = AppViewModelProvider(this)

        setContent {

            val navHostController = rememberNavController()

            AppScaffold { padding ->
                AppNavHost(
                    modifier = Modifier.padding(padding),
                    navHostController = navHostController,
                    viewModelProvider = viewModelProvider
                )
            }
        }
    }
}