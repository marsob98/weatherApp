package com.example.weatherapp.viewmodel.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherapp.utils.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
        // Mockujemy LocationServices
        mockStatic(LocationServices::class.java).use { mockedLocationServices ->
            mockedLocationServices.`when`<FusedLocationProviderClient> {
                LocationServices.getFusedLocationProviderClient(context)
            }.thenReturn(fusedLocationClient)
        }

        // Mockujemy sprawdzanie uprawnień
        mockStatic(ContextCompat::class.java).use { mockedContextCompat ->
            mockedContextCompat.`when`<Int> {
                ContextCompat.checkSelfPermission(any(), any())
            }.thenReturn(PackageManager.PERMISSION_GRANTED)
        }

        locationManager = LocationManager(context)
    }

    @Test
    fun `hasLocationPermission returns true when permissions granted`() {
        // Given
        mockStatic(ContextCompat::class.java).use { mockedContextCompat ->
            mockedContextCompat.`when`<Int> {
                ContextCompat.checkSelfPermission(any(), any())
            }.thenReturn(PackageManager.PERMISSION_GRANTED)

            // When
            val hasPermission = locationManager.hasLocationPermission()

            // Then
            assertTrue(hasPermission)
        }
    }

    @Test
    fun `hasLocationPermission returns false when permissions not granted`() {
        // Given
        mockStatic(ContextCompat::class.java).use { mockedContextCompat ->
            mockedContextCompat.`when`<Int> {
                ContextCompat.checkSelfPermission(any(), any())
            }.thenReturn(PackageManager.PERMISSION_DENIED)

            // When
            val hasPermission = locationManager.hasLocationPermission()

            // Then
            assertFalse(hasPermission)
        }
    }

    @Test
    fun `getLastLocation returns location when available`() = runBlocking {
        // Given
        `when`(fusedLocationClient.lastLocation).thenReturn(locationTask)
        `when`(locationTask.addOnSuccessListener(any())).thenAnswer {
            val listener = it.arguments[0] as com.google.android.gms.tasks.OnSuccessListener<Location>
            listener.onSuccess(location)
            locationTask
        }

        // When
        val result = locationManager.getLastLocation()

        // Then
        assertNotNull(result)
        assertEquals(location, result)
    }

    @Test
    fun `getLastLocation returns null when permission not granted`() = runBlocking {
        // Given
        mockStatic(ContextCompat::class.java).use { mockedContextCompat ->
            mockedContextCompat.`when`<Int> {
                ContextCompat.checkSelfPermission(any(), any())
            }.thenReturn(PackageManager.PERMISSION_DENIED)

            // When
            val result = locationManager.getLastLocation()

            // Then
            assertNull(result)
        }
    }
}