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
import com.ivor.openanime.presentation.player.PlayerScreen
import com.ivor.openanime.presentation.search.SearchScreen
import com.ivor.openanime.presentation.watch_history.WatchHistoryScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object History : Screen("history")
    data object Details : Screen("details/{mediaType}/{animeId}") {
        fun createRoute(mediaType: String, animeId: Int) = "details/$mediaType/$animeId"
    }
    data object Player : Screen("player/{mediaType}/{animeId}/{season}/{episode}") {
        fun createRoute(mediaType: String, animeId: Int, season: Int, episode: Int) = "player/$mediaType/$animeId/$season/$episode"
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
                    navController.navigate(Screen.Details.createRoute("tv", animeId))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onAnimeClick = { animeId, mediaType ->
                    navController.navigate(Screen.Details.createRoute(mediaType, animeId))
                }
            )
        }

        composable(Screen.History.route) {
            WatchHistoryScreen(
                onBackClick = { navController.popBackStack() },
                onAnimeClick = { animeId, mediaType ->
                    navController.navigate(Screen.Details.createRoute(mediaType, animeId))
                }
            )
        }
        
        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument("mediaType") { type = NavType.StringType },
                navArgument("animeId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mediaType = backStackEntry.arguments?.getString("mediaType") ?: "tv"
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: return@composable
            DetailsScreen(
                mediaType = mediaType,
                onBackClick = { navController.popBackStack() },
                onPlayClick = { season, episode ->
                    navController.navigate(Screen.Player.createRoute(mediaType, animeId, season, episode))
                }
            )
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument("mediaType") { type = NavType.StringType },
                navArgument("animeId") { type = NavType.IntType },
                navArgument("season") { type = NavType.IntType },
                navArgument("episode") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mediaType = backStackEntry.arguments?.getString("mediaType") ?: "tv"
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: return@composable
            val season = backStackEntry.arguments?.getInt("season") ?: return@composable
            val episode = backStackEntry.arguments?.getInt("episode") ?: return@composable
            
            PlayerScreen(
                mediaType = mediaType,
                tmdbId = animeId,
                season = season,
                episode = episode,
                onBackClick = { navController.popBackStack() },
                onEpisodeClick = { newEpisode ->
                    navController.navigate(Screen.Player.createRoute(mediaType, animeId, season, newEpisode)) {
                        // Pop up to the current player screen to avoid deep stacking
                        popUpTo(Screen.Player.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
