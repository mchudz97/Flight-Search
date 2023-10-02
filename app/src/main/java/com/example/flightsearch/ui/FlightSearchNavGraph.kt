package com.example.flightsearch.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flightsearch.ui.screens.HomeScreen
import com.example.flightsearch.ui.screens.SearchResultScreen

const val ROUTE_HOME = "Home"
const val IATA_CODE = "IataCode"
const val ROUTE_RESULT = "Result"
const val ROUTE_RESULT_WITH_ARG = "$ROUTE_RESULT/{$IATA_CODE}"
@Composable
fun FlightSearchNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
  NavHost(navController = navController, startDestination = ROUTE_HOME, modifier = modifier){
      composable(route = ROUTE_HOME){
          HomeScreen(navigateToAirport = navController::navigate )
      }
      composable(route = ROUTE_RESULT_WITH_ARG, arguments = listOf(navArgument(IATA_CODE){
          type = NavType.StringType
      })
      ){
          SearchResultScreen(
              onBackButtonClicked = { navController.popBackStack() }
          )
      }
  }  
}