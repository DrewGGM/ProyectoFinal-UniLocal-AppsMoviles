package com.example.primeraplicacionprueba.model

data class MapFilters(
	val keyword: String? = null,
	val categories: Set<PlaceType> = emptySet(),
	val city: String? = null,
	val minRating: Float? = null,
	val openNow: Boolean = false,
	val maxDistanceKm: Float? = null
)


