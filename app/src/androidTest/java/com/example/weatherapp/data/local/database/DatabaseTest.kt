
package com.example.weatherapp.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.local.dao.FavouriteDao
import com.example.weatherapp.data.local.entity.FavouriteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTest {
    private lateinit var favouriteDao: FavouriteDao
    private lateinit var db: WeatherDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, WeatherDatabase::class.java
        ).build()
        favouriteDao = db.favouriteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFavourite() = runBlocking {
        // Given
        val favourite = FavouriteEntity("Wrocław")

        // When
        favouriteDao.insertFavourite(favourite)
        val favourites = favouriteDao.getFavourites().first()

        // Then
        assertEquals(1, favourites.size)
        assertEquals("Wrocław", favourites[0].cityName)
    }

    @Test
    @Throws(Exception::class)
    fun deleteFavourite() = runBlocking {
        // Given
        val favourite = FavouriteEntity("Wrocław")
        favouriteDao.insertFavourite(favourite)

        // When
        favouriteDao.deleteFavourite(favourite)
        val favourites = favouriteDao.getFavourites().first()

        // Then
        assertEquals(0, favourites.size)
    }

    @Test
    @Throws(Exception::class)
    fun isFavourite() = runBlocking {
        // Given
        val favourite = FavouriteEntity("Wrocław")
        favouriteDao.insertFavourite(favourite)

        // When
        val isWroclawFavourite = favouriteDao.isFavourite("Wrocław")
        val isKrakowFavourite = favouriteDao.isFavourite("Kraków")

        // Then
        assertTrue(isWroclawFavourite)
        assertFalse(isKrakowFavourite)
    }
}