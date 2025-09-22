package com.example.primeraplicacionprueba.ui.config

import kotlinx.serialization.Serializable

sealed class RouteScreem{
    @Serializable
    data object HomeUser: RouteScreem()
    @Serializable
    data object HomeAdmin: RouteScreem()
    @Serializable
    data object Login: RouteScreem()
    @Serializable
    data object Register: RouteScreem()

}