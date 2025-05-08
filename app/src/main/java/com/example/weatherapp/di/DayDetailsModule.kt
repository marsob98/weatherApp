package com.example.weatherapp.di

import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.ui.details.DayDetailsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Moduł Hilt dla ekranu szczegółów dnia
 */
@Module
@InstallIn(ViewModelComponent::class)
object DayDetailsModule {

    @Provides
    @ViewModelScoped
    fun provideDayDetailsViewModel(
        weatherRepository: WeatherRepository
    ): DayDetailsViewModel {
        return DayDetailsViewModel(weatherRepository)
    }
}