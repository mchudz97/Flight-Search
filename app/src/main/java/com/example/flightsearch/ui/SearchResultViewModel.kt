package com.example.flightsearch.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class ResultUiState(val airport: Airport?, val possibleConnections: List<Flight>)
class SearchResultViewModel(
    savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val airportCode: String = checkNotNull(savedStateHandle[IATA_CODE])
    val uiState: MutableStateFlow<ResultUiState> =
        MutableStateFlow(ResultUiState(null, emptyList()))
    init {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchPhase(airportCode)
            userPreferencesRepository.searchPhase.collect { airportCode ->
                airportRepository.getAirportStream(airportCode).collect { resultAirport ->
                    airportRepository.getAllAirportsStream().map { airports ->
                        airports.filter { it.iataCode != resultAirport.iataCode }.map {
                            Flight(
                                id = null,
                                departureCode = resultAirport.iataCode,
                                destinationCode = it.iataCode,
                                departurePortName = resultAirport.name,
                                destinationPortName = it.name
                            )
                        }
                    }.collect { flights: List<Flight> ->
                        favoriteRepository.getAll().map {
                            it.filter { fav ->
                                fav.departureCode == airportCode
                            }
                        }.collect { favorites ->
                            val updatedFlights = flights.map { flight ->
                                val fav = favorites.firstOrNull {
                                    it.destinationCode == flight.destinationCode
                                }
                                if (fav != null) Flight(
                                    fav.id,
                                    flight.departureCode,
                                    flight.destinationCode,
                                    flight.departurePortName,
                                    flight.destinationPortName,
                                    true
                                )
                                else
                                    flight
                            }
                            uiState.value = ResultUiState(
                                airport = resultAirport,
                                possibleConnections = updatedFlights
                            )
                        }
                    }
                }
            }
        }

    }


    suspend fun addToFavorites(flight: Flight) = favoriteRepository
        .insertFavorite(flight.toFavorite())

    suspend fun removeFromFavorites(flight: Flight) = favoriteRepository
        .deleteFavorite(flight.toFavorite())
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
                SearchResultViewModel(
                    this.createSavedStateHandle(),
                    application.appContainer.airportRepository,
                    application.appContainer.favoriteRepository,
                    application.userPreferencesRepository
                )
            }
        }
    }
}

