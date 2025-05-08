package com.example.weatherapp.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entity.FavouriteEntity
import com.example.weatherapp.viewmodel.FavouriteViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun FavoritesScreen(
    weatherViewModel: WeatherViewModel,
    onNavigateBack: () -> Unit,
    favouriteViewModel: FavouriteViewModel = hiltViewModel()
) {
    val favourites by favouriteViewModel.favourites.collectAsState(initial = emptyList())

    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF2E3346),
                Color(0xFF1C1B33)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.screen_favorites)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                backgroundColor = Color(0xFF2E3346),
                contentColor = Color.White,
                elevation = 4.dp
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(paddingValues)
        ) {
            if (favourites.isEmpty()) {
                Text(
                    text = stringResource(R.string.empty_favourites),
                    style = MaterialTheme.typography.body1,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(favourites) { favourite ->
                        FavouriteItem(
                            favourite = favourite,
                            onItemClick = {
                                weatherViewModel.getWeatherForCity(favourite.cityName)
                                onNavigateBack()
                            },
                            onDeleteClick = {
                                favouriteViewModel.removeFavourite(favourite.cityName)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavouriteItem(
    favourite: FavouriteEntity,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color(0xFF3A3E59).copy(alpha = 0.7f),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = favourite.cityName,
                style = MaterialTheme.typography.body1,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.favorite_remove),
                    tint = Color.White
                )
            }
        }
    }
}