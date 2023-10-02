package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("Select * from airport")
    fun getAll(): Flow<List<Airport>>
    @Query("Select * from airport where iata_code like '%' || :phase || '%' " +
            "OR name like '%' || :phase || '%'")
    fun getSuggestedAirports(phase: String): Flow<List<Airport>>

    @Query("Select * from airport where iata_code = :iataCode")
    fun getAirport(iataCode: String): Flow<Airport>
}