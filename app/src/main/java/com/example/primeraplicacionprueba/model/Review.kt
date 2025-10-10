package com.example.primeraplicacionprueba.model

import java.time.LocalDateTime

data class Review(
    val id: String,
    val userID: String,
    val username: String,
    val placeID: String,
    val rating: Int,
    val comment: String,
    val date: LocalDateTime,
    val replies: List<ReviewReply> = emptyList()
)

data class ReviewReply(
    val id: String,
    val reviewId: String,
    val userID: String,
    val username: String,
    val replyText: String,
    val date: LocalDateTime
)