package com.example.weatherapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.ui.Screen
import com.example.weatherapp.ui.WeatherNavigation
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: NavHostController

    @Test
    fun splashScreenNavigatesToHomeScreen() {
        // Given
        composeTestRule.setContent {
            navController = rememberNavController()
            WeatherNavigation()
        }

        // Wait for splash screen animation
        composeTestRule.waitUntil(3000L) {
            // Check if navigation occurred
            navController.currentDestination?.route == Screen.Home.route
        }

        // Then
        assertEquals(Screen.Home.route, navController.currentDestination?.route)
    }
}