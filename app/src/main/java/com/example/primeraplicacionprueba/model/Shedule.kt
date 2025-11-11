package com.example.primeraplicacionprueba.model

import java.time.LocalTime

data class Shedule(
    val day: Day = Day.MONDAY,
    val openHour: String = "09:00", // Changed from LocalTime to String (HH:mm format)
    val closeHour: String = "18:00"  // Changed from LocalTime to String (HH:mm format)
) {
    // Helper methods to convert to LocalTime
    val open: LocalTime
        get() = try {
            LocalTime.parse(openHour)
        } catch (e: Exception) {
            LocalTime.of(9, 0)
        }

    val close: LocalTime
        get() = try {
            LocalTime.parse(closeHour)
        } catch (e: Exception) {
            LocalTime.of(18, 0)
        }
}