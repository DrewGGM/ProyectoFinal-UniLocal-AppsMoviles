package com.example.primeraplicacionprueba.viewmodel

import androidx.lifecycle.ViewModel
import com.example.primeraplicacionprueba.model.Rol
import com.example.primeraplicacionprueba.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsersViewModel : ViewModel() {

    private val _users = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

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
                rol = Rol.USER,
                placesCreated = 3,
                placesVisited = 8,
                reviewsWritten = 12,
                favoritesAdded = 5,
                joinDate = "2024-01-01"
            ),
            User(
                nombre = "Andrew",
                username = "admin",
                city = "Armenia",
                country = "Colombia",
                email = "admin@email.com",
                password = "admin123",
                id = "2",
                rol = Rol.ADMIN,
                placesCreated = 15,
                placesVisited = 25,
                reviewsWritten = 35,
                favoritesAdded = 12,
                joinDate = "2023-12-01"
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
    
    fun logout() {
        _currentUser.value = null
    }

}