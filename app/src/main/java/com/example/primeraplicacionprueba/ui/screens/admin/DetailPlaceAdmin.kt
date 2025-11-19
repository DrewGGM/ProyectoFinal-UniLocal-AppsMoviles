package com.example.primeraplicacionprueba.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.theme.SuccessGreen
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.ui.components.Map as PlacesMap
import com.mapbox.geojson.Point

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailPlaceAdmin(
    id: String,
    placesViewModel: PlacesViewModel,
    onNavigateBack: () -> Unit
) {
    val places by placesViewModel.places.collectAsState()
    val place = places.find { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            if (place != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(10.dp),
                        onClick = { placesViewModel.rejectPlace(place.id); onNavigateBack() }
                    ) {
                        Text(stringResource(R.string.txt_reject), color = MaterialTheme.colorScheme.onError)
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(10.dp),
                        onClick = { placesViewModel.approvePlace(place.id); onNavigateBack() }
                    ) {
                        Text(stringResource(R.string.txt_approve), color = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        if (place == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.txt_place_not_found))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // HEADER tipo banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                MaterialTheme.colorScheme.tertiaryContainer,
                                RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = place.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = stringResource(R.string.txt_created_by, place.ownerId),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // Imágenes del lugar
                if (place.imagenes.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .padding(10.dp, bottom = 10.dp, end = 10.dp, top = 10.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(Modifier.padding(25.dp, bottom = 15.dp, end = 25.dp, top = 25.dp)) {
                                Text(
                                    text = stringResource(R.string.txt_images),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    place.imagenes.forEach { imageUrl ->
                                        AsyncImage(
                                            model = imageUrl,
                                            contentDescription = stringResource(R.string.txt_place_image),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .width(250.dp)
                                                .fillMaxHeight()
                                                .clip(RoundedCornerShape(12.dp))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    // Descripción
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(10.dp, bottom = 10.dp, end =  10.dp, top = 10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(25.dp)) {
                            Text(stringResource(R.string.txt_description), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = place.description,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                item {
                    // Información y contacto
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(10.dp, bottom = 10.dp, end =  10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

                    ) {
                        Column(Modifier.padding(25.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(stringResource(R.string.txt_information_contact), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

                            val categoryText = when (place.type) {
                                com.example.primeraplicacionprueba.model.PlaceType.RESTAURANT -> stringResource(R.string.category_restaurant)
                                com.example.primeraplicacionprueba.model.PlaceType.CAFE -> stringResource(R.string.category_cafe)
                                com.example.primeraplicacionprueba.model.PlaceType.FAST_FOOD -> stringResource(R.string.category_fastfood)
                                com.example.primeraplicacionprueba.model.PlaceType.MUSEUM -> stringResource(R.string.category_museum)
                                com.example.primeraplicacionprueba.model.PlaceType.HOTEL -> stringResource(R.string.category_hotel)
                                com.example.primeraplicacionprueba.model.PlaceType.BAR -> stringResource(R.string.place_type_bar)
                                com.example.primeraplicacionprueba.model.PlaceType.PARK -> stringResource(R.string.category_park)
                                com.example.primeraplicacionprueba.model.PlaceType.SHOPPING -> stringResource(R.string.category_shopping)
                                com.example.primeraplicacionprueba.model.PlaceType.GAS_STATION -> stringResource(R.string.place_type_gas_station)
                                com.example.primeraplicacionprueba.model.PlaceType.PHARMACY -> stringResource(R.string.place_type_pharmacy)
                                com.example.primeraplicacionprueba.model.PlaceType.HOSPITAL -> stringResource(R.string.place_type_hospital)
                                com.example.primeraplicacionprueba.model.PlaceType.BANK -> stringResource(R.string.place_type_bank)
                                com.example.primeraplicacionprueba.model.PlaceType.GYM -> stringResource(R.string.place_type_gym)
                                com.example.primeraplicacionprueba.model.PlaceType.CINEMA -> stringResource(R.string.place_type_cinema)
                                com.example.primeraplicacionprueba.model.PlaceType.OTHER -> stringResource(R.string.category_other)
                            }
                            InfoRow(Icons.Filled.Category, stringResource(R.string.txt_category_label, categoryText))
                            InfoRow(Icons.Filled.Call, stringResource(R.string.txt_phone_label, place.phones.firstOrNull() ?: stringResource(R.string.txt_not_available_short)))
                            InfoRow(Icons.Filled.Public, stringResource(R.string.txt_web_label, place.website ?: stringResource(R.string.txt_not_available_short)))
                            InfoRow(Icons.Filled.Schedule, stringResource(R.string.txt_open_today, "7:00 AM - 8:00 PM"))
                        }
                    }
                }

                item {
                    // Ubicación
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(10.dp, bottom = 10.dp, end =  10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(25.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(stringResource(R.string.txt_location), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                PlacesMap(
                                    modifierr = Modifier.fillMaxSize(),
                                    activateClick = false,
                                    places = listOf(place),
                                    centerPoint = Point.fromLngLat(place.location.longitude, place.location.latitude),
                                    centerZoom = 15.0
                                )
                            }
                            Text(place.adress, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = SuccessGreen)
        Spacer(Modifier.width(8.dp))
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
