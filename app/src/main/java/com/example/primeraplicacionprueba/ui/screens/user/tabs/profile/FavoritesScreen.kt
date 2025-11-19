package com.example.primeraplicacionprueba.ui.screens.user.tabs.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.ui.theme.*
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.ui.screens.LocalMainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favorites: List<Place> = emptyList(),
    placesViewModel: PlacesViewModel,
    onNavigateBack: () -> Unit = {},
    onPlaceClick: (String) -> Unit = {},
    onRemoveFavorite: (String) -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val usersViewModel = mainViewModel.usersViewModel
    val userLocation by usersViewModel.userLocation.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.txt_my_favorites_title),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        if (favorites.isEmpty()) {
            EmptyFavoritesState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favorites) { place ->
                    FavoriteCard(
                        place = place,
                        placesViewModel = placesViewModel,
                        userLocation = userLocation,
                        onClick = { onPlaceClick(place.id) },
                        onRemoveFavorite = { onRemoveFavorite(place.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteCard(
    place: Place,
    placesViewModel: PlacesViewModel,
    userLocation: com.example.primeraplicacionprueba.model.Location?,
    onClick: () -> Unit,
    onRemoveFavorite: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del lugar
            if (place.imagenes.isNotEmpty()) {
                AsyncImage(
                    model = place.imagenes.first(),
                    contentDescription = place.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getIconForPlaceType(place.type),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Información del lugar
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = place.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.txt_place_type_distance, place.type, place.getDistanceFromUser(userLocation)),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Rating con estrellas
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < placesViewModel.getAverageRatingForPlace(place.id).toInt()) {
                                    Icons.Default.Star
                                } else {
                                    Icons.Default.StarBorder
                                },
                                contentDescription = null,
                                tint = Accent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = stringResource(R.string.txt_rating_percentage, (placesViewModel.getAverageRatingForPlace(place.id) * 10).toInt()),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Botón de favorito
            IconButton(
                onClick = onRemoveFavorite,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.txt_remove_from_favorites),
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyFavoritesState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.HeartBroken,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(R.string.txt_no_favorites_yet),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.txt_explore_to_find_favorite),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}