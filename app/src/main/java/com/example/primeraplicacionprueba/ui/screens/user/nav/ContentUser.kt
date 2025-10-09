package com.example.primeraplicacionprueba.ui.screens.user.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.Home
import com.example.primeraplicacionprueba.ui.screens.user.tabs.map.Map
import com.example.primeraplicacionprueba.ui.screens.user.tabs.profile.Profile
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel

@Composable
fun ContentUser(
    padding: PaddingValues,
    navController: NavHostController,
    onNavigateToCreatePlace: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToPlace: (String) -> Unit = {},
    onNavigateToAchievements: () -> Unit = {},
    user: User
) {

    val placesViewModel: PlacesViewModel = viewModel()

    NavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = RouteTab.Home
    ) {
        composable<RouteTab.Home> {
            Home(
                placesViewModel = placesViewModel,
                onNavigateToCreatePlace = onNavigateToCreatePlace,
                onNavigateToPlace = onNavigateToPlace,
                user = user
            )
        }
        composable<RouteTab.Map> {
            Map()
        }
        composable<RouteTab.Profile> {
            Profile(
                onNavigateToEditProfile = onNavigateToEditProfile,
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToAchievements = onNavigateToAchievements
            )
        }
    }
}