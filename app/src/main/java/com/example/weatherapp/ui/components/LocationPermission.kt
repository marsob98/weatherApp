// Plik: app/src/main/java/com/example/weatherapp/ui/components/LocationPermission.kt

package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(
    permissions: List<String>,
    onPermissionGranted: () -> Unit,
    content: @Composable () -> Unit
) {
    val permissionsState = rememberMultiplePermissionsState(permissions) {
        val allGranted = it.all { permission -> permission.value }
        if (allGranted) {
            onPermissionGranted()
        }
    }

    if (permissionsState.allPermissionsGranted) {
        content()
    } else {
        PermissionRequest(
            permissionsState = permissionsState,
            onRequestPermission = { permissionsState.launchMultiplePermissionRequest() }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequest(
    permissionsState: MultiplePermissionsState,
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textToShow = if (permissionsState.shouldShowRationale) {
            "Lokalizacja jest potrzebna, aby pokazać pogodę dla Twojej aktualnej lokalizacji."
        } else {
            "Zezwól na dostęp do lokalizacji, aby zobaczyć pogodę w Twojej okolicy."
        }

        Text(
            text = textToShow,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRequestPermission,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Zezwól na dostęp")
        }
    }
}