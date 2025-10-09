package com.example.primeraplicacionprueba.ui.config

import kotlinx.serialization.Serializable

sealed class RouteScreen{
    @Serializable
    data object Home : RouteScreen()
    @Serializable
    data object HomeAdmin: RouteScreen()
    @Serializable
    data object Login: RouteScreen()
    @Serializable
    data object Register: RouteScreen()

    @Serializable
    data object EditProfile: RouteScreen();

    @Serializable
    data object CreatePlaceStepOne: RouteScreen();

    @Serializable
    data object CreatePlaceStepTwo: RouteScreen();

    @Serializable
    data object CreatePlaceStepThree: RouteScreen();

    @Serializable
    data object CreatePlaceStepFour: RouteScreen();

    @Serializable
    data class PlaceDetail(val id: String): RouteScreen();

    @Serializable
    data object Achievements: RouteScreen();
}