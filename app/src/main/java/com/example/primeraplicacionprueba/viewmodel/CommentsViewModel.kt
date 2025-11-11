package com.example.primeraplicacionprueba.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primeraplicacionprueba.model.Comment
import com.example.primeraplicacionprueba.utils.RequestResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CommentsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _comments = MutableStateFlow(emptyList<Comment>())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _commentResult = MutableStateFlow<RequestResult?>(null)
    val commentResult: StateFlow<RequestResult?> = _commentResult.asStateFlow()

    private var commentsListener: ListenerRegistration? = null

    init {
        loadComments()
    }

    // ========== FIREBASE CRUD OPERATIONS ==========

    fun loadComments() {
        // Real-time listener for comments from Firebase
        commentsListener = db.collection("comments")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CommentsViewModel", "Error loading comments", error)
                    return@addSnapshotListener
                }

                val commentsList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Comment::class.java)?.apply {
                        this.id = doc.id
                    }
                } ?: emptyList()

                _comments.value = commentsList
            }
    }

    fun create(comment: Comment) {
        viewModelScope.launch {
            _commentResult.value = RequestResult.Loading
            _commentResult.value = runCatching { createFirebase(comment) }
                .fold(
                    onSuccess = { RequestResult.Success(message = "Comentario creado exitosamente") },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: "Error creando el Comentario") }
                )
        }
    }

    private suspend fun createFirebase(comment: Comment) {
        db.collection("comments").add(comment).await()
    }

    fun update(comment: Comment) {
        viewModelScope.launch {
            _commentResult.value = RequestResult.Loading
            _commentResult.value = runCatching { updateFirebase(comment) }
                .fold(
                    onSuccess = { RequestResult.Success(message = "Comentario actualizado correctamente") },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: "Error actualizando el Comentario") }
                )
        }
    }

    private suspend fun updateFirebase(comment: Comment) {
        if (comment.id.isEmpty()) {
            throw Exception("ID de comentario no válido")
        }
        db.collection("comments")
            .document(comment.id)
            .set(comment)
            .await()
    }

    fun delete(commentId: String) {
        viewModelScope.launch {
            _commentResult.value = RequestResult.Loading
            _commentResult.value = runCatching { deleteFirebase(commentId) }
                .fold(
                    onSuccess = { RequestResult.Success(message = "Comentario eliminado correctamente") },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: "Error eliminando el Comentario") }
                )
        }
    }

    private suspend fun deleteFirebase(commentId: String) {
        db.collection("comments")
            .document(commentId)
            .delete()
            .await()
    }

    // ========== QUERIES ==========

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

    fun resetOperationResult() {
        _commentResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        commentsListener?.remove()
    }
}
