package com.example.primeraplicacionprueba.viewmodel

import androidx.lifecycle.ViewModel
import com.example.primeraplicacionprueba.model.Comment
import com.example.primeraplicacionprueba.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

class CommentsViewModel : ViewModel(){
    private val _comments = MutableStateFlow(emptyList<Comment>())
    val places: StateFlow<List<Comment>> = _comments.asStateFlow()

    init {
        loadComments()
    }
    fun loadComments() {
        _comments.value = listOf(
            Comment(
                id = "c1",
                placeId = "p1",
                userId = "1",
                username = "juanca",
                rating = 5,
                commentText = "Excelente lugar, el ambiente es muy tranquilo y el café tiene un sabor espectacular. Perfecto para trabajar o leer un rato.",
                timestamp = LocalDateTime.now(),
                imageUrl = "https://plus.unsplash.com/premium_photo-1670984940113-f3aa1cd1309a?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8cmVzdGF1cmFudGVzfGVufDB8fDB8fHww"
            ),
            Comment(
                id = "c2",
                placeId = "p1",
                userId = "1",
                username = "juanca",
                rating = 4,
                commentText = "Muy buen sitio, la atención fue amable y los postres deliciosos. Solo recomendaría ampliar un poco el horario los fines de semana.",
                timestamp = LocalDateTime.now(),
                imageUrl = "https://plus.unsplash.com/premium_photo-1670984940113-f3aa1cd1309a?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8cmVzdGF1cmFudGVzfGVufDB8fDB8fHww"
            ),
            Comment(
                id = "c3",
                placeId = "p1",
                userId = "1",
                username = "juanca",
                rating = 3,
                commentText = "El lugar es bonito, pero estaba un poco lleno cuando fui. La música era agradable y el café bastante bueno.",
                timestamp = LocalDateTime.now(),
                imageUrl = "https://plus.unsplash.com/premium_photo-1670984940113-f3aa1cd1309a?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8cmVzdGF1cmFudGVzfGVufDB8fDB8fHww"
            )
        )
    }

    fun findById(id: String): Comment? {
        return _comments.value.find { it.id == id }
    }

    fun findByPlaceId(placeId: String): List<Comment> {
        return _comments.value.filter { it.placeId == placeId }

    }
    fun findByUserId(userId: String): List<Comment> {
        return _comments.value.filter { it.userId == userId }
    }
    fun findByUserPlaceId(placeId: String, userId: String): List<Comment> {
        return _comments.value.filter { it.placeId == placeId && it.userId == userId }
    }

    fun create(comment: Comment) {
        _comments.value = _comments.value + comment
    }
}