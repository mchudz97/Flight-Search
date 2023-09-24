package com.example.flightsearch.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    val searchPhase: Flow<String> = dataStore.data.catch{
        if(it is IOException) emit(emptyPreferences())
        else throw it
    }.map{ it[SEARCH_PHASE] ?: ""}

    private companion object {
        val SEARCH_PHASE = stringPreferencesKey("search_phase")
    }

    suspend fun saveSearchPhase(phase: String) = dataStore.edit {
        it[SEARCH_PHASE] = phase
    }
}