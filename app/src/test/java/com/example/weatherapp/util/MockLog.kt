package com.example.weatherapp.util

import android.util.Log
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

/**
 * Klasa pomocnicza do mockowania klasy Log w testach jednostkowych
 */
object MockLog {
    /**
     * Mockuje wszystkie statyczne metody klasy Log
     */
    fun mock() {
        try {
            Mockito.mockStatic(Log::class.java).use { mockedLog ->
                mockedLog.`when`<Int> {
                    Log.d(anyString(), anyString())
                }.thenReturn(0)

                mockedLog.`when`<Int> {
                    Log.e(anyString(), anyString())
                }.thenReturn(0)

                mockedLog.`when`<Int> {
                    Log.i(anyString(), anyString())
                }.thenReturn(0)

                mockedLog.`when`<Int> {
                    Log.v(anyString(), anyString())
                }.thenReturn(0)

                mockedLog.`when`<Int> {
                    Log.w(anyString(), anyString())
                }.thenReturn(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}