package com.example.primeraplicacionprueba.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Comment(
    var id: String = "",
    val placeId: String = "",
    val userId: String = "",
    val username: String = "",
    val rating: Int = 0,
    val commentText: String = "",
    @get:PropertyName("timestamp")
    @set:PropertyName("timestamp")
    var timestamp: Any? = null, // Changed to Any? to handle Timestamp/HashMap
    val imageUrl: String? = null
) {
    // Helper to convert timestamp to LocalDateTime
    private fun getTimestampAsLocalDateTime(): LocalDateTime {
        return when (timestamp) {
            is Timestamp -> {
                (timestamp as Timestamp).toDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
            is Map<*, *> -> {
                val map = timestamp as Map<*, *>
                val seconds = (map["seconds"] as? Number)?.toLong() ?: return LocalDateTime.now()
                val nanoseconds = (map["nanoseconds"] as? Number)?.toInt() ?: 0
                Timestamp(seconds, nanoseconds).toDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
            else -> LocalDateTime.now()
        }
    }

    fun getFormattedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return getTimestampAsLocalDateTime().format(formatter)
    }

    fun isPositiveRating(): Boolean {
        return rating >= 4
    }

    fun hasImage(): Boolean {
        return !imageUrl.isNullOrEmpty()
    }
}