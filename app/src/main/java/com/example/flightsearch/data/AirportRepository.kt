package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface AirportRepository {
    fun getAllAirportsStream(): Flow<List<Airport>>
    fun getSuggestedAirportsStream(phase: String): Flow<List<Airport>>
    fun getAirportStream(iataCode: String): Flow<Airport>
}