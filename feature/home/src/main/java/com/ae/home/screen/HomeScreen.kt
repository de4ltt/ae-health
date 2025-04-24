package com.ae.home.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.ae.home.component.HomeSearchBar
import com.ae.home.component.SmallFilterTile
import com.ae.home.model.SearchItemCategoryUI
import com.ae.home.viewmodel.SearchViewModel
import com.ae.home.viewmodel.event.HomeUIEvent
import com.ae.home.viewmodel.event.SearchParamsEvent
import com.ae.mylibrary.theme.AEHealthTheme
import com.ae.search.model.item.util.SearchItemCategory

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    searchViewModel: SearchViewModel,
) {

    Column {

        HomeSearchBar(onEvent = searchViewModel::onEvent)


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(AEHealthTheme.dimens.defaultSpacing)
        ) {

            SearchItemCategoryUI.values.forEach { category ->

                val text = stringResource(category.resId)

                SmallFilterTile(
                    modifier = Modifier.weight(text.length.toFloat()), text = text
                ) {
                    searchViewModel.onEvent(
                        SearchParamsEvent.OnCategoryToggled(category)
                    )
                }
            }
        }

    }
}