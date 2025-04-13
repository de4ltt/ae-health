package com.ae.health

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ae.health.viewmodel_provider.AppViewModelProvider
import com.ae.home.HomeScreen
import com.ae.home.viewmodel.SearchViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = AppViewModelProvider(this)

        setContent {
            HomeScreen(searchViewModel = viewModelProvider<SearchViewModel>())
        }
    }
}