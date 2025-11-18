package com.example.primeraplicacionprueba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.primeraplicacionprueba.model.CountryRequest
import com.example.primeraplicacionprueba.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private var countriesCache: List<String>? = null

    private val _countries = MutableStateFlow<List<String>>(emptyList())
    val countries: StateFlow<List<String>> = _countries.asStateFlow()

    private val _cities = MutableStateFlow<List<String>>(emptyList())
    val cities: StateFlow<List<String>> = _cities.asStateFlow()

    private val _isLoadingCountries = MutableStateFlow(false)
    val isLoadingCountries: StateFlow<Boolean> = _isLoadingCountries.asStateFlow()

    private val _isLoadingCities = MutableStateFlow(false)
    val isLoadingCities: StateFlow<Boolean> = _isLoadingCities.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadCountries()
    }

    fun loadCountries() {
        if (countriesCache != null && countriesCache!!.isNotEmpty()) {
            _countries.value = countriesCache!!
            return
        }

        viewModelScope.launch {
            _isLoadingCountries.value = true
            _errorMessage.value = null

            try {
                val response = RetrofitClient.api.getCountries()
                if (!response.error) {
                    val countryNames = response.data.map { it.country }.sorted()
                    countriesCache = countryNames
                    _countries.value = countryNames
                } else {
                    _errorMessage.value = response.msg
                    val fallback = getCommonCountries()
                    countriesCache = fallback
                    _countries.value = fallback
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar países: ${e.message}"
                val fallback = getCommonCountries()
                countriesCache = fallback
                _countries.value = fallback
            } finally {
                _isLoadingCountries.value = false
            }
        }
    }

    fun loadCitiesByCountry(country: String) {
        if (country.isBlank()) {
            _cities.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoadingCities.value = true
            _errorMessage.value = null

            try {
                val response = RetrofitClient.api.getCitiesByCountry(CountryRequest(country))
                if (!response.error) {
                    _cities.value = response.data.sorted()
                } else {
                    _errorMessage.value = response.msg
                    _cities.value = emptyList()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar ciudades: ${e.message}"
                _cities.value = emptyList()
            } finally {
                _isLoadingCities.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun resetCities() {
        _cities.value = emptyList()
    }

    private fun getCommonCountries(): List<String> {
        return listOf(
            "Argentina", "Bolivia", "Brasil", "Chile", "Colombia",
            "Costa Rica", "Cuba", "Ecuador", "El Salvador", "España",
            "Guatemala", "Honduras", "México", "Nicaragua", "Panamá",
            "Paraguay", "Perú", "Puerto Rico", "República Dominicana",
            "Uruguay", "Venezuela", "Estados Unidos", "Canadá"
        ).sorted()
    }
}
