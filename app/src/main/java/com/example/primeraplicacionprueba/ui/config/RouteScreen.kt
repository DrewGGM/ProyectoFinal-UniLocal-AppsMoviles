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
    data class CreatePlaceStepOne(val placeId: String? = null): RouteScreen();

    @Serializable
    data class CreatePlaceStepTwo(val placeId: String? = null): RouteScreen();

    @Serializable
    data class CreatePlaceStepThree(val placeId: String? = null): RouteScreen();

    @Serializable
    data class CreatePlaceStepFour(val placeId: String? = null): RouteScreen();

    @Serializable
    data class PlaceDetail(val id: String): RouteScreen();

    @Serializable
    data object Achievements: RouteScreen();

    @Serializable
    data object MyPlaces: RouteScreen();

    @Serializable
    data object Favorites: RouteScreen();

    @Serializable
    data object ForgotPassword: RouteScreen();
  
   @Serializable
    data object FilterBusqueda: RouteScreen();

    @Serializable
    data object Map: RouteScreen();

    @Serializable
    data class CommentScream(val id: String): RouteScreen();


    @Serializable
    data class AdminPlaceDetail(val id: String): RouteScreen();

    @Serializable
    data object MyPerfilAdmin: RouteScreen();

    @Serializable
    data class MyComments(val placeId: String): RouteScreen();

}