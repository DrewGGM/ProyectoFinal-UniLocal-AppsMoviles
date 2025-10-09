package com.example.primeraplicacionprueba.model

class User(
    val nombre: String,
    val username: String,
    val city: String,
    val country: String,
    val email: String,
    val password: String,
    val id: String,
    val rol: Rol,
    val imageUrl: String? = null,
    // Estadísticas para achievements
    val placesCreated: Int = 0,
    val placesVisited: Int = 0,
    val reviewsWritten: Int = 0,
    val favoritesAdded: Int = 0,
    val joinDate: String = "", // Fecha de registro
) {
    
    // Métodos para calcular achievements
    fun getTotalPlacesCreated(): Int = placesCreated
    fun getTotalPlacesVisited(): Int = placesVisited
    fun getTotalReviewsWritten(): Int = reviewsWritten
    fun getTotalFavoritesAdded(): Int = favoritesAdded
    
    // Método para obtener días desde el registro
    fun getDaysSinceJoin(): Int {
        return if (joinDate.isNotEmpty()) {
            // Simulación: asumimos que se registró hace 30 días
            30
        } else {
            0
        }
    }
}