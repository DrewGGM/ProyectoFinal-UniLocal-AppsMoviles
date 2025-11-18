package com.example.primeraplicacionprueba.ui.screens.user.tabs.map

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import com.example.primeraplicacionprueba.ui.components.Search
import com.example.primeraplicacionprueba.ui.components.PlaceSearchItem
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.ui.screens.LocalMainViewModel
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.components.Map as PlacesMap
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.ui.theme.Accent
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.DefaultViewportTransitionOptions

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Map(
    onMapToFilter: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {}
){

    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
   val placesViewModel = LocalMainViewModel.current.placesViewModel
    val places by placesViewModel.places.collectAsState()
    val filtered by placesViewModel.filteredPlaces.collectAsState()

    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var centerPointState by remember { mutableStateOf<Point?>(null) }
    var centerZoomState by remember { mutableStateOf<Double?>(null) }

    Box(


    ) {
        //  Imagen de fondo (mapa)
         PlacesMap (
            places= places,
             modifierr = Modifier.fillMaxSize(),
            activateClick = false,
             onMapClickListener = { selectedPlace = null },
             onPlaceSelected = { place ->
                 selectedPlace = place
            },
            centerPoint = centerPointState ?: if (placesViewModel.hasActiveFilters() && filtered.isNotEmpty()) {
                Point.fromLngLat(filtered.first().location.longitude, filtered.first().location.latitude)
            } else null,
            centerZoom = centerZoomState ?: if (placesViewModel.hasActiveFilters() && filtered.isNotEmpty()) 14.0 else null
         )

        //BARRA DE BUSQUEDA
        Search(
            query = query,
            onSearch = { searchQuery ->
                placesViewModel.filtrarportitulotypo(searchQuery)
                filtered
            },
            placeholder = stringResource(id = R.string.txt_search_placeholder),
            itemText= {it.title} ,
            onQueryChange = {query = it},
            expanded = expanded,
            onExpandedChange = { expanded = it },
            onItemClick = { place ->
                // Navegar al detalle del lugar
                onNavigateToDetail(place.id)
            },
            itemContent = { place, onClick ->
                PlaceSearchItem(
                    place = place,
                    rating = placesViewModel.getAverageRatingForPlace(place.id),
                    onClick = onClick
                )
            }
        )

        FloatingActionButton(
            onClick = {onMapToFilter()},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = if (selectedPlace != null) 120.dp else 20.dp
                )
                .zIndex(1f),
            containerColor = Accent,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.FilterAlt,
                contentDescription = stringResource(R.string.cd_filter_icon)
            )
        }

        //  Mensaje si no hay resultados con filtros
        if (placesViewModel.hasActiveFilters() && filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFF3E0))
                    .padding(14.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.txt_no_places_found_filters),
                    color = Color(0xFF8D6E63),
                    fontSize = 14.sp
                )
            }
        }

        //  Tarjeta inferior (solo si hay un lugar seleccionado)
        if (selectedPlace != null) {
            val place = selectedPlace!!
            val rating = placesViewModel.getAverageRatingForPlace(place.id)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 9.dp)
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White)
                    .clickable { onNavigateToDetail(place.id) }
                    .padding(16.dp)
            ) {
                Column {
                    Text(place.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(place.type.name.lowercase().replaceFirstChar { it.titlecase() }, color = Color.Gray, fontSize = 14.sp)
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.txt_rating_star, rating), color = Color(0xFFFFC107), fontSize = 16.sp)
                    }
                }
            }
        }

    }

}


