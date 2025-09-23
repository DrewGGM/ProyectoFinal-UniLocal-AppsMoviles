package com.example.primeraplicacionprueba.ui.screens.user.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.Home
import com.example.primeraplicacionprueba.ui.screens.user.tabs.map.Map
import com.example.primeraplicacionprueba.ui.screens.user.tabs.profile.Profile

@Composable
fun ContentUser(
    padding: PaddingValues,
    navController: NavHostController,
    onNavigateToCreatePlace: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
){

    NavHost(
        modifier = Modifier.padding(padding),
        navController=navController,
        startDestination = RouteTab.Home
    ){
        composable <RouteTab.Home>{
            Home(onNavigateToCreatePlace = onNavigateToCreatePlace)
        }
        composable <RouteTab.Map>{
            Map()
        }
        composable <RouteTab.Profile>{
            Profile(
                onNavigateToEditProfile = onNavigateToEditProfile,
                onNavigateToLogin = onNavigateToLogin
            )
        }
    }
}