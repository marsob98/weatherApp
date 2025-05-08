package com.example.weatherapp.viewmodel.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.example.weatherapp.util.MockLog
import com.example.weatherapp.utils.NetworkUtils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
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
        // Mockowanie klasy Log
        MockLog.mock()

        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetwork).thenReturn(network)
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(networkCapabilities)
    }

    @Test
    fun isNetworkAvailable_returnsTrue_forWiFi() {
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
    fun isNetworkAvailable_returnsTrue_forCellular() {
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
    fun isNetworkAvailable_returnsFalse_whenNoNetworkAvailable() {
        // Given
        `when`(connectivityManager.activeNetwork).thenReturn(null)

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    fun isNetworkAvailable_returnsFalse_whenNoNetworkCapabilitiesAvailable() {
        // Given
        `when`(connectivityManager.getNetworkCapabilities(network)).thenReturn(null)

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    fun isNetworkAvailable_returnsFalse_whenNoTransportAvailable() {
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