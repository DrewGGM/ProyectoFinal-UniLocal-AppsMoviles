package com.example.primeraplicacionprueba.ui.screens.user.tabs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel

@Composable
fun PlaceDetail(
    placesViewModel: PlacesViewModel,
    id: String
) {

    val place = placesViewModel.findById(id)



}