package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.primeraplicacionprueba.model.Rol
import com.example.primeraplicacionprueba.ui.config.RouteScreen
import com.example.primeraplicacionprueba.ui.screens.admin.HomeAdmin
import com.example.primeraplicacionprueba.ui.screens.user.UserScreen
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.CreatePlaceStepFour
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.CreatePlaceStepOne
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.CreatePlaceStepThree
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.CreatePlaceStepTwo
import com.example.primeraplicacionprueba.ui.screens.user.tabs.map.Map
import com.example.primeraplicacionprueba.ui.screens.user.tabs.map.filtro.FilterBusqueda
import com.example.primeraplicacionprueba.ui.screens.user.tabs.profile.AchievementsScreen
import com.example.primeraplicacionprueba.utils.SharedPrefsUtil
import com.example.primeraplicacionprueba.viewmodel.MainViewModel

val LocalMainViewModel =
    staticCompositionLocalOf<MainViewModel> { error("No MainViewModel provided") }

@Composable
fun Navigation(
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val user = SharedPrefsUtil.getPreferences(context)

    val startDestination = if (user["userId"]?.isEmpty() == true) {
        RouteScreen.Login
    } else {
        if (user["role"] == "ADMIN") {
            RouteScreen.HomeAdmin
        } else {
            RouteScreen.Home
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        CompositionLocalProvider(
            LocalMainViewModel provides mainViewModel
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable<RouteScreen.Login> {
                    LoginScreen(
                        onNavigateToRegister = {
                            navController.navigate(RouteScreen.Register)
                        },
                        onNavigateToHome = { user ->
                            SharedPrefsUtil.savePreferences(
                                context,
                                user.id,
                                user.rol.name,
                                user.nombre,
                                user.email
                            )

                            if (user.rol == Rol.ADMIN) {
                                navController.navigate(RouteScreen.HomeAdmin)
                            } else {
                                navController.navigate(RouteScreen.Home)
                            }
                        }
                    )
                }
                composable<RouteScreen.Register> {
                    RegisterScreem(
                        onNavigateToLogin = {
                            navController.navigate(RouteScreen.Login)
                        },
                        onNavigateToHome = { user ->
                            SharedPrefsUtil.savePreferences(
                                context,
                                user.id,
                                user.rol.name,
                                user.nombre,
                                user.email
                            )
                            navController.navigate(RouteScreen.Home)
                        }
                    )
                }
                composable<RouteScreen.Home> {
                    val mainViewModel = LocalMainViewModel.current
                    val usersViewModel = mainViewModel.usersViewModel
                    val currentUser by usersViewModel.currentUser.collectAsState()
                    currentUser?.let { user ->
                        UserScreen(
                            onNavigateToCreatePlace = {
                                navController.navigate(RouteScreen.CreatePlaceStepOne)
                            },
                            onNavigateToEditProfile = {
                                navController.navigate(RouteScreen.EditProfile)
                            },
                            onNavigateToLogin = {
                                usersViewModel.logout()
                                SharedPrefsUtil.clearPreferences(context)
                                navController.navigate(RouteScreen.Login) {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToPlace = { placeId ->
                                navController.navigate(RouteScreen.PlaceDetail(placeId))
                            },
                            onNavigateToAchievements = {
                                navController.navigate(RouteScreen.Achievements)
                            },
                            onNavigateToFilter = {
                                navController.navigate(RouteScreen.FilterBusqueda)
                            },
                            user = user
                        )
                    }
                }
                composable<RouteScreen.HomeAdmin> {
                    HomeAdmin()
                }

                composable<RouteScreen.EditProfile> {
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

                composable<RouteScreen.FilterBusqueda> {
                    FilterBusqueda(
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                    )
                }
                composable<RouteScreen.Map> {
                    Map(
                        onMapToFilter = {
                            navController.navigate(RouteScreen.FilterBusqueda)
                        }
                    )
                }


                composable<RouteScreen.CreatePlaceStepOne> {
                    CreatePlaceStepOne(
                        onNavigateToHome = {
                            navController.navigate(RouteScreen.Home) {
                                popUpTo(RouteScreen.Home) { inclusive = false }
                            }
                        },
                        onNavigateToNext = {
                            // Navegar al siguiente paso
                            navController.navigate(RouteScreen.CreatePlaceStepTwo)
                        }
                    )
                }

                composable<RouteScreen.CreatePlaceStepTwo> {
                    CreatePlaceStepTwo(
                        onNavigateToHome = {
                            navController.navigate(RouteScreen.Home) {
                                popUpTo(RouteScreen.Home) { inclusive = false }
                            }
                        },
                        onNavigateToPrevious = {
                            // Navegar al paso anterior
                            navController.popBackStack()
                        },
                        onNavigateToNext = {
                            // Navegar al siguiente paso
                            navController.navigate(RouteScreen.CreatePlaceStepThree)
                        }
                    )
                }

                composable<RouteScreen.CreatePlaceStepThree> {
                    CreatePlaceStepThree(
                        onNavigateToHome = {
                            navController.navigate(RouteScreen.Home) {
                                popUpTo(RouteScreen.Home) { inclusive = false }
                            }
                        },
                        onNavigateToPrevious = {
                            // Navegar al paso anterior
                            navController.popBackStack()
                        },
                        onNavigateToNext = {
                            // Navegar al siguiente paso
                            navController.navigate(RouteScreen.CreatePlaceStepFour)
                        }
                    )
                }

                composable<RouteScreen.CreatePlaceStepFour> {
                    CreatePlaceStepFour(
                        onNavigateToHome = {
                            navController.navigate(RouteScreen.Home) {
                                popUpTo(RouteScreen.Home) { inclusive = false }
                            }
                        },
                        onNavigateToPrevious = {
                            // Navegar al paso anterior
                            navController.popBackStack()
                        }
                    )
                }

                composable<RouteScreen.PlaceDetail> {
                    val args = it.toRoute<RouteScreen.PlaceDetail>()
                    val mainViewModel = LocalMainViewModel.current
                    val placesViewModel = mainViewModel.placesViewModel
                    PlaceDetail(
                        placesViewModel = placesViewModel,
                        id = args.id,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable<RouteScreen.Achievements> {
                    val mainViewModel = LocalMainViewModel.current
                    val usersViewModel = mainViewModel.usersViewModel
                    val achievementViewModel = mainViewModel.achievementViewModel
                    val currentUser by usersViewModel.currentUser.collectAsState()
                    currentUser?.let { user ->
                        AchievementsScreen(
                            user = user,
                            achievementViewModel = achievementViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }

            }
        }
    }
}