package com.example.flightsearch.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.ui.HomeScreenUiState
import com.example.flightsearch.ui.HomeScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAirport: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(
        factory = HomeScreenViewModel.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.app_name),
                canNavigateBack = false, onBackButtonClick = { }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(it)
        ){
            Surface(
                color = Color.Blue
            ){
                TextField(
                    value = when (uiState) {
                        is HomeScreenUiState.Default -> ""
                        is HomeScreenUiState.WithPhase -> uiState.phase
                    },
                    onValueChange = { phase ->
                        viewModel.updatePhase(phase)
                        coroutineScope.launch {
                            viewModel.refresh(phase)
                        }
                        Log.d("UiState",uiState.toString())
                    },
                    trailingIcon = {
                        when (uiState) {
                            is HomeScreenUiState.Default -> { }
                            is HomeScreenUiState.WithPhase -> {
                                IconButton(onClick = {
                                    coroutineScope.launch{
                                        viewModel.refresh("")
                                    }

                                }){
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    },
                    leadingIcon = {Icon(imageVector = Icons.Filled.Search, contentDescription = null)},
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color.White)

                )
            }
            when(uiState){
                is HomeScreenUiState.Default -> {
                    FavoriteList(
                        favorites = uiState.favorites,
                        onStarClicked = {  },
                        onStarUnclicked = viewModel::removeFromFavorites
                    )
                }
                is HomeScreenUiState.WithPhase -> {
                    SuggestionList(
                        suggestions = uiState.airports,
                        onSelectionClicked = navigateToAirport
                    )
                }
            }
        }
    }


}

