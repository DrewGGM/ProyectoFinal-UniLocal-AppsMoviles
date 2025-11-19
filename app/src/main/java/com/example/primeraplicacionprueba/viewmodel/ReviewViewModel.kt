package com.example.primeraplicacionprueba.viewmodel

import android.util.Log
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Review
import com.example.primeraplicacionprueba.model.ReviewReply
import com.example.primeraplicacionprueba.utils.RequestResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val app: Application = getApplication()
    private val db = FirebaseFirestore.getInstance()

    private val _reviews = MutableStateFlow(emptyList<Review>())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _reviewResult = MutableStateFlow<RequestResult?>(null)
    val reviewResult: StateFlow<RequestResult?> = _reviewResult.asStateFlow()

    private var reviewsListener: ListenerRegistration? = null
    private var usersViewModel: UsersViewModel? = null

    init {
        loadReviews()
    }

    fun setUsersViewModel(usersViewModel: UsersViewModel) {
        this.usersViewModel = usersViewModel
    }

    fun loadReviews() {
        reviewsListener = db.collection("reviews")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ReviewViewModel", "Error loading reviews", error)
                    return@addSnapshotListener
                }

                val reviewsList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Review::class.java)?.apply {
                        this.id = doc.id
                    }
                } ?: emptyList()

                _reviews.value = reviewsList
            }
    }

    fun create(review: Review) {
        viewModelScope.launch {
            _reviewResult.value = RequestResult.Loading
            _reviewResult.value = runCatching { createFirebase(review) }
                .fold(
                    onSuccess = {
                        usersViewModel?.incrementReviewsWritten()
                        RequestResult.Success(message = app.getString(R.string.msg_review_created))
                    },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_creating_review)) }
                )
        }
    }

    private suspend fun createFirebase(review: Review) {
        db.collection("reviews").add(review).await()
    }

    fun update(review: Review) {
        viewModelScope.launch {
            _reviewResult.value = RequestResult.Loading
            _reviewResult.value = runCatching { updateFirebase(review) }
                .fold(
                    onSuccess = { RequestResult.Success(message = app.getString(R.string.msg_review_updated)) },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_updating_review)) }
                )
        }
    }

    private suspend fun updateFirebase(review: Review) {
        if (review.id.isEmpty()) {
            throw Exception(app.getString(R.string.error_invalid_review_id))
        }
        db.collection("reviews")
            .document(review.id)
            .set(review)
            .await()
    }

    fun delete(reviewId: String) {
        viewModelScope.launch {
            _reviewResult.value = RequestResult.Loading
            _reviewResult.value = runCatching { deleteFirebase(reviewId) }
                .fold(
                    onSuccess = { RequestResult.Success(message = app.getString(R.string.msg_review_deleted)) },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_deleting_review)) }
                )
        }
    }

    private suspend fun deleteFirebase(reviewId: String) {
        db.collection("reviews")
            .document(reviewId)
            .delete()
            .await()
    }

    fun addReplyToReview(reviewId: String, reply: ReviewReply) {
        viewModelScope.launch {
            try {
                val replyDoc = db.collection("review_replies").add(reply).await()

                val review = _reviews.value.find { it.id == reviewId }
                if (review != null) {
                    val updatedReplyIds = review.replyIds + replyDoc.id
                    val updatedReview = review.copy(replyIds = updatedReplyIds)
                    updateFirebase(updatedReview)
                }
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "Error adding reply", e)
            }
        }
    }

    fun loadRepliesForReview(reviewId: String, onResult: (List<ReviewReply>) -> Unit) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("review_replies")
                    .whereEqualTo("reviewId", reviewId)
                    .get()
                    .await()

                val replies = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ReviewReply::class.java)?.apply {
                        this.id = doc.id
                    }
                }
                onResult(replies)
            } catch (e: Exception) {
                onResult(emptyList())
            }
        }
    }

    fun getReviewsByPlace(placeID: String): List<Review> {
        return _reviews.value.filter { it.placeID == placeID }
    }

    fun getReviewsByUser(userId: String): List<Review> {
        return _reviews.value.filter { it.userID == userId }
    }

    fun getReviewById(reviewId: String): Review? {
        return _reviews.value.find { it.id == reviewId }
    }

    fun resetOperationResult() {
        _reviewResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        reviewsListener?.remove()
    }
}
