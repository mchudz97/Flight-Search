package com.example.flightsearch

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flightsearch.ui.FlightSearchNavHost

@Composable
fun FlightSearchApp(navController: NavHostController = rememberNavController()) {
    FlightSearchNavHost(navController = navController)
}