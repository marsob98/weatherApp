package com.example.weatherapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.AstronomicalData
import com.example.weatherapp.data.remote.model.MoonPhaseData
import com.example.weatherapp.ui.theme.LocalWeatherColors

@Composable
fun AstronomicalCard(
    astronomicalData: AstronomicalData?,
    moonPhaseData: MoonPhaseData? = null
) {
    if (astronomicalData == null) return

    val weatherColors = LocalWeatherColors.current
    val currentProgress = astronomicalData.getDayProgress()

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
                text = "Dane astronomiczne",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Wizualizacja wschodu i zachodu słońca
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val width = size.width
                    val height = size.height
                    val arcRadius = height * 0.8f

                    // Rysowanie łuku reprezentującego ścieżkę słońca
                    drawArc(
                        color = Color.Gray.copy(alpha = 0.3f),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(0f, height - arcRadius),
                        size = androidx.compose.ui.geometry.Size(width, arcRadius * 2),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Rysowanie postępu dnia
                    drawArc(
                        color = Color(0xFFFFA000),
                        startAngle = 180f,
                        sweepAngle = 180f * currentProgress / 100f,
                        useCenter = false,
                        topLeft = Offset(0f, height - arcRadius),
                        size = androidx.compose.ui.geometry.Size(width, arcRadius * 2),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Rysowanie pozycji słońca
                    val sunAngle = 180f + (180f * currentProgress / 100f)
                    val sunX = width / 2 + arcRadius * kotlin.math.cos(Math.toRadians(sunAngle.toDouble())).toFloat()
                    val sunY = height - arcRadius + arcRadius * kotlin.math.sin(Math.toRadians(sunAngle.toDouble())).toFloat()

                    if (currentProgress in 1..99) {
                        drawCircle(
                            color = Color(0xFFFFA000),
                            radius = 8.dp.toPx(),
                            center = Offset(sunX, sunY)
                        )
                    }
                }

                // Etykiety wschodu i zachodu słońca
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Wschód",
                            style = MaterialTheme.typography.bodySmall,
                            color = weatherColors.textSecondary
                        )
                        Text(
                            text = astronomicalData.getSunriseFormatted(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = weatherColors.textPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Zachód",
                            style = MaterialTheme.typography.bodySmall,
                            color = weatherColors.textSecondary
                        )
                        Text(
                            text = astronomicalData.getSunsetFormatted(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = weatherColors.textPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Długość dnia",
                        style = MaterialTheme.typography.bodySmall,
                        color = weatherColors.textSecondary
                    )
                    Text(
                        text = astronomicalData.getDayLength(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = weatherColors.textPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (moonPhaseData != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Faza księżyca",
                            style = MaterialTheme.typography.bodySmall,
                            color = weatherColors.textSecondary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = moonPhaseData.getMoonPhaseEmoji(),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = moonPhaseData.getMoonPhaseName(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = weatherColors.textPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}