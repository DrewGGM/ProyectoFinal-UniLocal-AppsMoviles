package com.example.primeraplicacionprueba.ui.screens.user

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.screens.user.bottombar.BottomBarUser
import com.example.primeraplicacionprueba.ui.screens.user.nav.ContentUser

@Composable
fun UserScreen(
    onNavigateToCreatePlace: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToPlace: (String) -> Unit = {},
    user: User
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomBarUser(navController = navController)
        }
    ) { paddingValues ->
        ContentUser(
            padding = paddingValues,
            navController = navController,
            onNavigateToCreatePlace = onNavigateToCreatePlace,
            onNavigateToEditProfile = onNavigateToEditProfile,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToPlace = onNavigateToPlace,
            user = user
        )
    }
}
