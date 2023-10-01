package com.example.flightsearch.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.ui.SearchResultViewModel

@Composable
fun SearchResultScreen(
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchResultViewModel = viewModel(factory = SearchResultViewModel.Factory)
){
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = uiState.value.airport?.iataCode ?: "",
                fullTitle = uiState.value.airport?.name ?: "",
                canNavigateBack = true,
                onBackButtonClick = onBackButtonClicked)
        }
    ) {
        FavoriteList(
            favorites = uiState.value.possibleConnections,
            onStarClicked = viewModel::addToFavorites,
            onStarUnclicked = viewModel::removeFromFavorites,
            modifier = Modifier.padding(it)
        )
    }


}