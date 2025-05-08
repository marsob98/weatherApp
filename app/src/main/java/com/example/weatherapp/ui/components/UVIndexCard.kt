package com.example.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.UVIndexResponse
import com.example.weatherapp.data.remote.model.UVRecommendation
import com.example.weatherapp.ui.theme.LocalWeatherColors

@Composable
fun UVIndexCard(uvIndex: UVIndexResponse?) {
    if (uvIndex == null) return

    val weatherColors = LocalWeatherColors.current
    val recommendation = UVRecommendation.getRecommendation(uvIndex.value)
    val gradientColors = when {
        uvIndex.value < 3 -> listOf(Color(0xFF4CAF50), Color(0xFF8BC34A)) // Zielony
        uvIndex.value < 6 -> listOf(Color(0xFFFFEB3B), Color(0xFFFFC107)) // Żółty
        uvIndex.value < 8 -> listOf(Color(0xFFFF9800), Color(0xFFFF5722)) // Pomarańczowy
        uvIndex.value < 11 -> listOf(Color(0xFFF44336), Color(0xFFE91E63)) // Czerwony
        else -> listOf(Color(0xFF9C27B0), Color(0xFF673AB7)) // Fioletowy
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
                text = "Indeks UV",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.radialGradient(gradientColors),
                            shape = RoundedCornerShape(40.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uvIndex.value.toInt().toString(),
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = recommendation.level,
                        style = MaterialTheme.typography.titleMedium,
                        color = weatherColors.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = recommendation.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = weatherColors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Zalecenia:",
                style = MaterialTheme.typography.bodyLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = recommendation.recommendation,
                style = MaterialTheme.typography.bodyMedium,
                color = weatherColors.textSecondary
            )
        }
    }
}