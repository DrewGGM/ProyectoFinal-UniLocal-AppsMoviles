package com.example.primeraplicacionprueba.model

data class CountryResponse(
    val error: Boolean,
    val msg: String,
    val data: List<CountryItem>
)

data class CountryItem(
    val country: String,
    @Transient val cities: List<String>? = null
)

data class CitiesResponse(
    val error: Boolean,
    val msg: String,
    val data: List<String>
)

data class StatesResponse(
    val error: Boolean,
    val msg: String,
    val data: StatesData
)

data class StatesData(
    val name: String,
    val iso3: String,
    val iso2: String,
    val states: List<StateItem>
)

data class StateItem(
    val name: String,
    val state_code: String? = null
)

data class StateCitiesResponse(
    val error: Boolean,
    val msg: String,
    val data: List<String>
)

data class CountryRequest(
    val country: String
)

data class StateCityRequest(
    val country: String,
    val state: String
)
