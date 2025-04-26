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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    var cities by remember { mutableStateOf(listOf<String>()) }

    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF2E3346),
                Color(0xFF1C1B33)
            )
        )
    }

    LaunchedEffect(searchQuery.text) {
        if (searchQuery.text.length >= 3) {
            // W rzeczywistej aplikacji pobieralibyśmy listę miast z API
            // Na potrzeby tego przykładu użyjemy statycznej listy
            cities = listOf(
                "Warszawa", "Kraków", "Wrocław", "Poznań", "Gdańsk",
                "Szczecin", "Łódź", "Lublin", "Katowice", "Białystok"
            ).filter { it.contains(searchQuery.text, ignoreCase = true) }
        } else {
            cities = emptyList()
        }
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
                    onValueChange = { searchQuery = it },
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

                        // Te parametry możesz zachować bez zmian
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(cities) { city ->
                        CityItem(
                            city = city,
                            onClick = {
                                viewModel.getWeatherForCity(city)
                                onNavigateBack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CityItem(
    city: String,
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
            Text(
                text = city,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

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