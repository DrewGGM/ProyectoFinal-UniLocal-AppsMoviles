package com.example.primeraplicacionprueba.viewmodel

import androidx.lifecycle.ViewModel
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.model.Location
import com.example.primeraplicacionprueba.model.Shedule
import com.example.primeraplicacionprueba.model.Day
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlacesViewModel : ViewModel() {

    private val _places = MutableStateFlow(emptyList<Place>())
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    init {
        loadPlaces()
    }

    fun loadPlaces() {
        _places.value = listOf(
            Place(
                id = "p1",
                title = "Café Central",
                imagenes = listOf(
                    "https://cdn.pixabay.com/photo/2016/11/18/19/13/buildings-1836478_1280.jpg",
                    "https://cdn.pixabay.com/photo/2022/07/14/00/36/cafe-7320242_1280.jpg"
                ),
                description = "Un café acogedor con postres artesanales.",
                phones = listOf("+57 300 111 2233"),
                type = PlaceType.CAFETERIA,
                shedule = listOf(
                    Shedule(Day.MONDAY, java.time.LocalTime.of(8, 0), java.time.LocalTime.of(20, 0)),
                    Shedule(Day.TUESDAY, java.time.LocalTime.of(8, 0), java.time.LocalTime.of(20, 0)),
                    Shedule(Day.WEDNESDAY, java.time.LocalTime.of(8, 0), java.time.LocalTime.of(20, 0)),
                    Shedule(Day.THURSDAY, java.time.LocalTime.of(8, 0), java.time.LocalTime.of(20, 0)),
                    Shedule(Day.FRIDAY, java.time.LocalTime.of(8, 0), java.time.LocalTime.of(20, 0)),
                    Shedule(Day.SATURDAY, java.time.LocalTime.of(9, 0), java.time.LocalTime.of(18, 0))
                ),
                location = Location(latitude = 4.8143, longitude = -75.6946),
                rating = 4.6f,
                adress = "Calle 10 #12-34, Centro"
            ),
            Place(
                id = "p2",
                title = "La Parrilla Q",
                imagenes = listOf(
                    "https://images.pexels.com/photos/262047/pexels-photo-262047.jpeg"
                ),
                description = "Restaurante de carnes a la brasa.",
                phones = listOf("+57 310 555 8899"),
                type = PlaceType.RESTAURANTE,
                shedule = listOf(
                    Shedule(Day.MONDAY, java.time.LocalTime.of(12, 0), java.time.LocalTime.of(22, 0)),
                    Shedule(Day.TUESDAY, java.time.LocalTime.of(12,  0), java.time.LocalTime.of(22, 0)),
                    Shedule(Day.WEDNESDAY, java.time.LocalTime.of(12, 0), java.time.LocalTime.of(22, 0)),
                    Shedule(Day.THURSDAY, java.time.LocalTime.of(12, 0), java.time.LocalTime.of(22,  0 )),
                    Shedule(Day.FRIDAY, java.time.LocalTime.of(12, 0), java.time.LocalTime.of(22, 0)),
                    Shedule(Day.SATURDAY, java.time.LocalTime.of(12, 0), java.time.LocalTime.of(22, 0)),
                    Shedule(Day.SUNDAY, java.time.LocalTime.of(12, 0), java.time.LocalTime.of(22, 0))
                ),
                location = Location(latitude = 4.5353, longitude = -75.6750),
                rating = 4.2f,
                adress = "Av. Bolívar 45-67"
            )
        )
    }

    fun create(place: Place) {
        _places.value = _places.value + place
    }

    fun findById(id: String): Place? {
        return _places.value.find { it.id == id }
    }

    fun findByTitle(title: String): Place? {
        return _places.value.find { it.title.equals(title, ignoreCase = true) }
    }

    fun findByType(type: PlaceType): List<Place>{
        return _places.value.filter { it.type == type }
    }
}