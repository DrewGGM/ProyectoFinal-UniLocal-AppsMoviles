package com.example.primeraplicacionprueba.model

import java.time.LocalDate

class Review (
    val id: String,
    val userId: String,
    val placeId: String,
    val rating: Int,//calificacion del comentario
    val comment: String,
    val date: LocalDate
){
}