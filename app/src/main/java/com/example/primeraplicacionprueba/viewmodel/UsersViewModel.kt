package com.example.primeraplicacionprueba.viewmodel

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primeraplicacionprueba.model.Role
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.utils.RequestResult
import com.example.primeraplicacionprueba.utils.SharedPrefsUtil
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class UsersViewModel : ViewModel() {

    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    val db = Firebase.firestore

    private val _userResult = MutableStateFlow<RequestResult?>(value = null)
    val userResult: StateFlow<RequestResult?> = _userResult.asStateFlow()


    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val visitedPlaceIds = mutableSetOf<String>()

    init {
        loadUsers()
    }

    fun resetOperationResult() {
        _userResult.value = null
    }

    fun loadUsers() {
        /*_users.value = listOf(
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
        )*/

    }

    private suspend fun createFirebase(user: User) {
        // Check if email already exists
        val emailSnapshot = db.collection("users")
            .whereEqualTo("email", user.email)
            .get()
            .await()

        if (!emailSnapshot.isEmpty) {
            throw Exception("El correo electrónico ya está registrado")
        }

        // Check if username already exists
        val usernameSnapshot = db.collection("users")
            .whereEqualTo("username", user.username)
            .get()
            .await()

        if (!usernameSnapshot.isEmpty) {
            throw Exception("El nombre de usuario ya está en uso")
        }

        // Create user
        db.collection("users").add(user).await()
    }

    fun create(user: User) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching { createFirebase(user) }
                .fold(
                    onSuccess = { RequestResult.Success(message = "Usuario creado correctamente") },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: "Error creando el Usuario") }
                )
        }
    }
    fun update(user: User) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching { updateFirebase(user) }
                .fold(
                    onSuccess = { RequestResult.Success(message = "Usuario actualizado correctamente") },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: "Error actualizando el Usuario") }
                )
        }
    }

    private suspend fun updateFirebase(user: User) {
        if (user.id.isEmpty()) {
            throw Exception("ID de usuario no válido")
        }
        db.collection("users")
            .document(user.id)
            .set(user)
            .await()

        // Update current user if it's the same
        if (_currentUser.value?.id == user.id) {
            _currentUser.value = user
        }
    }

    fun findById(id: String) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching { findByIdFirebase(id) }
                .fold(
                    onSuccess = { RequestResult.Success(message = "Usuario obtenido exitosamente") },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: "Error obteniendo el Usuario") }
                )
        }
    }

    private suspend fun findByIdFirebase(id: String) {
        val snapshot = db.collection("users")
            .document(id)
            .get()
            .await()

        val user = snapshot.toObject(User::class.java)?.apply {
            this.id = snapshot.id
        }

        _currentUser.value = user
    }

    fun findByUsername(username: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .await()

                val user = snapshot.documents.firstOrNull()?.toObject(User::class.java)?.apply {
                    this.id = snapshot.documents.first().id
                }
                onResult(user)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun findByEmail(email: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                val user = snapshot.documents.firstOrNull()?.toObject(User::class.java)?.apply {
                    this.id = snapshot.documents.first().id
                }
                onResult(user)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun delete(userId: String) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching { deleteFirebase(userId) }
                .fold(
                    onSuccess = { RequestResult.Success(message = "Usuario eliminado correctamente") },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: "Error eliminando el Usuario") }
                )
        }
    }

    private suspend fun deleteFirebase(userId: String) {
        db.collection("users")
            .document(userId)
            .delete()
            .await()

        // Clear current user if it's the same
        if (_currentUser.value?.id == userId) {
            _currentUser.value = null
        }
    }

    fun login(emailOrUsername: String, password: String, context: Context? = null) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            try {
                loginFirebase(emailOrUsername, password)

                // Only save if login was successful and currentUser is not null
                val user = _currentUser.value
                if (user != null) {
                    context?.let {
                        SharedPrefsUtil.savePreferences(
                            it,
                            userId = user.id,
                            role = user.rol.toString(),
                            name = user.nombre,
                            email = user.email
                        )
                    }
                    _userResult.value = RequestResult.Success(message = "Inicio de sesión exitoso")
                } else {
                    _userResult.value = RequestResult.Failure(errorMessage = "Error: Usuario no encontrado")
                }
            } catch (e: Exception) {
                _currentUser.value = null // Clear current user on failure
                _userResult.value = RequestResult.Failure(errorMessage = e.message ?: "Credenciales incorrectas")
            }
        }
    }

    private suspend fun loginFirebase(emailOrUsername: String, password: String) {
        // Clear current user before attempting login
        _currentUser.value = null

        // Try to find user by email first
        var snapshot = db.collection("users")
            .whereEqualTo("email", emailOrUsername)
            .whereEqualTo("password", password)
            .get()
            .await()

        // If not found by email, try by username
        if (snapshot.isEmpty) {
            snapshot = db.collection("users")
                .whereEqualTo("username", emailOrUsername)
                .whereEqualTo("password", password)
                .get()
                .await()
        }

        if (snapshot.isEmpty) {
            throw Exception("Credenciales incorrectas")
        }

        val userDoc = snapshot.documents.firstOrNull()
            ?: throw Exception("Credenciales incorrectas")

        val user = userDoc.toObject(User::class.java)
            ?: throw Exception("Error al obtener datos del usuario")

        user.id = userDoc.id
        _currentUser.value = user
    }
    fun restoreUserFromPreferences(context: Context) {
        val prefs = SharedPrefsUtil.getPreferences(context)
        val userId = prefs["userId"] ?: ""

        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                runCatching { findByIdFirebase(userId) }
                    .onFailure {
                        // If user not found in Firebase, clear preferences
                        SharedPrefsUtil.clearPreferences(context)
                    }
            }
        }
    }

    fun logout(context: Context) {
        _currentUser.value = null
        SharedPrefsUtil.clearPreferences(context)
    }

    fun removeFavorite(placeId: String) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                try {
                    // Remove from Firestore
                    db.collection("user_favorites")
                        .document("${user.id}_$placeId")
                        .delete()
                        .await()

                    // Update local user
                    val updatedFavorites = user.favoriteIds.filter { it != placeId }
                    val updatedUser = user.copy(
                        favoriteIds = updatedFavorites,
                        favoritesAdded = updatedFavorites.size
                    )
                    _currentUser.value = updatedUser

                    // Update user in Firestore
                    updateFirebase(updatedUser)
                } catch (e: Exception) {
                    _userResult.value = RequestResult.Failure(errorMessage = "Error al eliminar favorito")
                }
            }
        }
    }

    fun addFavorite(placeId: String) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                if (user.favoriteIds.contains(placeId)) return@launch

                try {
                    // Add to Firestore user_favorites collection
                    val favoriteData = hashMapOf(
                        "userId" to user.id,
                        "placeId" to placeId,
                        "timestamp" to com.google.firebase.Timestamp.now()
                    )
                    db.collection("user_favorites")
                        .document("${user.id}_$placeId")
                        .set(favoriteData)
                        .await()

                    // Update local user
                    val updatedFavorites = user.favoriteIds + placeId
                    val updatedUser = user.copy(
                        favoriteIds = updatedFavorites,
                        favoritesAdded = updatedFavorites.size
                    )
                    _currentUser.value = updatedUser

                    // Update user in Firestore
                    updateFirebase(updatedUser)
                } catch (e: Exception) {
                    _userResult.value = RequestResult.Failure(errorMessage = "Error al agregar favorito")
                }
            }
        }
    }

    fun toggleFavorite(placeId: String) {
        _currentUser.value?.let { user ->
            if (user.favoriteIds.contains(placeId)) {
                removeFavorite(placeId)
            } else {
                addFavorite(placeId)
            }
        }
    }

    fun isFavorite(placeId: String): Boolean {
        return _currentUser.value?.favoriteIds?.contains(placeId) == true
    }

    fun loadUserFavorites(userId: String, onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("user_favorites")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val favoriteIds = snapshot.documents.mapNotNull {
                    it.getString("placeId")
                }
                onResult(favoriteIds)
            } catch (e: Exception) {
                onResult(emptyList())
            }
        }
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