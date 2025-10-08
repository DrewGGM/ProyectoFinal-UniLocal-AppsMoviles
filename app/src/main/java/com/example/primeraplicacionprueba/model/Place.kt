package com.example.primeraplicacionprueba.model

class Place(
    val id: String,
    val title: String,
    val imagenes: List<String>,
    val description: String,
    val phones: List<String>,
    val type: PlaceType,
    val shedule: List<Shedule>,
    val location: Location,
    val rating: Float,
    val adress: String
) {
    fun getDistanceFromUser(): String {
        return "5 km"
    }
}