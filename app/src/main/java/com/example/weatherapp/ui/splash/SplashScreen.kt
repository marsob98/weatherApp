package com.example.weatherapp.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    // Animacje
    val scale = remember {
        Animatable(0.3f)
    }

    val alpha = remember {
        Animatable(0f)
    }

    // Tło z gradientem
    val colors = listOf(
        Color(0xFF1A237E), // Ciemny niebieski
        Color(0xFF0D47A1), // Niebieski
        Color(0xFF42A5F5)  // Jasny niebieski
    )

    LaunchedEffect(key1 = true) {
        // Sekwencja animacji
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 700,
                easing = FastOutSlowInEasing
            )
        )

        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = LinearEasing
            )
        )

        delay(1800)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .size(160.dp * scale.value)
                    .scale(scale.value)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                // Używamy istniejącej ikony
                Image(
                    painter = painterResource(id = R.drawable.ic_sun_cloud),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nazwa aplikacji
            Text(
                text = "Sky Check",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                modifier = Modifier.scale(scale.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tagline
            Text(
                text = "Sprawdź pogodę w mgnieniu oka!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = alpha.value),
                modifier = Modifier.scale(alpha.value)
            )
        }
    }
}