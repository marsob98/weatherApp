package com.example.weatherapp.viewmodel.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherapp.util.MockLog
import com.example.weatherapp.utils.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocationManagerTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Mock
    private lateinit var locationTask: Task<Location>

    @Mock
    private lateinit var location: Location

    private lateinit var locationManager: LocationManager

    @Before
    fun setup() {
        // Mockowanie klasy Log
        MockLog.mock()

        // Mockujemy LocationServices
        mockStatic(LocationServices::class.java).use { mockedLocationServices ->
            mockedLocationServices.`when`<FusedLocationProviderClient> {
                LocationServices.getFusedLocationProviderClient(any(Context::class.java))
            }.thenReturn(fusedLocationClient)
        }

        // Mockujemy sprawdzanie uprawnień
        mockStatic(ContextCompat::class.java).use { mockedContextCompat ->
            mockedContextCompat.`when`<Int> {
                ContextCompat.checkSelfPermission(any(Context::class.java), anyString())
            }.thenReturn(PackageManager.PERMISSION_GRANTED)
        }

        // Konfiguracja mocka fusedLocationClient
        `when`(fusedLocationClient.lastLocation).thenReturn(locationTask)
        doAnswer { invocation ->
            val listener = invocation.arguments[0] as OnSuccessListener<Location>
            listener.onSuccess(location)
            locationTask
        }.`when`(locationTask).addOnSuccessListener(any(OnSuccessListener::class.java))

        locationManager = LocationManager(context)
    }

    @Test
    fun hasLocationPermission_returnsTrue_whenPermissionsGranted() {
        // Given - już skonfigurowane w setup()

        // When
        val hasPermission = locationManager.hasLocationPermission()

        // Then
        assertTrue(hasPermission)
    }

    @Test
    fun hasLocationPermission_returnsFalse_whenPermissionsNotGranted() {
        // Given
        mockStatic(ContextCompat::class.java).use { mockedContextCompat ->
            mockedContextCompat.`when`<Int> {
                ContextCompat.checkSelfPermission(any(Context::class.java), anyString())
            }.thenReturn(PackageManager.PERMISSION_DENIED)

            // When
            val hasPermission = locationManager.hasLocationPermission()

            // Then
            assertFalse(hasPermission)
        }
    }

    @Test
    fun getLastLocation_returnsLocation_whenAvailable() = runBlocking {
        // Given - już skonfigurowane w setup()

        // When
        val result = locationManager.getLastLocation()

        // Then
        assertNotNull(result)
        assertEquals(location, result)
    }

    @Test
    fun getLastLocation_returnsNull_whenPermissionNotGranted() = runBlocking {
        // Given
        mockStatic(ContextCompat::class.java).use { mockedContextCompat ->
            mockedContextCompat.`when`<Int> {
                ContextCompat.checkSelfPermission(any(Context::class.java), anyString())
            }.thenReturn(PackageManager.PERMISSION_DENIED)

            // When
            val result = locationManager.getLastLocation()

            // Then
            assertNull(result)
        }
    }
}