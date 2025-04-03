package com.example.weatherapp.data.local.dao

import androidx.room.*
import com.example.weatherapp.data.local.entity.FavouriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourites ORDER BY timestamp DESC")
    fun getFavourites(): Flow<List<FavouriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: FavouriteEntity)

    @Delete
    suspend fun deleteFavourite(favourite: FavouriteEntity)

    @Query("DELETE FROM favourites")
    suspend fun deleteAllFavourites()

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE cityName = :cityName LIMIT 1)")
    suspend fun isFavourite(cityName: String): Boolean
}