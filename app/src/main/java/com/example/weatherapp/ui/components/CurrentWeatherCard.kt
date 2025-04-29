package com.example.weatherapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.remote.model.WeatherResponse
import com.example.weatherapp.ui.theme.LocalWeatherColors
import com.example.weatherapp.ui.utils.formatTimestamp
import com.example.weatherapp.viewmodel.FavouriteViewModel
import java.util.*

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
        animationSpec = tween(300)
    )

    // Sprawdzamy, czy to miasto jest ulubione
    LaunchedEffect(weather.name) {
        favouriteViewModel.isFavourite(weather.name) { isFav ->
            isFavourite = isFav
        }
    }

    // Używamy GlassmorphicCard zamiast standardowej Card
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        backgroundColor = weatherColors.cardBackground,
        borderColor = weatherColors.textPrimary.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dodajemy nazwę miasta na górze karty
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = weather.name,
                    style = MaterialTheme.typography.h5,
                    color = weatherColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                // Przycisk ulubione przenosimy do tego samego wiersza co nazwa miasta
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
                        contentDescription = if (isFavourite)
                            stringResource(R.string.favorite_remove)
                        else
                            stringResource(R.string.favorite_add),
                        tint = heartColor
                    )
                }
            }

            // Główne informacje o temperaturze
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dodajemy ikonę pogody obok temperatury
                WeatherIcon(
                    iconCode = weather.weather.firstOrNull()?.icon ?: "",
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.weather_temp_format, weather.main.temp.toInt()),
                    style = MaterialTheme.typography.h2,
                    color = weatherColors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Dodanie funkcji rozszerzającej capitalize() jeśli jest przestarzała
            val description = weather.weather.firstOrNull()?.description?.let {
                it.replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase(Locale.getDefault())
                    else char.toString()
                }
            } ?: ""

            Text(
                text = description,
                style = MaterialTheme.typography.h6,
                color = weatherColors.textPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.weather_feels_like),
                        style = MaterialTheme.typography.caption,
                        color = weatherColors.textSecondary
                    )
                    Text(
                        text = stringResource(R.string.weather_temp_format, weather.main.feelsLike.toInt()),
                        style = MaterialTheme.typography.body1,
                        color = weatherColors.textPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.weather_min_max),
                        style = MaterialTheme.typography.caption,
                        color = weatherColors.textSecondary
                    )
                    Text(
                        text = stringResource(
                            R.string.weather_min_max_format,
                            weather.main.tempMin.toInt(),
                            weather.main.tempMax.toInt()
                        ),
                        style = MaterialTheme.typography.body1,
                        color = weatherColors.textPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(
                    R.string.weather_sunrise_sunset_format,
                    formatTimestamp(weather.sys.sunrise),
                    formatTimestamp(weather.sys.sunset)
                ),
                style = MaterialTheme.typography.caption,
                color = weatherColors.textSecondary
            )
        }
    }
}