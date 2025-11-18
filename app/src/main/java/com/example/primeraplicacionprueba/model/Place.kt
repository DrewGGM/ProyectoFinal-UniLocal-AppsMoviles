package com.example.primeraplicacionprueba.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Place(
    var id: String = "",
    val title: String = "",
    val imagenes: List<String> = emptyList(),
    val description: String = "",
    val phones: List<String> = emptyList(),
    val type: PlaceType = PlaceType.RESTAURANT,
    val shedule: List<Shedule> = emptyList(),
    val location: Location = Location(),
    val adress: String = "",
    val website: String? = null,
    val email: String? = null,
    val socialMedia: String? = null,
    val reviewIds: List<String> = emptyList(), // Changed from List<Review> to List<String>
    val city: String = "",
    val neighborhood: String = "",
    val priceRange: String = "",
    val amenities: List<String> = emptyList(),
    val isVerified: Boolean = false,
    val ownerId: String = "",
    val viewCount: Int = 0,
    val favoriteCount: Int = 0,
    var placeStatus: PlaceStatus = PlaceStatus.PENDING,
    @get:PropertyName("createdDate")
    @set:PropertyName("createdDate")
    var createdDate: Any? = null, // Changed to Any? to handle Timestamp/HashMap
) {
    // Helper to convert createdDate to LocalDateTime
    private fun getCreatedDateAsLocalDateTime(): LocalDateTime {
        return when (createdDate) {
            is Timestamp -> {
                (createdDate as Timestamp).toDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
            is Map<*, *> -> {
                val map = createdDate as Map<*, *>
                val seconds = (map["seconds"] as? Number)?.toLong() ?: return LocalDateTime.now()
                val nanoseconds = (map["nanoseconds"] as? Number)?.toInt() ?: 0
                Timestamp(seconds, nanoseconds).toDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
            else -> LocalDateTime.now()
        }
    }

    fun getFormattedCreatedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return getCreatedDateAsLocalDateTime().format(formatter)
    }

    fun changePlaceStatus(status: PlaceStatus) {
        placeStatus = status
    }

    fun getDistanceFromUser(): String {
        return "5 km"
    }
    
    fun isCurrentlyOpen(): Boolean {
        val now = LocalTime.now()
        val today = DayOfWeek.from(LocalDate.now())
        val todaySchedule = shedule.find { it.day.name == today.name }
        return todaySchedule?.let { now.isAfter(it.open) && now.isBefore(it.close) } ?: false
    }
    
    fun getNextCloseTime(): String {
        val now = LocalTime.now()
        val today = DayOfWeek.from(LocalDate.now())
        val todaySchedule = shedule.find { it.day.name == today.name }
        return todaySchedule?.close?.toString() ?: "N/A"
    }
    
    fun getReviewCount(): Int = reviewIds.size

    // Note: Average rating should be calculated from actual Review objects
    // This is just a placeholder - implement in ViewModel by fetching reviews
    fun getAverageRating(): Float = 0f
    
    fun getFormattedCloseTime(): String {
        val closeTime = getNextCloseTime()
        return if (closeTime != "N/A") {
            try {
                val time = LocalTime.parse(closeTime)
                val hour = time.hour
                val minute = time.minute
                val period = if (hour >= 12) "PM" else "AM"
                val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
                "$displayHour:${String.format("%02d", minute)} $period"
            } catch (e: Exception) {
                closeTime
            }
        } else {
            closeTime
        }
    }
}

data class CreatePlaceState(
    // Step 1: Información básica
    val title: String = "",
    val description: String = "",
    val type: PlaceType? = null,

    // Step 2: Contacto y ubicación
    val phones: List<String> = emptyList(),
    val website: String? = null,
    val socialMedia: String? = null,
    val location: Location? = null,
    val address: String = "",
    val city: String = "",
    val neighborhood: String = "",

    // Step 3: Horarios
    val schedule: List<Shedule> = emptyList(),

    // Step 4: Imágenes
    val images: List<String> = emptyList()
)