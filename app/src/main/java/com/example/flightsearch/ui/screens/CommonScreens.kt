package com.example.flightsearch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.Flight
import com.example.flightsearch.ui.ROUTE_RESULT
import com.example.flightsearch.ui.theme.FlightSearchTheme
import kotlinx.coroutines.launch


@Composable
fun TopAppBar(
    title: String,
    fullTitle: String? = null,
    canNavigateBack: Boolean,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    //fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                if(!fullTitle.isNullOrBlank()){
                    Text(
                        text = fullTitle,
                        //fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.widthIn(0.dp, 280.dp)
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if(canNavigateBack){
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Blue)
    )
}

@Composable
fun SuggestionList(
    suggestions: List<Airport>,
    onSelectionClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(4.dp, 2.dp)
    ) {
        items(
            items = suggestions,
            key = { airport -> airport.id }
        ) {
            Column(modifier = Modifier.clickable {
                onSelectionClicked("$ROUTE_RESULT/${it.iataCode}")
            }
            ) {
                Row {
                    Text(
                        text = it.iataCode,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(8.dp, 0.dp))
                    Text(
                        text = it.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.Blue))
            }

        }
    }
}

@Composable
fun FavoriteList(
    favorites: List<Flight>,
    onStarClicked: suspend (Flight) -> Unit,
    onStarUnclicked: suspend (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            items = favorites,
            //key = {fav -> fav.id ?: throw IllegalArgumentException(fav.toString())}
        ) {
            Card(
                shape = MaterialTheme.shapes.extraSmall,
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ){
                    FlightDetails(favorite = it, modifier = Modifier.weight(1.0f))
                    IconButton(onClick = {
                        coroutineScope.launch {
                            if(it.isFavorite)
                                onStarUnclicked(it)
                            else
                                onStarClicked(it)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = if(it.isFavorite) Color.Yellow else Color.DarkGray,
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun FlightDetails(favorite: Flight, modifier: Modifier = Modifier){
    Column(modifier){
        Text(
            text = stringResource(id = R.string.depart),
            style = MaterialTheme.typography.labelMedium
        )
        Row {
            Text(
                text = favorite.departureCode,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp, 0.dp))
            Text(
                text = favorite.departurePortName
            )
        }
        Text(
            text = stringResource(id = R.string.arrive),
            style = MaterialTheme.typography.labelMedium
        )
        Row {
            Text(
                text = favorite.destinationCode,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp, 0.dp))
            Text(
                text = favorite.destinationPortName
            )
        }
    }
}
@Preview
@Composable
fun TopAppBarPreview(){
    TopAppBar(title = "xd", fullTitle = "xDDDDDDDDDDDDDDD", canNavigateBack = true, onBackButtonClick = {})
}
@Preview
@Composable
fun SuggestionListPreview(){
    val suggestions = listOf(
        Airport(2137, "JP2", "Kremowkowo", 2137),
        Airport(21, "GMD", "Yellowland", 2137)
    )

    FlightSearchTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            SuggestionList(suggestions = suggestions, onSelectionClicked = {})
        }
    }
}
@Preview
@Composable
fun FlightDetailsPreview(){
    val fav: Flight = Flight(
        2137,
        "JP2",
        "GMD",
        "Kremowkowo",
        "Yellowland"
    )
    FlightSearchTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            FlightDetails(favorite = fav)
        }
    }
}

@Preview
@Composable
fun ListPreview(){
    val favs = listOf<Flight>(
        Flight(
            2137,
            "JP2",
            "GMD",
            "Kremowkowo",
            "Yellowland"
        ),
        Flight(
            21137,
            "JP2",
            "GMD",
            "Kremowkowo",
            "Yellowland"
        ),
        Flight(
            21337,
            "JP2",
            "GMD",
            "Kremowkowo",
            "Yellowland",
            true
        )
    )
    FlightSearchTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            FavoriteList(favorites = favs, { }, { })
        }
    }
}
