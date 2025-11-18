package com.example.primeraplicacionprueba.network

import com.example.primeraplicacionprueba.model.CitiesResponse
import com.example.primeraplicacionprueba.model.CountryRequest
import com.example.primeraplicacionprueba.model.CountryResponse
import com.example.primeraplicacionprueba.model.StateCitiesResponse
import com.example.primeraplicacionprueba.model.StateCityRequest
import com.example.primeraplicacionprueba.model.StatesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CountriesNowApi {

    @GET("countries")
    suspend fun getCountries(): CountryResponse

    @POST("countries/cities")
    suspend fun getCitiesByCountry(@Body body: CountryRequest): CitiesResponse

    @POST("countries/states")
    suspend fun getStatesByCountry(@Body body: CountryRequest): StatesResponse

    @POST("countries/state/cities")
    suspend fun getCitiesByState(@Body body: StateCityRequest): StateCitiesResponse
}
