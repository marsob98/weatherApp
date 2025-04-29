// Plik: app/src/main/java/com/example/weatherapp/ui/WeatherNavigation.kt
package com.example.weatherapp.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherapp.ui.alerts.AlertsScreen
import com.example.weatherapp.ui.details.DayDetailsScreen
import com.example.weatherapp.ui.favorites.FavoritesScreen
import com.example.weatherapp.ui.home.HomeScreen
import com.example.weatherapp.ui.search.SearchScreen
import com.example.weatherapp.ui.splash.SplashScreen
import com.example.weatherapp.viewmodel.WeatherViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object DayDetails : Screen("day_details/{date}") {
        fun createRoute(date: Long): String = "day_details/$date"
    }
    object Alerts : Screen("alerts")
}

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    // Współdzielony ViewModel na poziomie nawigacji
    val weatherViewModel: WeatherViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                weatherViewModel = weatherViewModel,
                navController = navController,
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route)
                },
                onNavigateToFavorites = {
                    navController.navigate(Screen.Favorites.route)
                },
                onNavigateToDayDetails = { date ->
                    navController.navigate(Screen.DayDetails.createRoute(date))
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                weatherViewModel = weatherViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                weatherViewModel = weatherViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.DayDetails.route,
            arguments = listOf(navArgument("date") { type = NavType.LongType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getLong("date") ?: 0L
            DayDetailsScreen(
                date = date,
                forecastItems = weatherViewModel.forecastState.value?.list ?: emptyList(),
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Alerts.route) {
            AlertsScreen(
                alerts = weatherViewModel.alertsState.value,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}