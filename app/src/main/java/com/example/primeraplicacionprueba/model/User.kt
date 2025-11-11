package com.example.primeraplicacionprueba.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.time.LocalDate
import java.time.ZoneId

data class User(
    val nombre: String = "",
    val username: String = "",
    val city: String = "",
    val country: String = "",
    val email: String = "",
    val password: String = "",
    var id: String = "",
    val rol: Role = Role.USER,
    val imageUrl: String? = null,
    val favoriteIds: List<String> = emptyList(), // Changed: now stores only IDs
    // Estadísticas para achievements
    val placesCreated: Int = 0,
    val placesVisited: Int = 0,
    val reviewsWritten: Int = 0,
    val favoritesAdded: Int = 0,
    @get:PropertyName("joinDate")
    @set:PropertyName("joinDate")
    var joinDate: Any? = null, // Changed to Any? to handle both Timestamp and HashMap
    val isActive: Boolean = true
) {

    // Constructor secundario para mantener compatibilidad con código existente
    constructor(
        nombre: String,
        username: String,
        city: String,
        country: String,
        email: String,
        password: String,
        id: String,
        rol: Role,
        imageUrl: String? = null,
        favorites: List<Place> = emptyList(),
        placesCreated: Int = 0,
        placesVisited: Int = 0,
        reviewsWritten: Int = 0,
        favoritesAdded: Int = 0,
        joinDate: LocalDate,
        isActive: Boolean = true
    ) : this(
        nombre = nombre,
        username = username,
        city = city,
        country = country,
        email = email,
        password = password,
        id = id,
        rol = rol,
        imageUrl = imageUrl,
        favoriteIds = favorites.map { it.id }, // Convert Place objects to IDs
        placesCreated = placesCreated,
        placesVisited = placesVisited,
        reviewsWritten = reviewsWritten,
        favoritesAdded = favoritesAdded,
        joinDate = Timestamp(java.util.Date.from(joinDate.atStartOfDay(ZoneId.systemDefault()).toInstant())) as Any,
        isActive = isActive
    )

    // Métodos para calcular achievements
    fun getTotalPlacesCreated(): Int = placesCreated
    fun getTotalPlacesVisited(): Int = placesVisited
    fun getTotalReviewsWritten(): Int = reviewsWritten
    fun getTotalFavoritesAdded(): Int = favoritesAdded

    // Método para obtener días desde el registro
    fun getDaysSinceJoin(): Int {
        return getJoinDateAsTimestamp()?.let {
            val joinLocalDate = it.toDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            LocalDate.now().compareTo(joinLocalDate)
        } ?: 0
    }

    // Método helper para obtener joinDate como LocalDate
    fun getJoinDateAsLocalDate(): LocalDate {
        return getJoinDateAsTimestamp()?.toDate()?.toInstant()
            ?.atZone(ZoneId.systemDefault())
            ?.toLocalDate() ?: LocalDate.now()
    }

    // Helper method to convert joinDate (Any?) to Timestamp
    private fun getJoinDateAsTimestamp(): Timestamp? {
        return when (joinDate) {
            is Timestamp -> joinDate as Timestamp
            is com.google.firebase.Timestamp -> joinDate as Timestamp
            is Map<*, *> -> {
                // Handle HashMap from Firebase
                val map = joinDate as Map<*, *>
                val seconds = (map["seconds"] as? Number)?.toLong() ?: return null
                val nanoseconds = (map["nanoseconds"] as? Number)?.toInt() ?: 0
                Timestamp(seconds, nanoseconds)
            }
            else -> null
        }
    }
}