package com.ae.health

import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.ae.home.BasicBox
import com.ae.home.viewmodel.SearchViewModel
import com.ae.home.viewmodel.SearchViewModelFactory
import javax.inject.Inject

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
