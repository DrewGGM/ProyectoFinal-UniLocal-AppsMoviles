package com.example.primeraplicacionprueba.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Comment(
    val id: String,
    val placeId: String,
    val userId: String,
    val username: String,
    val rating: Int,
    val commentText: String,
    val timestamp: LocalDateTime,
    val imageUrl: String? = null

) {
    fun getFormattedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return timestamp.format(formatter)
    }
    fun isPositiveRating(): Boolean {
        return rating >= 4
    }
    
    fun hasImage(): Boolean {
        return !imageUrl.isNullOrEmpty()
    }



}