package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.primeraplicacionprueba.ui.config.RouteScreem
import androidx.navigation.compose.composable
import com.example.primeraplicacionprueba.ui.screens.admin.HomeAdmin
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.AddNewPlace
import com.example.primeraplicacionprueba.ui.screens.user.UserScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RouteScreem.Home
    ){
        composable<RouteScreem.Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(RouteScreem.Register)
                },
                onNavigateToHome = {
                    navController.navigate(RouteScreem.Home)
                }
            )
        }
        composable<RouteScreem.Register> {
            RegisterScreem(
                onNavigateToLogin = {
                    navController.navigate(RouteScreem.Login)
                },
                onNavigateToHome = {
                    navController.navigate(RouteScreem.Home)
                }
            )
        }
        composable<RouteScreem.Home>{
            UserScreen(
                onNavigateToCreatePlace = {
                    navController.navigate(RouteScreem.CreatePlace)
                },
                onNavigateToEditProfile = {
                    navController.navigate(RouteScreem.EditProfile)
                },
                onNavigateToLogin = {
                    // Limpiar el back stack y navegar al login
                    navController.navigate(RouteScreem.Login) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<RouteScreem.HomeAdmin>{
            HomeAdmin()
        }

        composable<RouteScreem.EditProfile> {
            EditProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()  // Regresa a la pantalla anterior
                },
                onSaveChanges = {
                    // Lógica para guardar cambios y regresar
                    navController.popBackStack()
                }
            )
        }

        composable<RouteScreem.CreatePlace> {
            AddNewPlace(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPrevious = {
                    // Navegar al paso anterior (puedes crear otra pantalla o simplemente regresar)
                    navController.popBackStack()
                },
                onNavigateToNext = {
                    // Navegar al siguiente paso (puedes crear la siguiente pantalla)
                    // Por ahora solo muestra un mensaje o navega a otra pantalla
                    navController.navigate(RouteScreem.Home)
                }
            )
        }

    }
}