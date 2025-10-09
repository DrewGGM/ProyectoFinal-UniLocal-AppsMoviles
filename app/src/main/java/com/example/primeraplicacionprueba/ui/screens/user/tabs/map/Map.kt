package com.example.primeraplicacionprueba.ui.screens.user.tabs.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.primeraplicacionprueba.ui.components.Search
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel

@Composable
fun Map(){
    val placesViewModel : PlacesViewModel = viewModel()
    var query by remember { mutableStateOf("") }
    Search(
        query = query,
        onSearch = {
                    placesViewModel.filtrarportitulotypo(query)
                   },
    placeholder = "hh",
    itemText= {it.title} ,
        onQueryChange = {query = it}

    )
}