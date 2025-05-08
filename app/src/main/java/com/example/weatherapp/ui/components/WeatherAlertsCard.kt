package com.example.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.Alert
import com.example.weatherapp.ui.theme.LocalWeatherColors
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherAlertsCard(
    alerts: List<Alert>?,
    onViewAllAlerts: () -> Unit
) {
    if (alerts.isNullOrEmpty()) return

    val weatherColors = LocalWeatherColors.current
    val dateFormat = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Alerty pogodowe",
                    style = MaterialTheme.typography.titleLarge,
                    color = weatherColors.textPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Zobacz wszystkie",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4A90E2),
                    modifier = Modifier.clickable { onViewAllAlerts() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            alerts.take(2).forEach { alert ->
                val severity = alert.getAlertSeverity()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = Color(severity.color),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = alert.event,
                            style = MaterialTheme.typography.bodyLarge,
                            color = weatherColors.textPrimary,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "Od ${dateFormat.format(Date(alert.start * 1000))} do ${dateFormat.format(Date(alert.end * 1000))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = weatherColors.textSecondary
                        )

                        Text(
                            text = alert.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = weatherColors.textSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (alert != alerts.take(2).lastOrNull()) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(weatherColors.textPrimary.copy(alpha = 0.1f))
                    )
                }
            }

            if (alerts.size > 2) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "+${alerts.size - 2} więcej alertów",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4A90E2),
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onViewAllAlerts() }
                )
            }
        }
    }
}