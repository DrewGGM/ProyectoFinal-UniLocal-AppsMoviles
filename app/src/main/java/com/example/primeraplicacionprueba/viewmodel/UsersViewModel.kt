package com.example.primeraplicacionprueba.viewmodel

import android.Manifest
import android.content.Context
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Location
import com.example.primeraplicacionprueba.model.Role
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.utils.RequestResult
import com.example.primeraplicacionprueba.utils.SharedPrefsUtil
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private val app: Application = getApplication()
    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    val db = Firebase.firestore

    val auth : FirebaseAuth = FirebaseAuth.getInstance()


    private val _userResult = MutableStateFlow<RequestResult?>(value = null)
    val userResult: StateFlow<RequestResult?> = _userResult.asStateFlow()


    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _needsProfileCompletion = MutableStateFlow(false)
    val needsProfileCompletion: StateFlow<Boolean> = _needsProfileCompletion.asStateFlow()

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val visitedPlaceIds = mutableSetOf<String>()

    init {
        // Intentar obtener la ubicación del usuario al inicializar
        updateUserLocation()
    }

    fun updateUserLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (ContextCompat.checkSelfPermission(
                        app,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val location = fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        CancellationTokenSource().token
                    ).await()

                    location?.let {
                        _userLocation.value = Location(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    }
                }
            } catch (e: Exception) {
                // Si falla, mantener la ubicación como null
                e.printStackTrace()
            }
        }
    }

    fun resetOperationResult() {
        _userResult.value = null
    }

    private suspend fun createFirebase(user: User) {
        val usernameSnapshot = db.collection("users")
            .whereEqualTo("username", user.username)
            .limit(1)
            .get()
            .await()

        if (!usernameSnapshot.isEmpty) {
            throw Exception(app.getString(R.string.error_username_in_use))
        }

        val authResult = auth.createUserWithEmailAndPassword(user.email, user.password).await()
        val uid = authResult.user?.uid
            ?: throw Exception(app.getString(R.string.error_could_not_get_uid))

        val userProfile = user.copy(
            id = uid,
            password = ""
        )

        db.collection("users")
            .document(uid)
            .set(userProfile)
            .await()
    }

    fun create(user: User) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching { createFirebase(user) }
                .fold(
                    onSuccess = { RequestResult.Success(message = app.getString(R.string.msg_user_created)) },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_creating_user)) }
                )
        }
    }
    fun update(user: User) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching { updateFirebase(user) }
                .fold(
                    onSuccess = { RequestResult.Success(message = app.getString(R.string.msg_user_updated)) },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_updating_user)) }
                )
        }
    }

    private suspend fun updateFirebase(user: User) {
        if (user.id.isEmpty()) {
            throw Exception(app.getString(R.string.error_invalid_user_id))
        }
        db.collection("users")
            .document(user.id)
            .set(user)
            .await()

        if (_currentUser.value?.id == user.id) {
            _currentUser.value = user
        }
    }

    fun findById(id: String) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching { findByIdFirebase(id) }
                .fold(
                    onSuccess = { RequestResult.Success(message = app.getString(R.string.msg_user_obtained)) },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_obtaining_user)) }
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
                    onSuccess = { RequestResult.Success(message = app.getString(R.string.msg_user_deleted)) },
                    onFailure = { RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_deleting_user)) }
                )
        }
    }

    private suspend fun deleteFirebase(userId: String) {
        db.collection("users")
            .document(userId)
            .delete()
            .await()

        if (_currentUser.value?.id == userId) {
            _currentUser.value = null
        }
    }

    fun login(emailOrUsername: String, password: String, context: Context? = null) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            try {
                loginFirebase(emailOrUsername, password)

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
                    _userResult.value = RequestResult.Success(message = app.getString(R.string.msg_login_success))
                } else {
                    _userResult.value = RequestResult.Failure(errorMessage = app.getString(R.string.error_user_not_found))
                }
            } catch (e: Exception) {
                _currentUser.value = null
                _userResult.value = RequestResult.Failure(errorMessage = e.message ?: app.getString(R.string.error_credentials_incorrect))
            }
        }
    }

    private suspend fun loginFirebase(emailOrUsername: String, password: String) {
        _currentUser.value = null

        val email = if (emailOrUsername.contains("@")) {
            emailOrUsername
        } else {
            val snapshot = db.collection("users")
                .whereEqualTo("username", emailOrUsername)
                .limit(1)
                .get()
                .await()

            if (snapshot.isEmpty) {
                throw Exception(app.getString(R.string.error_user_not_found_exception))
            }

            snapshot.documents.firstOrNull()?.getString("email")
                ?: throw Exception(app.getString(R.string.error_user_not_found_exception))
        }

        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid
            ?: throw Exception(app.getString(R.string.error_authentication))

        findByIdFirebase(id = uid)

        if (_currentUser.value == null) {
            throw Exception(app.getString(R.string.error_loading_user_profile))
        }
    }
    fun signInWithGoogle(account: GoogleSignInAccount, context: Context) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            try {
                signInWithGoogleFirebase(account)

                val user = _currentUser.value
                if (user != null) {
                    SharedPrefsUtil.savePreferences(
                        context,
                        userId = user.id,
                        role = user.rol.toString(),
                        name = user.nombre,
                        email = user.email
                    )
                    _userResult.value = RequestResult.Success(message = app.getString(R.string.msg_login_success_google))
                } else {
                    _userResult.value = RequestResult.Failure(errorMessage = app.getString(R.string.error_user_not_found))
                }
            } catch (e: Exception) {
                _currentUser.value = null
                _userResult.value = RequestResult.Failure(errorMessage = e.message ?: app.getString(R.string.error_google_signin))
            }
        }
    }

    private suspend fun signInWithGoogleFirebase(account: GoogleSignInAccount) {
        _currentUser.value = null

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        val authResult = auth.signInWithCredential(credential).await()
        val uid = authResult.user?.uid
            ?: throw Exception(app.getString(R.string.error_authentication_google))

        val userSnapshot = db.collection("users")
            .document(uid)
            .get()
            .await()

        if (userSnapshot.exists()) {
            val user = userSnapshot.toObject(User::class.java)?.apply {
                this.id = userSnapshot.id
            }
            _currentUser.value = user

            _needsProfileCompletion.value = user?.let {
                it.email.isBlank() || it.city.isBlank() || it.country.isBlank()
            } ?: false
        } else {
            val firebaseUser = authResult.user!!
            val newUser = User(
                id = uid,
                nombre = firebaseUser.displayName ?: app.getString(R.string.default_user_name),
                username = firebaseUser.email?.substringBefore("@") ?: "user_${uid.take(8)}",
                email = firebaseUser.email ?: "",
                password = "",
                rol = Role.USER,
                city = "",
                country = "",
                imageUrl = firebaseUser.photoUrl?.toString(),
                favoriteIds = emptyList(),
                placesVisited = 0,
                placesCreated = 0,
                reviewsWritten = 0,
                favoritesAdded = 0,
                joinDate = Timestamp.now(),
                isActive = true
            )

            db.collection("users")
                .document(uid)
                .set(newUser)
                .await()

            _currentUser.value = newUser
            _needsProfileCompletion.value = true
        }
    }

    fun signInWithFacebook(accessToken: AccessToken, context: Context) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            try {
                signInWithFacebookFirebase(accessToken)

                // Save to SharedPreferences
                val user = _currentUser.value
                if (user != null) {
                    SharedPrefsUtil.savePreferences(
                        context,
                        userId = user.id,
                        role = user.rol.toString(),
                        name = user.nombre,
                        email = user.email
                    )
                    _userResult.value = RequestResult.Success(message = app.getString(R.string.msg_login_success_facebook))
                } else {
                    _userResult.value = RequestResult.Failure(errorMessage = app.getString(R.string.error_user_not_found))
                }
            } catch (e: Exception) {
                _currentUser.value = null
                _userResult.value = RequestResult.Failure(errorMessage = e.message ?: app.getString(R.string.error_facebook_signin))
            }
        }
    }

    private suspend fun signInWithFacebookFirebase(accessToken: AccessToken) {
        _currentUser.value = null

        val credential = FacebookAuthProvider.getCredential(accessToken.token)

        val authResult = auth.signInWithCredential(credential).await()
        val uid = authResult.user?.uid
            ?: throw Exception(app.getString(R.string.error_authentication_facebook))

        // Check if user exists in Firestore
        val userSnapshot = db.collection("users")
            .document(uid)
            .get()
            .await()

        if (userSnapshot.exists()) {
            val user = userSnapshot.toObject(User::class.java)?.apply {
                this.id = userSnapshot.id
            }
            _currentUser.value = user

            _needsProfileCompletion.value = user?.let {
                it.email.isBlank() || it.city.isBlank() || it.country.isBlank()
            } ?: false
        } else {
            val firebaseUser = authResult.user!!

            // Obtener foto de perfil de Facebook en alta calidad usando Graph API
            val profilePictureUrl = getFacebookProfilePictureUrl(accessToken)

            val newUser = User(
                id = uid,
                nombre = firebaseUser.displayName ?: app.getString(R.string.default_user_name),
                username = firebaseUser.email?.substringBefore("@") ?: "user_${uid.take(8)}",
                email = firebaseUser.email ?: "",
                password = "", // No password for Facebook sign-in
                rol = Role.USER,
                city = "",
                country = "",
                imageUrl = profilePictureUrl ?: firebaseUser.photoUrl?.toString(),
                favoriteIds = emptyList(),
                placesVisited = 0,
                placesCreated = 0,
                reviewsWritten = 0,
                favoritesAdded = 0,
                joinDate = Timestamp.now(),
                isActive = true
            )

            db.collection("users")
                .document(uid)
                .set(newUser)
                .await()

            _currentUser.value = newUser
            _needsProfileCompletion.value = true
        }
    }

    private suspend fun getFacebookProfilePictureUrl(accessToken: AccessToken): String? {
        return withContext(Dispatchers.IO) {
            try {
                // Usar Graph API de Facebook para obtener foto de perfil en alta calidad
                val graphRequest = GraphRequest.newMeRequest(
                    accessToken
                ) { _, _ ->
                    // Este callback no se usa en la versión síncrona
                }

                // Configurar los campos que queremos obtener
                val parameters = android.os.Bundle()
                parameters.putString("fields", "picture.type(large)")
                graphRequest.parameters = parameters

                // Ejecutar la petición de forma síncrona
                val response = graphRequest.executeAndWait()
                val jsonObject = response.jsonObject

                // Extraer la URL de la foto de perfil
                jsonObject?.optJSONObject("picture")
                    ?.optJSONObject("data")
                    ?.optString("url")
            } catch (e: Exception) {
                android.util.Log.e("FacebookProfilePic", "Error getting profile picture: ${e.message}")
                null
            }
        }
    }

    fun restoreUserFromPreferences(context: Context) {
        val prefs = SharedPrefsUtil.getPreferences(context)
        val userId = prefs["userId"] ?: ""

        if (userId.isNotEmpty()) {
            viewModelScope.launch {
                runCatching { findByIdFirebase(userId) }
                    .onFailure {
                        SharedPrefsUtil.clearPreferences(context)
                    }
            }
        }
    }

    fun logout(context: Context) {
        _currentUser.value = null
        auth.signOut()
        SharedPrefsUtil.clearPreferences(context)
    }

    fun removeFavorite(placeId: String) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                try {
                    db.collection("user_favorites")
                        .document("${user.id}_$placeId")
                        .delete()
                        .await()

                    val updatedFavorites = user.favoriteIds.filter { it != placeId }
                    val updatedUser = user.copy(
                        favoriteIds = updatedFavorites,
                        favoritesAdded = updatedFavorites.size
                    )
                    _currentUser.value = updatedUser
                    updateFirebase(updatedUser)
                } catch (e: Exception) {
                    _userResult.value = RequestResult.Failure(errorMessage = app.getString(R.string.error_removing_favorite))
                }
            }
        }
    }

    fun addFavorite(placeId: String) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                if (user.favoriteIds.contains(placeId)) return@launch

                try {
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
                    updateFirebase(updatedUser)
                } catch (e: Exception) {
                    _userResult.value = RequestResult.Failure(errorMessage = app.getString(R.string.error_adding_favorite))
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
            viewModelScope.launch {
                _currentUser.value?.let { user ->
                    val updatedUser = user.copy(placesVisited = user.placesVisited + 1)
                    _currentUser.value = updatedUser
                    try {
                        updateFirebase(updatedUser)
                    } catch (e: Exception) {
                        // Silently fail - achievement tracking is not critical
                    }
                }
            }
        }
    }

    fun updatePlacesCreated(count: Int) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                if (user.placesCreated != count) {
                    val updatedUser = user.copy(placesCreated = count)
                    _currentUser.value = updatedUser
                    try {
                        updateFirebase(updatedUser)
                    } catch (e: Exception) {
                        // Silently fail - achievement tracking is not critical
                    }
                }
            }
        }
    }

    fun incrementPlacesCreated() {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                val updatedUser = user.copy(placesCreated = user.placesCreated + 1)
                _currentUser.value = updatedUser
                try {
                    updateFirebase(updatedUser)
                } catch (e: Exception) {
                    // Silently fail - achievement tracking is not critical
                }
            }
        }
    }

    fun incrementReviewsWritten() {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                val updatedUser = user.copy(reviewsWritten = user.reviewsWritten + 1)
                _currentUser.value = updatedUser
                try {
                    updateFirebase(updatedUser)
                } catch (e: Exception) {
                    // Silently fail - achievement tracking is not critical
                }
            }
        }
    }

    fun updateCurrentUserProfile(nombre: String, username: String, city: String, imageUrl: String? = null) {
        val current = _currentUser.value ?: return
        val updated = current.copy(
            nombre = nombre,
            username = username,
            city = city,
            imageUrl = imageUrl ?: current.imageUrl
        )
        _currentUser.value = updated
        _users.value = _users.value.map { if (it.id == updated.id) updated else it }

        viewModelScope.launch {
            runCatching { updateFirebase(updated) }
                .onFailure {
                    _userResult.value = RequestResult.Failure(errorMessage = app.getString(R.string.error_updating_profile))
                }
        }
    }

    fun completeProfile(email: String, username: String, city: String, country: String, context: Context) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            try {
                val current = _currentUser.value ?: throw Exception(app.getString(R.string.error_user_not_found_exception))

                // Validar que el email no esté en uso si se cambió
                if (email != current.email && email.isNotBlank()) {
                    val emailSnapshot = db.collection("users")
                        .whereEqualTo("email", email)
                        .limit(1)
                        .get()
                        .await()

                    if (!emailSnapshot.isEmpty) {
                        throw Exception(app.getString(R.string.error_email_in_use))
                    }
                }

                if (username != current.username) {
                    val usernameSnapshot = db.collection("users")
                        .whereEqualTo("username", username)
                        .limit(1)
                        .get()
                        .await()

                    if (!usernameSnapshot.isEmpty) {
                        throw Exception(app.getString(R.string.error_username_in_use))
                    }
                }

                val updated = current.copy(
                    email = if (email.isNotBlank()) email else current.email,
                    username = username,
                    city = city,
                    country = country
                )

                updateFirebase(updated)

                _currentUser.value = updated
                _users.value = _users.value.map { if (it.id == updated.id) updated else it }

                SharedPrefsUtil.savePreferences(
                    context,
                    userId = updated.id,
                    role = updated.rol.toString(),
                    name = updated.nombre,
                    email = updated.email
                )

                _needsProfileCompletion.value = false

                _userResult.value = RequestResult.Success(message = app.getString(R.string.msg_profile_completed))
            } catch (e: Exception) {
                _userResult.value = RequestResult.Failure(errorMessage = e.message ?: app.getString(R.string.error_completing_profile))
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _userResult.value = RequestResult.Loading
            _userResult.value = runCatching {
                sendPasswordResetEmailFirebase(email)
            }.fold(
                onSuccess = {
                    RequestResult.Success(message = app.getString(R.string.msg_recovery_email_sent))
                },
                onFailure = {
                    RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_sending_email))
                }
            )
        }
    }

    private suspend fun sendPasswordResetEmailFirebase(email: String) {
        if (email.isBlank()) {
            throw Exception(app.getString(R.string.txt_email_required))
        }
        if (!email.contains("@")) {
            throw Exception(app.getString(R.string.txt_email_invalid))
        }

        auth.sendPasswordResetEmail(email).await()
    }

    fun syncUserStats() {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                try {
                    // Count places created by user
                    val placesSnapshot = db.collection("places")
                        .whereEqualTo("ownerId", user.id)
                        .get()
                        .await()
                    val placesCount = placesSnapshot.size()

                    // Count reviews written by user
                    val reviewsSnapshot = db.collection("reviews")
                        .whereEqualTo("userID", user.id)
                        .get()
                        .await()
                    val reviewsCount = reviewsSnapshot.size()

                    // Update user with real counts
                    val updatedUser = user.copy(
                        placesCreated = placesCount,
                        reviewsWritten = reviewsCount
                    )

                    _currentUser.value = updatedUser
                    updateFirebase(updatedUser)
                } catch (e: Exception) {
                    // Silently fail - stats sync is not critical
                    e.printStackTrace()
                }
            }
        }
    }

}