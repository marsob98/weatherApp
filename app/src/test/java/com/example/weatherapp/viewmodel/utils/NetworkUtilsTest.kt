package com.example.weatherapp.viewmodel.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import com.example.weatherapp.utils.NetworkUtils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(sdk = [Build.VERSION_CODES.M]) // Testujemy na API 23+
class NetworkUtilsTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    @Mock
    private lateinit var network: Network

    @Mock
    private lateinit var networkCapabilities: NetworkCapabilities

    @Before
    fun setup() {
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetwork).thenReturn(network)
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(networkCapabilities)
    }

    @Test
    fun `isNetworkAvailable returns true for WiFi`() {
        // Given
        `when`(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(true)
        `when`(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(false)
        `when`(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)).thenReturn(false)

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertTrue(result)
    }

    @Test
    fun `isNetworkAvailable returns true for Cellular`() {
        // Given
        `when`(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(false)
        `when`(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(true)
        `when`(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)).thenReturn(false)

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertTrue(result)
    }

    @Test
    fun `isNetworkAvailable returns false when no network available`() {
        // Given
        `when`(connectivityManager.activeNetwork).thenReturn(null)

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    fun `isNetworkAvailable returns false when no network capabilities available`() {
        // Given
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(null)

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    fun `isNetworkAvailable returns false when no transport available`() {
        // Given
        `when`(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(false)
        `when`(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(false)
        `when`(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)).thenReturn(false)

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }
}