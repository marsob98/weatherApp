// app/src/main/java/com/example/weatherapp/ui/components/CurrentWeatherCard.kt
package com.example.weatherapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.remote.model.WeatherResponse
import com.example.weatherapp.ui.theme.LocalWeatherColors
import com.example.weatherapp.ui.utils.formatTimestamp
import com.example.weatherapp.viewmodel.FavouriteViewModel

@Composable
fun CurrentWeatherCard(
    weather: WeatherResponse,
    favouriteViewModel: FavouriteViewModel = hiltViewModel()
) {
    var isFavourite by remember { mutableStateOf(false) }
    val weatherColors = LocalWeatherColors.current

    // Animowany kolor dla ikony serca
    val heartColor by animateColorAsState(
        targetValue = if (isFavourite) weatherColors.heartActive else weatherColors.heartInactive,
        animationSpec = tween(300),
        label = "Heart color"
    )

    // Sprawdzamy, czy to miasto jest ulubione
    LaunchedEffect(weather.name) {
        favouriteViewModel.isFavourite(weather.name) { isFav ->
            isFavourite = isFav
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = weatherColors.cardBackground,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
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
                        tint = heartColor
                    )
                }
            }

            Text(
                text = "${weather.main.temp.toInt()}°",
                style = MaterialTheme.typography.displayLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = weather.weather.firstOrNull()?.description?.capitalize() ?: "",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Odczuwalna",
                        style = MaterialTheme.typography.bodySmall,
                        color = weatherColors.textSecondary
                    )
                    Text(
                        text = "${weather.main.feelsLike.toInt()}°",
                        style = MaterialTheme.typography.bodyLarge,
                        color = weatherColors.textPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Min/Max",
                        style = MaterialTheme.typography.bodySmall,
                        color = weatherColors.textSecondary
                    )
                    Text(
                        text = "${weather.main.tempMin.toInt()}°/${weather.main.tempMax.toInt()}°",
                        style = MaterialTheme.typography.bodyLarge,
                        color = weatherColors.textPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Wschód: ${formatTimestamp(weather.sys.sunrise)} • Zachód: ${formatTimestamp(weather.sys.sunset)}",
                style = MaterialTheme.typography.bodySmall,
                color = weatherColors.textSecondary
            )
        }
    }
}