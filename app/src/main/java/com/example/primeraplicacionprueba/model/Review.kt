package com.example.primeraplicacionprueba.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.time.LocalDateTime
import java.time.ZoneId

data class Review(
    var id: String = "",
    val userID: String = "",
    val username: String = "",
    val placeID: String = "",
    val rating: Int = 0,
    val comment: String = "",
    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Any? = null, // Changed to Any? to handle Timestamp/HashMap
    val replyIds: List<String> = emptyList() // Changed from List<ReviewReply> to IDs
) {
    // Helper to convert date to LocalDateTime
    fun getDateAsLocalDateTime(): LocalDateTime {
        return when (date) {
            is Timestamp -> {
                (date as Timestamp).toDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
            is Map<*, *> -> {
                val map = date as Map<*, *>
                val seconds = (map["seconds"] as? Number)?.toLong() ?: return LocalDateTime.now()
                val nanoseconds = (map["nanoseconds"] as? Number)?.toInt() ?: 0
                Timestamp(seconds, nanoseconds).toDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
            else -> LocalDateTime.now()
        }
    }
}

data class ReviewReply(
    var id: String = "",
    val reviewId: String = "",
    val userID: String = "",
    val username: String = "",
    val replyText: String = "",
    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Any? = null
) {
    // Helper to convert date to LocalDateTime
    fun getDateAsLocalDateTime(): LocalDateTime {
        return when (date) {
            is Timestamp -> {
                (date as Timestamp).toDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
            is Map<*, *> -> {
                val map = date as Map<*, *>
                val seconds = (map["seconds"] as? Number)?.toLong() ?: return LocalDateTime.now()
                val nanoseconds = (map["nanoseconds"] as? Number)?.toInt() ?: 0
                Timestamp(seconds, nanoseconds).toDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
            else -> LocalDateTime.now()
        }
    }
}