package com.example.flightsearch

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearch.data.AppContainer
import com.example.flightsearch.data.AppDataContainer
import com.example.flightsearch.data.UserPreferencesRepository

private const val PREFERENCE_NAME = "prefs"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCE_NAME
)
class FlightSearchApplication: Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        appContainer = AppDataContainer(this)
    }
}