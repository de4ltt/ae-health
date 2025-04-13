package com.ae.health

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.ae.home.BasicBox
import com.ae.home.viewmodel.SearchViewModel

class MainActivity : ComponentActivity() {

    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (applicationContext as AEHeathApp).appComponent

        val searchViewModelFactory = appComponent.searchViewModelFactory()
        searchViewModel = ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]

        setContent {
            BasicBox(
                searchViewModel
            )
        }
    }
}
