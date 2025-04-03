package com.example.weatherapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.local.dao.FavouriteDao
import com.example.weatherapp.data.local.entity.FavouriteEntity

@Database(entities = [FavouriteEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
}