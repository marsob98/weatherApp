package com.example.weatherapp.ui.splash

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_displaysCorrectContent() {
        // Given
        var navigated = false
        val onNavigateToHome = { navigated = true }

        // When
        composeTestRule.setContent {
            SplashScreen(onNavigateToHome = onNavigateToHome)
        }

        // Then
        composeTestRule.onNodeWithText("Sky Check").assertExists()
        composeTestRule.onNodeWithText("Sprawdź pogodę w mgnieniu oka!").assertExists()

        // Verify navigation after the delay
        composeTestRule.waitUntil(3000L) { navigated }
        assert(navigated)
    }
}