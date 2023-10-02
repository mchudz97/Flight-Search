package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

sealed interface HomeScreenUiState {
    data class Default(val favorites: List<Flight>) : HomeScreenUiState
    data class WithPhase(val airports: List<Airport>, val phase: String) : HomeScreenUiState

}

class HomeScreenViewModel(
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(HomeScreenUiState
        .Default(emptyList()))

    init {
        viewModelScope.launch {
            userPreferencesRepository.searchPhase.collect{
                if(it.isEmpty())
                    setupDefaultState()
                else
                    setupWithPhaseState(it)
            }
        }
    }
    fun updatePhase(newPhase: String){
        if(newPhase.isNotEmpty())
            uiState.value = HomeScreenUiState.WithPhase(emptyList(), newPhase)
    }
    suspend fun refresh(newPhase: String) {
        userPreferencesRepository.saveSearchPhase(newPhase)
        if(newPhase.isEmpty()){
            when(uiState.value){
                is HomeScreenUiState.Default ->  { }
                is HomeScreenUiState.WithPhase ->{
                    setupDefaultState()
                }
            }
        }
        else{
            setupWithPhaseState(newPhase)
        }
    }

    suspend fun removeFromFavorites(fav: Flight) = favoriteRepository
        .deleteFavorite(fav.toFavorite())


    private suspend fun setupDefaultState(){
        favoriteRepository.getAll().collect{ favs: List<Favorite> ->

            val flights = favs.map { fav: Favorite ->
                val departure = airportRepository
                    .getAirportStream(fav.departureCode).firstOrNull()
                val destination = airportRepository
                    .getAirportStream(fav.destinationCode).firstOrNull()
                Flight(
                    id = fav.id,
                    departureCode = fav.departureCode,
                    destinationCode = fav.destinationCode,
                    departurePortName = departure?.name ?: "",
                    destinationPortName = destination?.name ?: "",
                    isFavorite = true
                )
            }

            uiState.value = HomeScreenUiState.Default(flights)
        }
    }
    private suspend fun setupWithPhaseState(newPhase: String){
        airportRepository.getSuggestedAirportsStream(newPhase).collect{
            uiState.value = HomeScreenUiState.WithPhase(it, newPhase)
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightSearchApplication)
                HomeScreenViewModel(
                    application.appContainer.airportRepository,
                    application.appContainer.favoriteRepository,
                    application.userPreferencesRepository
                )
            }
        }
    }
}

data class Flight(
    val id: Int?,
    val departureCode: String,
    val destinationCode: String,
    val departurePortName: String = "",
    val destinationPortName: String = "",
    val isFavorite: Boolean = false
)

fun Flight.toFavorite(): Favorite  {
    return if(id == null)
        Favorite(departureCode = departureCode, destinationCode = destinationCode)
    else
        Favorite(id = id, departureCode = departureCode, destinationCode = destinationCode)
}