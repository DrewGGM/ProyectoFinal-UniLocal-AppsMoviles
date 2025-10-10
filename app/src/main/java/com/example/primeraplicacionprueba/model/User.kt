package com.example.primeraplicacionprueba.model

import java.time.LocalDate

data class User(
    val nombre: String,
    val username: String,
    val city: String,
    val country: String,
    val email: String,
    val password: String,
    val id: String,
    val rol: Role,
    val imageUrl: String? = null,
    val favorites: List<Place> = emptyList(),
    // Estadísticas para achievements
    val placesCreated: Int = 0,
    val placesVisited: Int = 0,
    val reviewsWritten: Int = 0,
    val favoritesAdded: Int = 0,
    val joinDate: LocalDate,
    val isActive: Boolean = true
) {
    
    // Métodos para calcular achievements
    fun getTotalPlacesCreated(): Int = placesCreated
    fun getTotalPlacesVisited(): Int = placesVisited
    fun getTotalReviewsWritten(): Int = reviewsWritten
    fun getTotalFavoritesAdded(): Int = favoritesAdded
    
    // Método para obtener días desde el registro
    fun getDaysSinceJoin(): Int {
        return LocalDate.now().compareTo(joinDate)
    }


}