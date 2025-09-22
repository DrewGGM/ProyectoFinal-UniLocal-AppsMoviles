package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.primeraplicacionprueba.ui.config.RouteScreem
import androidx.navigation.compose.composable
import com.example.primeraplicacionprueba.ui.screens.admin.HomeAdmin
import com.example.primeraplicacionprueba.ui.screens.user.AddNewPlace
import com.example.primeraplicacionprueba.ui.screens.user.HomeUser

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RouteScreem.CreatePlace
    ){
        composable<RouteScreem.Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(RouteScreem.Register)
                },
                onNavigateToHome = {
                    navController.navigate(RouteScreem.HomeUser)
                }
            )
        }
        composable<RouteScreem.Register> {
            RegisterScreem(
                onNavigateToLogin = {
                    navController.navigate(RouteScreem.Login)
                },
                onNavigateToHome = {
                    navController.navigate(RouteScreem.HomeUser)
                }
            )
        }
        composable<RouteScreem.HomeUser>{
            HomeUser()
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
                    navController.navigate(RouteScreem.HomeUser)
                }
            )
        }

    }
}