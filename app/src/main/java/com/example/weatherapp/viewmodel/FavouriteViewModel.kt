package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.entity.FavouriteEntity
import com.example.weatherapp.domain.repository.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: FavouriteRepository
) : ViewModel() {

    private val _favourites = MutableStateFlow<List<FavouriteEntity>>(emptyList())
    val favourites: StateFlow<List<FavouriteEntity>> = _favourites

    init {
        viewModelScope.launch {
            repository.getFavourites().collect { favourites ->
                _favourites.value = favourites
            }
        }
    }

    fun addFavourite(cityName: String) {
        viewModelScope.launch {
            repository.addFavourite(cityName)
        }
    }

    fun removeFavourite(cityName: String) {
        viewModelScope.launch {
            repository.removeFavourite(cityName)
        }
    }

    fun isFavourite(cityName: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFavourite = repository.isFavourite(cityName)
            callback(isFavourite)
        }
    }
}