// Plik: app/src/main/java/com/example/weatherapp/ui/components/PrecipitationCard.kt
package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.PrecipitationInfo
import com.example.weatherapp.ui.theme.LocalWeatherColors

@Composable
fun PrecipitationCard(precipitationInfo: PrecipitationInfo?) {
    if (precipitationInfo == null) return

    val weatherColors = LocalWeatherColors.current

    // Kolor w zależności od intensywności opadów
    val intensityColor = when {
        precipitationInfo.amount < 0.5 -> Color(0xFF4CAF50) // Zielony
        precipitationInfo.amount < 4.0 -> Color(0xFFFFEB3B) // Żółty
        precipitationInfo.amount < 8.0 -> Color(0xFFFF9800) // Pomarańczowy
        else -> Color(0xFFF44336) // Czerwony
    }

    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        backgroundColor = weatherColors.cardBackground,
        borderColor = weatherColors.textPrimary.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Informacje o opadach",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = precipitationInfo.type,
                        style = MaterialTheme.typography.titleMedium,
                        color = weatherColors.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = precipitationInfo.intensity,
                        style = MaterialTheme.typography.bodyMedium,
                        color = weatherColors.textSecondary
                    )
                }

                Text(
                    text = "${precipitationInfo.amount} mm/${precipitationInfo.period}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = weatherColors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pasek intensywności opadów
            Column {
                Text(
                    text = "Intensywność",
                    style = MaterialTheme.typography.bodySmall,
                    color = weatherColors.textSecondary
                )

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = (precipitationInfo.amount / 10f).coerceIn(0f, 1f),
                    color = intensityColor,
                    backgroundColor = intensityColor.copy(alpha = 0.2f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Lekkie",
                        style = MaterialTheme.typography.bodySmall,
                        color = weatherColors.textSecondary
                    )
                    Text(
                        text = "Intensywne",
                        style = MaterialTheme.typography.bodySmall,
                        color = weatherColors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Zalecenia w zależności od intensywności
            Text(
                text = "Zalecenia:",
                style = MaterialTheme.typography.bodyLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            val recommendation = when {
                precipitationInfo.amount < 0.5 -> "Lekkie opady, możesz bezpiecznie przebywać na zewnątrz."
                precipitationInfo.amount < 4.0 -> "Umiarkowane opady, weź ze sobą parasol."
                precipitationInfo.amount < 8.0 -> "Intensywne opady, zalecana odpowiednia odzież przeciwdeszczowa."
                else -> "Bardzo intensywne opady, jeśli to możliwe, pozostań w pomieszczeniach."
            }

            Text(
                text = recommendation,
                style = MaterialTheme.typography.bodyMedium,
                color = weatherColors.textSecondary
            )
        }
    }
}