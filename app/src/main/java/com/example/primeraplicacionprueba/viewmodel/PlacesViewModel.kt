package com.example.primeraplicacionprueba.viewmodel

import android.util.Log
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.primeraplicacionprueba.model.CreatePlaceState
import com.example.primeraplicacionprueba.model.MapFilters
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.PlaceStatus
import com.example.primeraplicacionprueba.model.Review
import com.example.primeraplicacionprueba.utils.RequestResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PlacesViewModel(application: Application) : AndroidViewModel(application) {

    private val app: Application = getApplication()
    private val db = FirebaseFirestore.getInstance()

    private val _places = MutableStateFlow(emptyList<Place>())
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    private val _placeResult = MutableStateFlow<RequestResult?>(null)
    val placeResult: StateFlow<RequestResult?> = _placeResult.asStateFlow()

    private val _activeFilters = MutableStateFlow<MapFilters?>(null)
    private val _filteredPlaces = MutableStateFlow(emptyList<Place>())
    val filteredPlaces: StateFlow<List<Place>> = _filteredPlaces.asStateFlow()

    private val _createPlaceState = MutableStateFlow(CreatePlaceState())
    val createPlaceState: StateFlow<CreatePlaceState> = _createPlaceState.asStateFlow()

    private var reviewViewModel: ReviewViewModel? = null
    private var usersViewModel: UsersViewModel? = null
    private var placesListener: ListenerRegistration? = null

    init {
        loadPlaces()
    }

    fun setReviewViewModel(reviewViewModel: ReviewViewModel) {
        this.reviewViewModel = reviewViewModel
    }

    fun setUsersViewModel(usersViewModel: UsersViewModel) {
        this.usersViewModel = usersViewModel
    }

    fun getReviewsForPlace(placeId: String): List<Review> {
        return reviewViewModel?.getReviewsByPlace(placeId) ?: emptyList()
    }

    fun getAverageRatingForPlace(placeId: String): Float {
        val reviews = getReviewsForPlace(placeId)
        return if (reviews.isNotEmpty()) {
            reviews.map { it.rating }.average().toFloat()
        } else 0f
    }

    fun getReviewCountForPlace(placeId: String): Int {
        return getReviewsForPlace(placeId).size
    }

    fun loadPlaces() {
        placesListener = db.collection("places")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("PlacesViewModel", "Error loading places", error)
                    return@addSnapshotListener
                }

                val placesList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Place::class.java)?.apply {
                        this.id = doc.id
                    }
                } ?: emptyList()

                _places.value = placesList
                applyFilters(_activeFilters.value)
            }
    }

    fun create(place: Place) {
        viewModelScope.launch {
            _placeResult.value = RequestResult.Loading
            _placeResult.value = runCatching { createFirebase(place) }
                .fold(
                    onSuccess = {
                        usersViewModel?.incrementPlacesCreated()
                        RequestResult.Success(message = app.getString(R.string.msg_place_created))
                    },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_creating_place)) }
                )
        }
    }

    private suspend fun createFirebase(place: Place) {
        db.collection("places").add(place).await()
    }

    fun updatePlace(place: Place) {
        viewModelScope.launch {
            _placeResult.value = RequestResult.Loading
            _placeResult.value = runCatching { updateFirebase(place) }
                .fold(
                    onSuccess = { RequestResult.Success(message = app.getString(R.string.msg_place_updated)) },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_updating_place)) }
                )
        }
    }

    private suspend fun updateFirebase(place: Place) {
        if (place.id.isEmpty()) {
            throw Exception(app.getString(R.string.error_invalid_place_id))
        }
        db.collection("places")
            .document(place.id)
            .set(place)
            .await()
    }

    fun deletePlace(placeId: String) {
        viewModelScope.launch {
            _placeResult.value = RequestResult.Loading
            _placeResult.value = runCatching { deleteFirebase(placeId) }
                .fold(
                    onSuccess = { RequestResult.Success(message = app.getString(R.string.msg_place_deleted)) },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_deleting_place)) }
                )
        }
    }

    private suspend fun deleteFirebase(placeId: String) {
        db.collection("places")
            .document(placeId)
            .delete()
            .await()
    }

    fun findById(id: String): Place? {
        return _places.value.find { it.id == id }
    }

    // Admin operations
    fun approvePlace(placeId: String) {
        viewModelScope.launch {
            try {
                db.collection("places")
                    .document(placeId)
                    .update("placeStatus", PlaceStatus.APPROVED.name)
                    .await()
            } catch (e: Exception) {
                Log.e("PlacesViewModel", "Error approving place", e)
            }
        }
    }

    fun rejectPlace(placeId: String) {
        viewModelScope.launch {
            try {
                db.collection("places")
                    .document(placeId)
                    .update("placeStatus", PlaceStatus.REJECTED.name)
                    .await()
            } catch (e: Exception) {
                Log.e("PlacesViewModel", "Error rejecting place", e)
            }
        }
    }

    fun getPendingPlaces(): List<Place> {
        return _places.value.filter { it.placeStatus == PlaceStatus.PENDING }
    }

    fun getApprovedPlaces(): List<Place> {
        return _places.value.filter { it.placeStatus == PlaceStatus.APPROVED }
    }

    fun getUserPlaces(userId: String): List<Place> {
        return _places.value.filter { it.ownerId == userId }
    }

    fun applyFilters(filters: MapFilters?) {
        _activeFilters.value = filters
        _filteredPlaces.value = if (filters == null) {
            _places.value
        } else {
            _places.value.filter { place ->
                val matchesType = filters.categories.isEmpty() || filters.categories.contains(place.type)
                val matchesSearch = filters.keyword.isNullOrBlank() ||
                        place.title.contains(filters.keyword, ignoreCase = true) ||
                        place.description.contains(filters.keyword, ignoreCase = true)
                val matchesCity = filters.city == null || place.city == filters.city
                val matchesRating = if (filters.minRating != null && filters.minRating > 0) {
                    getAverageRatingForPlace(place.id) >= filters.minRating
                } else true

                matchesType && matchesSearch && matchesCity && matchesRating
            }
        }
    }

    fun clearFilters() {
        _activeFilters.value = null
        _filteredPlaces.value = _places.value
    }

    fun hasActiveFilters(): Boolean {
        return _activeFilters.value != null
    }

    fun filtrarportitulotypo(query: String) {
        if (query.isBlank()) {
            clearFilters()
        } else {
            applyFilters(
                MapFilters(
                    keyword = query,
                    categories = emptySet(),
                    city = null,
                    minRating = null,
                    openNow = false,
                    maxDistanceKm = null
                )
            )
        }
    }

    fun updateCreateState(state: CreatePlaceState) {
        _createPlaceState.value = state
    }

    fun resetCreateState() {
        _createPlaceState.value = CreatePlaceState()
    }

    fun loadPlaceForEdit(placeId: String) {
        val place = findById(placeId) ?: return
        _createPlaceState.value = CreatePlaceState(
            title = place.title,
            description = place.description,
            type = place.type,
            phones = place.phones,
            website = place.website,
            socialMedia = place.socialMedia,
            location = place.location,
            address = place.adress,
            city = place.city,
            neighborhood = place.neighborhood,
            schedule = place.shedule,
            images = place.imagenes
        )
    }

    fun resetOperationResult() {
        _placeResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        placesListener?.remove()
    }
}
