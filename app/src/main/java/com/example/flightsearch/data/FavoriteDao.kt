package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("Select * from favorite")
    fun getAll(): Flow<List<Favorite>>

    @Query("Select * from favorite where departure_code = :departureCode" +
            " and destination_code = :destinationCode")
    fun find(departureCode: String, destinationCode: String): Flow<Favorite>

}