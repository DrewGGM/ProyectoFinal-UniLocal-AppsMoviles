package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.primeraplicacionprueba.ui.config.RouteScreem
import androidx.navigation.compose.composable
import com.example.primeraplicacionprueba.ui.screens.admin.HomeAdmin
import com.example.primeraplicacionprueba.ui.screens.user.HomeUser

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RouteScreem.HomeUser
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
    }
}