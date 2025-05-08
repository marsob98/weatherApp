package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.local.dao.FavouriteDao
import com.example.weatherapp.data.local.entity.FavouriteEntity
import com.example.weatherapp.util.MockLog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*

class FavouriteRepositoryTest {

    private lateinit var favouriteDao: FavouriteDao
    private lateinit var repository: FavouriteRepository

    @Before
    fun setup() {
        // Mockowanie klasy Log
        MockLog.mock()

        favouriteDao = mock(FavouriteDao::class.java)
        repository = FavouriteRepository(favouriteDao)
    }

    @Test
    fun getFavourites_returnsCorrectData() = runBlocking {
        // Given
        val expectedFavourites = listOf(
            FavouriteEntity("Warszawa"),
            FavouriteEntity("Kraków")
        )
        `when`(favouriteDao.getFavourites()).thenReturn(flowOf(expectedFavourites))

        // When
        val result = repository.getFavourites().first()

        // Then
        assertEquals(expectedFavourites, result)
    }

    @Test
    fun addFavourite_callsDao() = runBlocking {
        // Given
        val cityName = "Wrocław"

        // When
        repository.addFavourite(cityName)

        // Then
        verify(favouriteDao).insertFavourite(any(FavouriteEntity::class.java))
    }

    @Test
    fun removeFavourite_callsDao() = runBlocking {
        // Given
        val cityName = "Gdańsk"

        // When
        repository.removeFavourite(cityName)

        // Then
        verify(favouriteDao).deleteFavourite(any(FavouriteEntity::class.java))
    }

    @Test
    fun isFavourite_returnsCorrectValue() = runBlocking {
        // Given
        val cityName = "Poznań"
        `when`(favouriteDao.isFavourite(cityName)).thenReturn(true)

        // When
        val result = repository.isFavourite(cityName)

        // Then
        assertTrue(result)
        verify(favouriteDao).isFavourite(cityName)
    }
}