// Plik: app/src/main/java/com/example/weatherapp/viewmodel/FavouriteViewModel.kt (cały plik)
package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.entity.FavouriteEntity
import com.example.weatherapp.domain.repository.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: FavouriteRepository
) : ViewModel() {

    private val _favourites = MutableStateFlow<List<FavouriteEntity>>(emptyList())
    val favourites: StateFlow<List<FavouriteEntity>> = _favourites

    private val TAG = "FavouriteViewModel"

    init {
        Log.d(TAG, "Inicjalizacja FavouriteViewModel")
        viewModelScope.launch {
            repository.getFavourites().collect { favourites ->
                Log.d(TAG, "Pobrano ulubione: $favourites")
                _favourites.value = favourites
            }
        }
    }

    fun addFavourite(cityName: String) {
        Log.d(TAG, "Dodawanie miasta do ulubionych: $cityName")
        viewModelScope.launch {
            repository.addFavourite(cityName)
            Log.d(TAG, "Miasto dodane do ulubionych: $cityName")
        }
    }

    fun removeFavourite(cityName: String) {
        Log.d(TAG, "Usuwanie miasta z ulubionych: $cityName")
        viewModelScope.launch {
            repository.removeFavourite(cityName)
            Log.d(TAG, "Miasto usunięte z ulubionych: $cityName")
        }
    }

    fun isFavourite(cityName: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFavourite = repository.isFavourite(cityName)
            Log.d(TAG, "Sprawdzenie czy miasto $cityName jest ulubione: $isFavourite")
            callback(isFavourite)
        }
    }
}