package com.example.primeraplicacionprueba.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class Place(
    val id: String,
    val title: String,
    val imagenes: List<String>,
    val description: String,
    val phones: List<String>,
    val type: PlaceType,
    val shedule: List<Shedule>,
    val location: Location,
    val adress: String,
    val website: String? = null,
    val email: String? = null,
    val socialMedia: String? = null,
    val reviews: List<Review> = emptyList(),
    val city: String = "",
    val neighborhood: String = "",
    val priceRange: String = "",
    val amenities: List<String> = emptyList(),
    val isVerified: Boolean = false,
    val ownerId: String = "",
    val viewCount: Int = 0,
    val favoriteCount: Int = 0,
    val placeStatus: PlaceStatus = PlaceStatus.PENDING,
    val createdDate: LocalDate,
) {
    fun getDistanceFromUser(): String {
        return "5 km"
    }
    
    fun isCurrentlyOpen(): Boolean {
        val now = LocalTime.now()
        val today = DayOfWeek.from(java.time.LocalDate.now())
        val todaySchedule = shedule.find { it.day.name == today.name }
        return todaySchedule?.let { now.isAfter(it.open) && now.isBefore(it.close) } ?: false
    }
    
    fun getNextCloseTime(): String {
        val now = LocalTime.now()
        val today = DayOfWeek.from(java.time.LocalDate.now())
        val todaySchedule = shedule.find { it.day.name == today.name }
        return todaySchedule?.close?.toString() ?: "N/A"
    }
    
    fun getReviewCount(): Int = reviews.size
    
    fun getAverageRating(): Float = if (reviews.isNotEmpty()) {
        reviews.map { it.rating }.average().toFloat()
    } else 0f
    
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