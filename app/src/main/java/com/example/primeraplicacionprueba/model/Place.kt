package com.example.primeraplicacionprueba.model

import java.time.DayOfWeek
import java.time.LocalTime

class Place(
    val id: String,
    val title: String,
    val imagenes: List<String>,
    val description: String,
    val phones: List<String>,
    val type: PlaceType,
    val shedule: List<Shedule>,
    val location: Location,
    val rating: Float,
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
    val favoriteCount: Int = 0
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
    } else rating
    
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