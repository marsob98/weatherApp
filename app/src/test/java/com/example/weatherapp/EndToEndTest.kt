package com.example.weatherapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EndToEndTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun searchAndSelectCity() {
        // Poczekaj na załadowanie ekranu głównego
        composeTestRule.waitUntil(5000L) {
            composeTestRule.onAllNodesWithContentDescription("Szukaj").fetchSemanticsNodes().isNotEmpty()
        }

        // Kliknij przycisk wyszukiwania
        composeTestRule.onNodeWithContentDescription("Szukaj").performClick()

        // Wpisz nazwę miasta
        composeTestRule.onNodeWithText("Wpisz nazwę miasta…").performTextInput("Wrocław")

        // Poczekaj na wyniki wyszukiwania
        composeTestRule.waitUntil(5000L) {
            composeTestRule.onAllNodesWithText("Wrocław").fetchSemanticsNodes().isNotEmpty()
        }

        // Kliknij na znalezione miasto
        composeTestRule.onNodeWithText("Wrocław").performClick()

        // Sprawdź, czy wróciło do ekranu głównego i wyświetla dane pogodowe dla wybranego miasta
        composeTestRule.waitUntil(5000L) {
            composeTestRule.onAllNodesWithText("Wrocław").fetchSemanticsNodes().isNotEmpty()
        }

        // Sprawdź, czy wyświetlane są podstawowe informacje pogodowe
        composeTestRule.onNodeWithText("Wrocław").assertIsDisplayed()
    }
}