package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteEntity(
    @PrimaryKey
    val cityName: String,
    val timestamp: Long = System.currentTimeMillis()
)