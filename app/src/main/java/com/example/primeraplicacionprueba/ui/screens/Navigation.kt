package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.primeraplicacionprueba.ui.config.RouteScreem
import com.example.primeraplicacionprueba.ui.screens.admin.HomeAdmin
import com.example.primeraplicacionprueba.ui.screens.user.UserScreen
import com.example.primeraplicacionprueba.ui.screens.user.tabs.PlaceDetail
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.AddNewPlace
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.viewmodel.UsersViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val usersViewModel: UsersViewModel = viewModel()
    val placesViewModel: PlacesViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = RouteScreem.Login
    ) {
        composable<RouteScreem.Login> {
            LoginScreen(
                usersViewModel = usersViewModel,
                onNavigateToRegister = {
                    navController.navigate(RouteScreem.Register)
                },
                onNavigateToHome = { user ->
                    navController.navigate(RouteScreem.Home)
                }
            )
        }
        composable<RouteScreem.Register> {
            RegisterScreem(
                usersViewModel = usersViewModel,
                onNavigateToLogin = {
                    navController.navigate(RouteScreem.Login)
                },
                onNavigateToHome = {
                    navController.navigate(RouteScreem.Home)
                }
            )
        }
        composable<RouteScreem.Home> {
            val currentUser by usersViewModel.currentUser.collectAsState()
            currentUser?.let { user ->
                UserScreen(
                    onNavigateToCreatePlace = {
                        navController.navigate(RouteScreem.CreatePlace)
                    },
                    onNavigateToEditProfile = {
                        navController.navigate(RouteScreem.EditProfile)
                    },
                    onNavigateToLogin = {
                        usersViewModel.logout()
                        navController.navigate(RouteScreem.Login) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToPlace = { placeId ->
                        navController.navigate(RouteScreem.PlaceDetail(placeId))
                    },
                    user = user
                )
            }
        }
        composable<RouteScreem.HomeAdmin> {
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
                onNavigateToHome = {
                    navController.navigate(RouteScreem.Home) {
                        popUpTo(RouteScreem.Home) { inclusive = false }
                    }
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

        composable<RouteScreem.PlaceDetail> {
            val args = it.toRoute<RouteScreem.PlaceDetail>()
            PlaceDetail(
                placesViewModel = placesViewModel,
                id = args.id,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}