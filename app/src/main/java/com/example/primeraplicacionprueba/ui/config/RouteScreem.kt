package com.example.primeraplicacionprueba.ui.config

import kotlinx.serialization.Serializable

sealed class RouteScreem{
    @Serializable
    data object Home : RouteScreem()
    @Serializable
    data object HomeAdmin: RouteScreem()
    @Serializable
    data object Login: RouteScreem()
    @Serializable
    data object Register: RouteScreem()

    @Serializable
    data object EditProfile: RouteScreem();

    @Serializable
    data object CreatePlace: RouteScreem();

    @Serializable
    data class PlaceDetail(val id: String): RouteScreem();
}