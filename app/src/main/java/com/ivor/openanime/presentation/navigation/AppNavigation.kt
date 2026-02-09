package com.ivor.openanime.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ivor.openanime.presentation.details.DetailsScreen
import com.ivor.openanime.presentation.home.HomeScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Details : Screen("details/{animeId}") {
        fun createRoute(animeId: Int) = "details/$animeId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.Details.createRoute(animeId))
                }
            )
        }
        
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: return@composable
            DetailsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
