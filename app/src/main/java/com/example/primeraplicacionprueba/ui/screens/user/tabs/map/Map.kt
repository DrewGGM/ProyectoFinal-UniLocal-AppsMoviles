package com.example.primeraplicacionprueba.ui.screens.user.tabs.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.primeraplicacionprueba.ui.components.Search
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.theme.Accent

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Map(
    onMapToFilter: () -> Unit = {},
){
    val placesViewModel : PlacesViewModel = viewModel()
    var query by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        //  Imagen de fondo (mapa)
        Image(
            painter = painterResource(id = R.drawable.mapa),
            contentDescription = "Mapa",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

//BARRA DE BUSQUEDA
        Search(
            query = query,
            onSearch = {
                placesViewModel.filtrarportitulotypo(query)
            },
            placeholder = stringResource(id = R.string.txt_search_placeholder),
            itemText= {it.title} ,
            onQueryChange = {query = it}

        )

        FloatingActionButton(
            onClick = {onMapToFilter()},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 120.dp)
                .zIndex(1f),
            containerColor = Accent,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.FilterAlt,
                contentDescription = "IconoFiltro"
            )
        }

        //  Tarjeta inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 9.dp)
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column {
                Text("Restaurante Del Sol", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Restaurante", color = Color.Gray, fontSize = 14.sp)
                Row (verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐ 4.8", color = Color(0xFFFFC107), fontSize = 16.sp)
                }
            }
        }

    }



}