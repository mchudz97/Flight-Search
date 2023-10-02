package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineFavoriteRepository(
    private val dao: FavoriteDao
) : FavoriteRepository {
    override suspend fun insertFavorite(favorite: Favorite) = dao.insert(favorite)
    override suspend fun deleteFavorite(favorite: Favorite) = dao.delete(favorite)
    override fun getAll(): Flow<List<Favorite>> = dao.getAll()
    override fun find(departureCode: String, destinationCode: String) = dao
        .find(departureCode, destinationCode)
}