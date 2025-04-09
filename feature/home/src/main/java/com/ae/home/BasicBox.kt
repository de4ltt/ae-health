package com.ae.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ae.home.viewmodel.SearchViewModel
import com.ae.search.model.ISearchItem
import javax.inject.Inject
import androidx.compose.runtime.getValue

@Composable
fun BasicBox(
    searchViewModel: SearchViewModel
) {

    val items by searchViewModel.foundObjects.collectAsState()
    val exception by searchViewModel.exception.collectAsState()

    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeContent)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(40.dp)
                .background(Color.Green)
                .clickable(onClick = { searchViewModel.onRegularSearch() }),
            contentAlignment = Alignment.Center,
            content = {
                Text("PRESS ME FOR REGULAR SEARCH")
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(40.dp)
                .background(Color.Green)
                .clickable(onClick = { searchViewModel.onNearbySearch() }),
            contentAlignment = Alignment.Center,
            content = {
                Text("PRESS ME FOR NEARBY SEARCH")
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            exception?.let {
                item {
                    Text(text = it, color = Color.Red)
                }
            }

            items(items) {

                val text = """
        title: ${it.title},
        subtitle: ${it.subtitle},
        category: ${it.category.toString()},
        imageUri: ${it.imageUri},
    """

                Text(text = text, color = Color.Black)
            }
        }
    }
}