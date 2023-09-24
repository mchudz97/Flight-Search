package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineAirportRepository(
    private val dao: AirportDao
) : AirportRepository{
    override fun getAllAirportsStream(): Flow<List<Airport>> = dao.getAll()

    override fun getSuggestedAirportsStream(phase: String): Flow<List<Airport>> = dao
        .getSuggestedAirports(phase)

    override fun getAirportStream(iataCode: String): Flow<Airport> = dao.getAirport(iataCode)
}