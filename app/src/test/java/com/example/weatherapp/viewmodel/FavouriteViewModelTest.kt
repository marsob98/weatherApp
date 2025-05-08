package com.example.weatherapp.viewmodel

import com.example.weatherapp.data.local.entity.FavouriteEntity
import com.example.weatherapp.domain.repository.FavouriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class FavouriteViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: FavouriteRepository
    private lateinit var viewModel: FavouriteViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(FavouriteRepository::class.java)

        val favourites = listOf(
            FavouriteEntity("Warszawa"),
            FavouriteEntity("Kraków")
        )
        `when`(repository.getFavourites()).thenReturn(flowOf(favourites))

        viewModel = FavouriteViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `init loads favourites from repository`() = testDispatcher.runBlockingTest {
        // Expected favourites already set up in setup()

        // When viewModel is initialized in setup()

        // Then
        assertEquals(2, viewModel.favourites.value.size)
        assertEquals("Warszawa", viewModel.favourites.value[0].cityName)
        assertEquals("Kraków", viewModel.favourites.value[1].cityName)
    }

    @Test
    fun `addFavourite calls repository`() = testDispatcher.runBlockingTest {
        // Given
        val cityName = "Gdańsk"

        // When
        viewModel.addFavourite(cityName)

        // Then
        verify(repository).addFavourite(cityName)
    }

    @Test
    fun `removeFavourite calls repository`() = testDispatcher.runBlockingTest {
        // Given
        val cityName = "Warszawa"

        // When
        viewModel.removeFavourite(cityName)

        // Then
        verify(repository).removeFavourite(cityName)
    }

    @Test
    fun `isFavourite calls repository and returns correct value`() = testDispatcher.runBlockingTest {
        // Given
        val cityName = "Kraków"
        `when`(repository.isFavourite(cityName)).thenReturn(true)

        // When
        var result = false
        viewModel.isFavourite(cityName) { isFound ->
            result = isFound
        }

        // Then
        verify(repository).isFavourite(cityName)
        assertEquals(true, result)
    }
}