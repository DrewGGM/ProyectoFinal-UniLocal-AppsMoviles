package com.example.primeraplicacionprueba.viewmodel

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.primeraplicacionprueba.model.Role
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.utils.SharedPrefsUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class UsersViewModel : ViewModel() {

    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val visitedPlaceIds = mutableSetOf<String>()

    init {
        loadUsers()
    }

    fun loadUsers() {
        _users.value = listOf(
            User(
                nombre = "Juan Camilo",
                username = "juanca",
                city = "Pereira",
                country = "Colombia",
                email = "user@email.com",
                password = "user123",
                id = "1",
                rol = Role.USER,
                placesCreated = 3,
                placesVisited = 8,
                reviewsWritten = 12,
                favoritesAdded = 5,
                joinDate = LocalDate.now()
            ),
            User(
                nombre = "Valeria",
                username = "vale",
                city = "Armenia",
                country = "Colombia",
                email = "valeria@email.com",
                password = "vale123",
                id = "2",
                rol = Role.USER,
                placesCreated = 5,
                placesVisited = 15,
                reviewsWritten = 7,
                favoritesAdded = 9,
                joinDate = LocalDate.now().minusDays(10)
            ),
            User(
                nombre = "Andrew",
                username = "admin",
                city = "Armenia",
                country = "Colombia",
                email = "admin@email.com",
                password = "admin123",
                id = "2",
                rol = Role.ADMIN,
                placesCreated = 15,
                placesVisited = 25,
                reviewsWritten = 35,
                favoritesAdded = 12,
                joinDate = LocalDate.now()
            )
        )

    }

    fun create(user: User) {
        _users.value = _users.value + user
    }

    fun findById(id: String): User? {
        return _users.value.find { it.id == id }
    }

    fun findByUsername(username: String): User? {
        return _users.value.find { it.username == username }
    }

    fun findByEmail(email: String): User? {
        return _users.value.find { it.email == email }
    }

    fun login(emialOrUsername: String, password: String): User? {
        val user = _users.value.find { (it.email == emialOrUsername || it.username == emialOrUsername) && it.password == password }
        if (user != null) {
            _currentUser.value = user
        }
        return user
    }
    
    fun restoreUserFromPreferences(context: Context) {
        val prefs = SharedPrefsUtil.getPreferences(context)
        val userId = prefs["userId"] ?: ""

        if (userId.isNotEmpty()) {
            val user = findById(userId)
            if (user != null) {
                _currentUser.value = user
            }
        }
    }

    fun logout(context: Context) {
        _currentUser.value = null
        SharedPrefsUtil.clearPreferences(context)
    }

    fun removeFavorite(placeId: String) {
        _currentUser.value?.let { user ->
            val updatedFavorites = user.favorites.filter { it.id != placeId }
            _currentUser.value = user.copy(
                favorites = updatedFavorites,
                favoritesAdded = updatedFavorites.size
            )
        }
    }

    fun addFavorite(place: com.example.primeraplicacionprueba.model.Place) {
        _currentUser.value?.let { user ->
            if (user.favorites.any { it.id == place.id }) return
            val updatedFavorites = user.favorites + place
            _currentUser.value = user.copy(
                favorites = updatedFavorites,
                favoritesAdded = updatedFavorites.size
            )
        }
    }

    fun toggleFavorite(place: com.example.primeraplicacionprueba.model.Place) {
        _currentUser.value?.let { user ->
            if (user.favorites.any { it.id == place.id }) {
                val updated = user.favorites.filter { it.id != place.id }
                _currentUser.value = user.copy(
                    favorites = updated,
                    favoritesAdded = updated.size
                )
            } else {
                val updated = user.favorites + place
                _currentUser.value = user.copy(
                    favorites = updated,
                    favoritesAdded = updated.size
                )
            }
        }
    }

    fun isFavorite(placeId: String): Boolean {
        return _currentUser.value?.favorites?.any { it.id == placeId } == true
    }

    fun registerVisited(placeId: String) {
        if (visitedPlaceIds.add(placeId)) {
            _currentUser.value?.let { user ->
                _currentUser.value = user.copy(placesVisited = user.placesVisited + 1)
            }
        }
    }

    fun updatePlacesCreated(count: Int) {
        _currentUser.value?.let { user ->
            if (user.placesCreated != count) {
                _currentUser.value = user.copy(placesCreated = count)
            }
        }
    }

    fun updateCurrentUserProfile(nombre: String, username: String, city: String) {
        val current = _currentUser.value ?: return
        val updated = current.copy(
            nombre = nombre,
            username = username,
            city = city
        )
        _currentUser.value = updated
        _users.value = _users.value.map { if (it.id == updated.id) updated else it }
    }

}