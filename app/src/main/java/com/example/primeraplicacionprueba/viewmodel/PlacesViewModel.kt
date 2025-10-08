package com.example.primeraplicacionprueba.viewmodel

import androidx.lifecycle.ViewModel
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.model.Location
import com.example.primeraplicacionprueba.model.Shedule
import com.example.primeraplicacionprueba.model.Day
import com.example.primeraplicacionprueba.model.Review
import java.time.LocalDate
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
                description = "Un café acogedor con postres artesanales y ambiente relajado perfecto para trabajar o estudiar.",
                phones = listOf("+57 300 111 2233", "+57 6 123 4567"),
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
                adress = "Calle 10 #12-34, Centro",
                website = "www.cafecentral.com",
                email = "info@cafecentral.com",
                socialMedia = "@cafecentral_med",
                reviews = listOf(
                    Review("r1", "u1", "p1", 5, "Una experiencia increíble. La comida deliciosa y el servicio excepcional. ¡Totalmente recomendado!", LocalDate.now().minusDays(2)),
                    Review("r2", "u2", "p1", 4, "Gran lugar para pasar la tarde. El café es excelente y el ambiente muy acogedor. Volveré pronto.", LocalDate.now().minusDays(5))
                ),
                city = "Medellín",
                neighborhood = "Centro",
                priceRange = "$$",
                amenities = listOf("WiFi Gratis", "Aire Acondicionado", "Terraza", "Estacionamiento"),
                isVerified = true,
                ownerId = "1",
                viewCount = 1250,
                favoriteCount = 89
            ),
            Place(
                id = "p2",
                title = "La Parrilla Q",
                imagenes = listOf(
                    "https://images.pexels.com/photos/262047/pexels-photo-262047.jpeg"
                ),
                description = "Restaurante especializado en carnes a la brasa con más de 15 años de experiencia.",
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
                adress = "Av. Bolívar 45-67",
                website = "www.laparrillaq.com",
                email = "reservas@laparrillaq.com",
                socialMedia = "@laparrillaq",
                reviews = listOf(
                    Review("r3", "u3", "p2", 5, "Las mejores carnes de la ciudad. El ambiente es perfecto para una cena romántica.", LocalDate.now().minusDays(1)),
                    Review("r4", "u4", "p2", 4, "Excelente calidad de carne y buen servicio. Un poco caro pero vale la pena.", LocalDate.now().minusDays(3)),
                    Review("r5", "u5", "p2", 3, "Buen lugar pero la espera fue muy larga. La comida estuvo bien.", LocalDate.now().minusDays(7))
                ),
                city = "Medellín",
                neighborhood = "El Poblado",
                priceRange = "$$$",
                amenities = listOf("Reservas", "Delivery", "Terraza", "Bar", "Estacionamiento Valet"),
                isVerified = true,
                ownerId = "2",
                viewCount = 890,
                favoriteCount = 156
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