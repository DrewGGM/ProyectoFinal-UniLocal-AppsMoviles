package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.primeraplicacionprueba.model.Role
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.Location
import com.example.primeraplicacionprueba.ui.config.RouteScreen
import java.time.LocalDate
import com.example.primeraplicacionprueba.ui.screens.admin.HomeAdmin
import com.example.primeraplicacionprueba.ui.screens.admin.DetailPlaceAdmin
import com.example.primeraplicacionprueba.ui.screens.admin.MyPerfilAdmin
import com.example.primeraplicacionprueba.ui.screens.user.UserScreen
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.CreatePlaceStepFour
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.CreatePlaceStepOne
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.CreatePlaceStepThree
import com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace.CreatePlaceStepTwo
import com.example.primeraplicacionprueba.ui.screens.user.tabs.map.Map
import com.example.primeraplicacionprueba.ui.screens.user.tabs.map.filtro.FilterBusqueda
import com.example.primeraplicacionprueba.ui.screens.user.tabs.profile.AchievementsScreen
import com.example.primeraplicacionprueba.ui.screens.user.tabs.profile.FavoritesScreen
import com.example.primeraplicacionprueba.ui.screens.user.tabs.profile.MyPlacesScreen
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

                            if(user.rol == Role.ADMIN) {
                                navController.navigate(RouteScreen.HomeAdmin)
                            } else {
                                navController.navigate(RouteScreen.Home)
                            }
                        },
                        onNavigateToForgotPassword = {
                            navController.navigate(RouteScreen.ForgotPassword)
                        }
                    )
                }
            composable<RouteScreen.Register> {
                RegisterScreen(
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
                                navController.navigate(RouteScreen.CreatePlaceStepOne())
                            },
                            onNavigateToEditProfile = {
                                navController.navigate(RouteScreen.EditProfile)
                            },
                            onNavigateToLogin = {
                                usersViewModel.logout(context)
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
                            onNavigateToMyPlaces = {
                                navController.navigate(RouteScreen.MyPlaces)
                            },
                            onNavigateToFavorites = {
                                navController.navigate(RouteScreen.Favorites)
                            },
                            onNavigateToFilter = {
                                navController.navigate(RouteScreen.FilterBusqueda)
                            },
                           user = user
                        )
                    }
                }
                composable<RouteScreen.HomeAdmin> {
                    val mainViewModel = LocalMainViewModel.current
                    val placesViewModel = mainViewModel.placesViewModel
                    HomeAdmin(
                        placesViewModel = placesViewModel,
                        onNavigateToDetail = { placeId ->
                            navController.navigate(RouteScreen.AdminPlaceDetail(placeId))
                        },
                        onNavigateToProfile = {
                            navController.navigate(RouteScreen.MyPerfilAdmin)
                        }
                    )
                }

                composable<RouteScreen.AdminPlaceDetail> {
                    val args = it.toRoute<RouteScreen.AdminPlaceDetail>()
                    val mainViewModel = LocalMainViewModel.current
                    val placesViewModel = mainViewModel.placesViewModel
                    DetailPlaceAdmin(
                        id = args.id,
                        placesViewModel = placesViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable<RouteScreen.MyPerfilAdmin> {
                    val mainViewModel = LocalMainViewModel.current
                    val usersViewModel = mainViewModel.usersViewModel
                    val placesViewModel = mainViewModel.placesViewModel
                    val currentUser by usersViewModel.currentUser.collectAsState()
                    currentUser?.let { user ->
                        MyPerfilAdmin(
                            user = user,
                            placesViewModel = placesViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }

                composable<RouteScreen.MyComments> {
                    val args = it.toRoute<RouteScreen.MyComments>()
                    val mainViewModel = LocalMainViewModel.current
                    val placesViewModel = mainViewModel.placesViewModel
                    val reviewViewModel = mainViewModel.reviewsViewModel
                    MyCommentScreen(
                        placeId = args.placeId,
                        placesViewModel = placesViewModel,
                        reviewViewModel = reviewViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
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
                    val args = it.toRoute<RouteScreen.CreatePlaceStepOne>()
                    val mainViewModel = LocalMainViewModel.current
                    val placesViewModel = mainViewModel.placesViewModel

                    // Si hay placeId, cargar el lugar para editar
                    LaunchedEffect(args.placeId) {
                        args.placeId?.let { id ->
                            placesViewModel.loadPlaceForEdit(id)
                        }
                    }

                    CreatePlaceStepOne(
                        viewModel = placesViewModel,
                        onNavigateToHome = {
                            placesViewModel.resetCreateState()
                            navController.navigate(RouteScreen.Home) {
                                popUpTo(RouteScreen.Home) { inclusive = false }
                            }
                        },
                        onNavigateToNext = {
                            navController.navigate(RouteScreen.CreatePlaceStepTwo(args.placeId))
                        }
                    )
                }

                composable<RouteScreen.CreatePlaceStepTwo> {
                    val args = it.toRoute<RouteScreen.CreatePlaceStepTwo>()
                    val mainViewModel = LocalMainViewModel.current
                    val placesViewModel = mainViewModel.placesViewModel
                    CreatePlaceStepTwo(
                        viewModel = placesViewModel,
                        onNavigateToHome = {
                            placesViewModel.resetCreateState()
                            navController.navigate(RouteScreen.Home) {
                                popUpTo(RouteScreen.Home) { inclusive = false }
                            }
                        },
                        onNavigateToPrevious = {
                            navController.popBackStack()
                        },
                        onNavigateToNext = {
                            navController.navigate(RouteScreen.CreatePlaceStepThree(args.placeId))
                        }
                    )
                }

                composable<RouteScreen.CreatePlaceStepThree> {
                    val args = it.toRoute<RouteScreen.CreatePlaceStepThree>()
                    val mainViewModel = LocalMainViewModel.current
                    val placesViewModel = mainViewModel.placesViewModel
                    CreatePlaceStepThree(
                        viewModel = placesViewModel,
                        onNavigateToHome = {
                            placesViewModel.resetCreateState()
                            navController.navigate(RouteScreen.Home) {
                                popUpTo(RouteScreen.Home) { inclusive = false }
                            }
                        },
                        onNavigateToPrevious = {
                            navController.popBackStack()
                        },
                        onNavigateToNext = {
                            navController.navigate(RouteScreen.CreatePlaceStepFour(args.placeId))
                        }
                    )
                }

                composable<RouteScreen.CreatePlaceStepFour> {
                    val args = it.toRoute<RouteScreen.CreatePlaceStepFour>()
                    val mainViewModel = LocalMainViewModel.current
                    val placesViewModel = mainViewModel.placesViewModel
                    val usersViewModel = mainViewModel.usersViewModel
                    val currentUser by usersViewModel.currentUser.collectAsState()
                    CreatePlaceStepFour(
                        viewModel = placesViewModel,
                        onNavigateToHome = {
                            placesViewModel.resetCreateState()
                            navController.navigate(RouteScreen.Home) {
                                popUpTo(RouteScreen.Home) { inclusive = false }
                            }
                        },
                        onNavigateToPrevious = {
                            navController.popBackStack()
                        },
                        onSubmit = {
                            val state = placesViewModel.createPlaceState.value

                            if (args.placeId != null) {
                                // Modo edición: actualizar lugar existente
                                val updatedPlace = Place(
                                    id = args.placeId,
                                    title = state.title,
                                    imagenes = if (state.images.isEmpty()) emptyList() else state.images,
                                    description = state.description,
                                    phones = if (state.phones.isEmpty()) listOf("N/A") else state.phones,
                                    type = state.type!!,
                                    shedule = state.schedule,
                                    location = state.location ?: Location(0.0, 0.0),
                                    adress = if (state.address.isBlank()) "No especificado" else state.address,
                                    website = state.website,
                                    email = null,
                                    socialMedia = state.socialMedia,
                                    city = currentUser?.city ?: "No especificado",
                                    neighborhood = if (state.neighborhood.isBlank()) "No especificado" else state.neighborhood,
                                    ownerId = currentUser?.id ?: "",
                                    createdDate = LocalDate.now()
                                )
                                placesViewModel.updatePlace(updatedPlace)
                            } else {
                                // Modo creación: crear nuevo lugar
                                val newPlace = Place(
                                    id = (placesViewModel.places.value.size + 1).toString(),
                                    title = state.title,
                                    imagenes = if (state.images.isEmpty()) emptyList() else state.images,
                                    description = state.description,
                                    phones = if (state.phones.isEmpty()) listOf("N/A") else state.phones,
                                    type = state.type!!,
                                    shedule = state.schedule,
                                    location = state.location ?: Location(0.0, 0.0),
                                    adress = if (state.address.isBlank()) "No especificado" else state.address,
                                    website = state.website,
                                    email = null,
                                    socialMedia = state.socialMedia,
                                    city = currentUser?.city ?: "No especificado",
                                    neighborhood = if (state.neighborhood.isBlank()) "No especificado" else state.neighborhood,
                                    ownerId = currentUser?.id ?: "",
                                    createdDate = LocalDate.now()
                                )
                                placesViewModel.create(newPlace)
                            }

                            placesViewModel.resetCreateState()
                            navController.navigate(RouteScreen.Home) {
                                popUpTo(RouteScreen.Home) { inclusive = false }
                            }
                        }
                    )
                }

                composable<RouteScreen.PlaceDetail> {
                    val args = it.toRoute<RouteScreen.PlaceDetail>()
                    val mainViewModel = LocalMainViewModel.current
                    val placesViewModel = mainViewModel.placesViewModel
                    val reviewViewModel = mainViewModel.reviewsViewModel
                    PlaceDetail(
                        placesViewModel = placesViewModel,
                        reviewViewModel = reviewViewModel,
                        id = args.id,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToComment = { placeId ->
                            navController.navigate(RouteScreen.CommentScream(placeId))
                        }
                    )

                }
                composable<RouteScreen.CommentScream> {
                    val args = it.toRoute<RouteScreen.CommentScream>()
                    val mainViewModel = LocalMainViewModel.current
                    val reviewViewModel = mainViewModel.reviewsViewModel
                    val usersViewModel = mainViewModel.usersViewModel
                    val currentUser by usersViewModel.currentUser.collectAsState()
                    currentUser?.let { user ->
                        CommentScreen(
                            placeId = args.id,
                            userId = user.id,
                            username = user.username,
                            reviewViewModel = reviewViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
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

                composable<RouteScreen.MyPlaces> {
                    val mainViewModel = LocalMainViewModel.current
                    val usersViewModel = mainViewModel.usersViewModel
                    val placesViewModel = mainViewModel.placesViewModel
                      val currentUser by usersViewModel.currentUser.collectAsState()
                      val allPlaces by placesViewModel.places.collectAsState()
                      val places = remember(allPlaces, currentUser) {
                          allPlaces.filter { it.ownerId == (currentUser?.id ?: "") }
                      }
                    currentUser?.let { user ->
                        MyPlacesScreen(
                            places = places,
                            onNavigateBack= {
                                navController.popBackStack()
                            },
                            onAddPlace= {
                                navController.navigate(RouteScreen.CreatePlaceStepOne())
                            },
                            onEditPlace= { placeId ->
                                navController.navigate(RouteScreen.CreatePlaceStepOne(placeId))
                            },
                            onDeletePlace= { placeId ->
                                // TODO: Implementar eliminación de lugar
                                placesViewModel.deletePlace(placeId)
                            },
                            onViewComments= { placeId ->
                                navController.navigate(RouteScreen.MyComments(placeId))
                            },
                        )
                    }
                }

                composable<RouteScreen.Favorites> {
                    val mainViewModel = LocalMainViewModel.current
                    val usersViewModel = mainViewModel.usersViewModel
                    val currentUser by usersViewModel.currentUser.collectAsState()
                    val favorites = currentUser?.favorites ?: emptyList()
                    currentUser?.let { user ->
                        FavoritesScreen(
                            favorites= favorites,
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onPlaceClick= { placeId ->
                                navController.navigate(RouteScreen.PlaceDetail(placeId))
                            },
                            onRemoveFavorite= { placeId ->
                                // TODO: Implementar remoción de favorito
                                usersViewModel.removeFavorite(placeId)
                            }
                        )
                    }
                }

                composable<RouteScreen.ForgotPassword> {
                    ForgotPasswordScreen(
                        onNavigateToLogin = {
                            navController.popBackStack()
                        }
                    )
                }

            }
        }
    }
}