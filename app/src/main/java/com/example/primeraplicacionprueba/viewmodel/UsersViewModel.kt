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
                rol = Rol.USER
            ),
            User(
                nombre = "Andrew",
                username = "admin",
                city = "Armenia",
                country = "Colombia",
                email = "admin@email.com",
                password = "admin123",
                id = "2",
                rol = Rol.ADMIN
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

    fun login(email: String, password: String): User? {
        return _users.value.find { it.email == email && it.password == password }
    }

}