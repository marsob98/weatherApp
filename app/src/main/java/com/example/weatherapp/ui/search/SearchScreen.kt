package com.example.weatherapp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import com.example.weatherapp.R
import com.example.weatherapp.data.remote.model.GeocodingResponse
import com.example.weatherapp.ui.components.GlassmorphicCard
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterialApi::class, FlowPreview::class)
@Composable
fun SearchScreen(
    weatherViewModel: WeatherViewModel,
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    val searchResults by weatherViewModel.searchResults.collectAsState(initial = emptyList())
    val isSearching = weatherViewModel.isSearching.value
    val error = weatherViewModel.error.value

    // Debounce do wyszukiwania
    val searchQueryFlow = remember { MutableStateFlow("") }

    LaunchedEffect(Unit) {
        searchQueryFlow
            .debounce(500) // 500ms opóźnienia
            .filter { it.isNotEmpty() && it.length >= 3 }
            .distinctUntilChanged()
            .collect {
                weatherViewModel.searchCities(it)
            }
    }

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
                title = { Text(stringResource(R.string.screen_search)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        searchQueryFlow.value = it.text
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            stringResource(R.string.search_hint),
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.action_search),
                            tint = Color.White
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        backgroundColor = Color.Transparent
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.White
                    )
                } else if (error != null && searchQuery.text.length >= 3) {
                    Text(
                        text = error,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (searchResults.isEmpty() && searchQuery.text.length >= 3) {
                    Text(
                        text = stringResource(R.string.no_cities_found, searchQuery.text),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (searchQuery.text.length < 3 && searchQuery.text.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.search_min_chars),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn {
                        items(searchResults) { result ->
                            CitySearchResult(
                                result = result,
                                onClick = {
                                    weatherViewModel.getWeatherForCity(result.name)
                                    onNavigateBack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CitySearchResult(
    result: GeocodingResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.name,
                    style = MaterialTheme.typography.body1,
                    color = Color.White
                )

                Text(
                    text = buildString {
                        if (result.state != null) {
                            append(result.state)
                            append(", ")
                        }
                        append(result.country)
                    },
                    style = MaterialTheme.typography.body2,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.action_search),
                    tint = Color.White
                )
            }
        }
    }
}