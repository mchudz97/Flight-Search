package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun deleteFavorite(favorite: Favorite)
    fun getAll(): Flow<List<Favorite>>
}