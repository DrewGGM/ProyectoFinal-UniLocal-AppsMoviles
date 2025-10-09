package com.example.primeraplicacionprueba.ui.screens.user.nav

import kotlinx.serialization.Serializable

sealed class RouteTab {
    @Serializable
    data object Home : RouteTab()
    @Serializable
    data object Map : RouteTab()
    @Serializable
    data object Profile : RouteTab()
}