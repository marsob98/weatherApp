package com.example.weatherapp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.data.remote.model.GeocodingResponse
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
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
            SmallTopAppBar(
                title = { Text("Wyszukaj miasto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Powrót")
                    }
                }
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
                    placeholder = { Text("Wpisz nazwę miasta...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Szukaj")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.White
                    )
                } else if (error != null && searchQuery.text.length >= 3) {
                    Text(
                        text = "Błąd wyszukiwania: $error",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (searchResults.isEmpty() && searchQuery.text.length >= 3) {
                    Text(
                        text = "Nie znaleziono miast dla zapytania: ${searchQuery.text}",
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3A3E59).copy(alpha = 0.7f)
        )
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
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )

                Text(
                    text = "${result.state ?: ""}, ${result.country}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Wyszukaj",
                    tint = Color.White
                )
            }
        }
    }
}