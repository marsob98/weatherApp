package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.local.dao.FavouriteDao
import com.example.weatherapp.data.local.entity.FavouriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouriteRepository @Inject constructor(
    private val favouriteDao: FavouriteDao
) {
    fun getFavourites(): Flow<List<FavouriteEntity>> {
        return favouriteDao.getFavourites()
    }

    suspend fun addFavourite(cityName: String) {
        favouriteDao.insertFavourite(FavouriteEntity(cityName))
    }

    suspend fun removeFavourite(cityName: String) {
        favouriteDao.deleteFavourite(FavouriteEntity(cityName))
    }

    suspend fun isFavourite(cityName: String): Boolean {
        return favouriteDao.isFavourite(cityName)
    }
}