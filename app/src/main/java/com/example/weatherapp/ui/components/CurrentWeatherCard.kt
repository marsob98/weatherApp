// Plik: app/src/main/java/com/example/weatherapp/ui/components/CurrentWeatherCard.kt (cały plik)
package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.remote.model.WeatherResponse
import com.example.weatherapp.ui.utils.formatTimestamp
import com.example.weatherapp.viewmodel.FavouriteViewModel

@Composable
fun CurrentWeatherCard(
    weather: WeatherResponse,
    favouriteViewModel: FavouriteViewModel = hiltViewModel()
) {
    var isFavourite by remember { mutableStateOf(false) }

    // Sprawdzamy, czy to miasto jest ulubione
    LaunchedEffect(weather.name) {
        favouriteViewModel.isFavourite(weather.name) { isFav ->
            isFavourite = isFav
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3A3E59).copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dodajemy przycisk do zarządzania ulubionymi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        if (isFavourite) {
                            favouriteViewModel.removeFavourite(weather.name)
                            isFavourite = false
                        } else {
                            favouriteViewModel.addFavourite(weather.name)
                            isFavourite = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavourite) "Usuń z ulubionych" else "Dodaj do ulubionych",
                        tint = if (isFavourite) Color(0xFFFF4081) else Color.White
                    )
                }
            }

            Text(
                text = "${weather.main.temp.toInt()}°",
                fontSize = 72.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = weather.weather.firstOrNull()?.description?.capitalize() ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Odczuwalna",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "${weather.main.feelsLike.toInt()}°",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Min/Max",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "${weather.main.tempMin.toInt()}°/${weather.main.tempMax.toInt()}°",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Wschód: ${formatTimestamp(weather.sys.sunrise)} • Zachód: ${formatTimestamp(weather.sys.sunset)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

// Funkcja pomocnicza do kapitalizacji pierwszej litery
fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}