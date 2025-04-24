package com.ae.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import com.ae.home.viewmodel.event.HomeScreenEvent
import com.ae.home.viewmodel.event.HomeUIEvent
import com.ae.home.viewmodel.event.SearchParamsEvent
import com.ae.mylibrary.resources.AppStrings
import com.ae.mylibrary.theme.AEHealthTheme
import com.ae.search.model.item.util.SearchItemCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    onEvent: (HomeUIEvent) -> Unit,
) {

    var text by rememberSaveable { mutableStateOf("") }

    Row {
        Box(
            Modifier
                .fillMaxSize()
                .semantics { isTraversalGroup = true }
        ) {
            SearchBar(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                inputField = {
                    SearchBarDefaults.InputField(
                        modifier = Modifier.background(
                            color = AEHealthTheme.colorScheme.onBackgroundContainer,
                            shape = AEHealthTheme.shapes.rounded12
                        ),
                        query = text,
                        onQueryChange = {
                            onEvent(
                                SearchParamsEvent.OnValueChange(
                                    newValue = it
                                )
                            )
                        },
                        onSearch = {
                            onEvent(HomeScreenEvent.OnSearchToggled)
                        },
                        expanded = false,
                        onExpandedChange = {},
                        placeholder = { Text(stringResource(AppStrings.HomeStrings.searchBarHint)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
                    )
                },
                expanded = false,
                onExpandedChange = {},
            ) {}
        }
    }
}