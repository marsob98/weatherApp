// app/src/main/java/com/example/weatherapp/ui/components/LocationWeatherCard.kt
package com.example.weatherapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.WeatherResponse
import com.example.weatherapp.ui.theme.LocalWeatherColors

@Composable
fun LocationWeatherCard(
    weather: WeatherResponse?,
    isLoading: Boolean,
    onRefreshLocation: () -> Unit
) {
    val weatherColors = LocalWeatherColors.current

    // Animacja dla przycisku odświeżania
    var isRefreshing by remember { mutableStateOf(false) }
    val rotationState = remember { Animatable(0f) }

    LaunchedEffect(isLoading) {
        isRefreshing = isLoading
        if (isLoading) {
            // Rozpocznij obracanie
            rotationState.animateTo(
                targetValue = rotationState.value + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            // Zatrzymaj animację
            rotationState.stop()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = weatherColors.cardBackground.copy(alpha = 0.1f)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = "Lokalizacja",
                        tint = weatherColors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Twoja lokalizacja",
                        style = MaterialTheme.typography.titleMedium,
                        color = weatherColors.textPrimary
                    )
                }

                IconButton(onClick = onRefreshLocation) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Odśwież lokalizację",
                        tint = weatherColors.textPrimary,
                        modifier = Modifier.rotate(rotationState.value)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    color = weatherColors.textPrimary,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (weather != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = weather.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = weatherColors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${weather.main.temp.toInt()}°",
                        style = MaterialTheme.typography.displayMedium,
                        color = weatherColors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = weather.weather.firstOrNull()?.description?.capitalize() ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        color = weatherColors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Wilgotność",
                                style = MaterialTheme.typography.bodySmall,
                                color = weatherColors.textSecondary
                            )
                            Text(
                                text = "${weather.main.humidity}%",
                                style = MaterialTheme.typography.bodyLarge,
                                color = weatherColors.textPrimary
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Wiatr",
                                style = MaterialTheme.typography.bodySmall,
                                color = weatherColors.textSecondary
                            )
                            Text(
                                text = "${weather.wind.speed} m/s",
                                style = MaterialTheme.typography.bodyLarge,
                                color = weatherColors.textPrimary
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Nie udało się pobrać danych pogodowych dla Twojej lokalizacji",
                    style = MaterialTheme.typography.bodyMedium,
                    color = weatherColors.textPrimary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}