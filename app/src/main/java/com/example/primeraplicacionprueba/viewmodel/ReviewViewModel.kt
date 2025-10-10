package com.example.primeraplicacionprueba.viewmodel

import androidx.lifecycle.ViewModel
import com.example.primeraplicacionprueba.model.Review
import com.example.primeraplicacionprueba.model.ReviewReply
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

class ReviewViewModel: ViewModel() {

    private val _reviews = MutableStateFlow(emptyList<Review>())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    init {
        loadReviews()
    }

    fun getReviewsByPlace(placeID: String): List<Review> {
        return _reviews.value.filter { it.placeID == placeID }
    }

    fun create(review: Review) {
        _reviews.value = _reviews.value + review
    }
    
    fun addReplyToReview(reviewId: String, reply: ReviewReply) {
        _reviews.value = _reviews.value.map { review ->
            if (review.id == reviewId) {
                review.copy(replies = review.replies + reply)
            } else {
                review
            }
        }
    }
    
    fun getReviewById(reviewId: String): Review? {
        return _reviews.value.find { it.id == reviewId }
    }

    fun loadReviews() {
        _reviews.value = listOf(
            Review(
                id = "r1",
                userID = "1",
                username = "Juan Camilo",
                placeID = "p1",
                rating = 5,
                comment = "Una experiencia increíble. La comida deliciosa y el servicio excepcional. ¡Totalmente recomendado!",
                date = LocalDateTime.now().minusDays(2),
                replies = listOf(
                    ReviewReply(
                        id = "reply1",
                        reviewId = "r1",
                        userID = "owner1",
                        username = "Café Central",
                        replyText = "¡Muchas gracias por tu comentario! Nos alegra saber que disfrutaste tu visita.",
                        date = LocalDateTime.now().minusDays(1)
                    )
                )
            ),
            Review(
                id = "r2",
                userID = "2",
                username = "Andrew",
                placeID = "p1",
                rating = 4,
                comment = "Gran lugar para pasar la tarde. El café es excelente y el ambiente muy acogedor. Volveré pronto.",
                date = LocalDateTime.now().minusDays(5)
            ),
            Review(
                id = "r3",
                userID = "1",
                username = "Juan Camilo",
                placeID = "p2",
                rating = 5,
                comment = "Las mejores carnes de la ciudad. El ambiente es perfecto para una cena romántica.",
                date = LocalDateTime.now().minusDays(1)
            ),
            Review(
                id = "r4",
                userID = "2",
                username = "Andrew",
                placeID = "p2",
                rating = 4,
                comment = "Excelente calidad de carne y buen servicio. Un poco caro pero vale la pena.",
                date = LocalDateTime.now().minusDays(3)
            ),
            Review(
                id = "r5",
                userID = "1",
                username = "Juan Camilo",
                placeID = "p2",
                rating = 3,
                comment = "Buen lugar pero la espera fue muy larga. La comida estuvo bien.",
                date = LocalDateTime.now().minusDays(7)
            )
        )
    }
}