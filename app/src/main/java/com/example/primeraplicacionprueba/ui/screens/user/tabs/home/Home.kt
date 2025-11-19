package com.example.primeraplicacionprueba.ui.screens.user.tabs.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.model.PlaceStatus
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.theme.*
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.viewmodel.UsersViewModel
import com.example.primeraplicacionprueba.ui.components.Search
import com.example.primeraplicacionprueba.ui.screens.LocalMainViewModel

@Composable
fun Home(
    onNavigateToCreatePlace: () -> Unit = {},
    onNavigateToPlace: (String) -> Unit = {},
    placesViewModel: PlacesViewModel,
    user: User
) {
    val mainViewModel = LocalMainViewModel.current
    val usersViewModel = mainViewModel.usersViewModel
    val userLocation by usersViewModel.userLocation.collectAsState()

    val places by placesViewModel.places.collectAsState()
    val mostPopularPlaces = places
        .filter { it.city.equals(user.city, ignoreCase = true) && it.placeStatus == PlaceStatus.APPROVED }
        .sortedByDescending { it.favoriteCount }

    var query by remember { mutableStateOf("") }
    var searchExpanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<PlaceType?>(null) }
    val availableTypes by remember(places, user.city) {
        mutableStateOf(
            places.filter { it.city.equals(user.city, ignoreCase = true) && it.placeStatus == PlaceStatus.APPROVED }
                .map { it.type }
                .distinct()
        )
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
            item {
                HeaderSection(
                    userName = user.nombre,
                    searchBar = {
                        Search(
                            query = query,
                            onQueryChange = { query = it },
                            onSearch = { q ->
                                places.filter { p ->
                                    p.title.contains(q, ignoreCase = true) ||
                                    p.type.toString().contains(q, ignoreCase = true)
                                }
                            },
                            placeholder = stringResource(R.string.txt_search_placeholder),
                            itemText = { place -> place.title },
                            expanded = false,
                            onExpandedChange = { }
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sección de Categorías (filtro rápido)
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.txt_categories),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
                CategoriesSection(
                    types = availableTypes,
                    selected = selectedType,
                    onSelect = { type ->
                        selectedType = if (selectedType == type) null else type
                    }
                )
            }

            // Sección de Lugares Populares
            item {
                Text(
                    text = stringResource(R.string.txt_popular_places),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }

            // Lista filtrada por query y categoría (solo lugares aprobados)
            val base = places.filter { it.city.equals(user.city, ignoreCase = true) && it.placeStatus == PlaceStatus.APPROVED }
            val byType = selectedType?.let { t -> base.filter { it.type == t } } ?: base
            val listToShow = if (query.isNotBlank()) {
                byType.filter { p ->
                    p.title.contains(query, ignoreCase = true) ||
                    p.type.toString().contains(query, ignoreCase = true)
                }
            } else byType

            items(listToShow, key = { it.id }) {
                PlaceCard(
                    name = it.title ,
                    category = it.type ,
                    rating = placesViewModel.getAverageRatingForPlace(it.id) ,
                    distance = it.getDistanceFromUser(userLocation),
                    imageUrl = it.imagenes.firstOrNull() ?: "",
                    onClick = { onNavigateToPlace(it.id) }
                )
            }
        }

        // Botón flotante
        FloatingActionButton(
            onClick = { onNavigateToCreatePlace() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            containerColor = Accent,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.txt_add_new_place)
            )
        }
        }
    }
}

@Composable
fun HeaderSection(
    userName: String,
    onSearchClick: () -> Unit = {},
    searchBar: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        // Fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Primary, Secondary)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.txt_hello_user, userName),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.txt_discover_places),
                    fontSize = 15.sp,
                    color = TextLight.copy(alpha = 0.9f)
                )
            }
        }

        // Barra de búsqueda (si se provee desde afuera)
        if (searchBar != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 48.dp)
            ) {
                searchBar()
            }
        }
    }
}

@Composable
fun CategoriesSection(
    types: List<PlaceType>,
    selected: PlaceType?,
    onSelect: (PlaceType) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        types.forEach { type ->
            val (label, icon, color) = when (type) {
                PlaceType.RESTAURANT -> Triple(stringResource(R.string.category_restaurant), Icons.Filled.Restaurant, Secondary)
                PlaceType.CAFE -> Triple(stringResource(R.string.category_cafe), Icons.Filled.Coffee, Accent)
                PlaceType.FAST_FOOD -> Triple(stringResource(R.string.category_fastfood), Icons.Filled.Fastfood, Primary)
                PlaceType.MUSEUM -> Triple(stringResource(R.string.category_museum), Icons.Filled.Museum, Tertiary)
                PlaceType.HOTEL -> Triple(stringResource(R.string.category_hotel), Icons.Filled.Hotel, Tertiary)
                PlaceType.BAR -> Triple(stringResource(R.string.place_type_bar), Icons.Filled.LocalBar, Secondary)
                PlaceType.PARK -> Triple(stringResource(R.string.category_park), Icons.Filled.Park, Tertiary)
                PlaceType.SHOPPING -> Triple(stringResource(R.string.category_shopping), Icons.Filled.ShoppingBag, Secondary)
                PlaceType.GAS_STATION -> Triple(stringResource(R.string.place_type_gas_station), Icons.Filled.LocalGasStation, Secondary)
                PlaceType.PHARMACY -> Triple(stringResource(R.string.place_type_pharmacy), Icons.Filled.LocalPharmacy, Secondary)
                PlaceType.HOSPITAL -> Triple(stringResource(R.string.place_type_hospital), Icons.Filled.LocalHospital, Secondary)
                PlaceType.BANK -> Triple(stringResource(R.string.place_type_bank), Icons.Filled.AccountBalance, Secondary)
                PlaceType.GYM -> Triple(stringResource(R.string.place_type_gym), Icons.Filled.FitnessCenter, Secondary)
                PlaceType.CINEMA -> Triple(stringResource(R.string.place_type_cinema), Icons.Filled.Movie, Secondary)
                PlaceType.OTHER -> Triple(stringResource(R.string.category_other), Icons.Filled.Place, Primary)
            }
            val bg = Brush.linearGradient(listOf(color, color.copy(alpha = 0.7f)))
            val chipSize = if (selected == type) 64.dp else 56.dp
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(80.dp)
                    .clickable { onSelect(type) }
            ) {
                Box(
                    modifier = Modifier
                        .size(chipSize)
                        .clip(RoundedCornerShape(20.dp))
                        .background(bg),
                    contentAlignment = Alignment.Center
                ) {
                    if (selected == type) {
                        // halo de selección
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color.White.copy(alpha = 0.12f))
                        ) {}
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    name: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable { }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(color, color.copy(alpha = 0.7f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}


@Composable
fun PlaceCard(
    name: String,
    category: PlaceType,
    rating: Float,
    distance: String,
    imageUrl: String,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Imagen desde URL
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            )

            // Contenido
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (category) {
                        PlaceType.RESTAURANT -> stringResource(R.string.category_restaurant)
                        PlaceType.CAFE -> stringResource(R.string.category_cafe)
                        PlaceType.FAST_FOOD -> stringResource(R.string.category_fastfood)
                        PlaceType.MUSEUM -> stringResource(R.string.category_museum)
                        PlaceType.HOTEL -> stringResource(R.string.category_hotel)
                        PlaceType.BAR -> stringResource(R.string.place_type_bar)
                        PlaceType.PARK -> stringResource(R.string.category_park)
                        PlaceType.SHOPPING -> stringResource(R.string.category_shopping)
                        PlaceType.GAS_STATION -> stringResource(R.string.place_type_gas_station)
                        PlaceType.PHARMACY -> stringResource(R.string.place_type_pharmacy)
                        PlaceType.HOSPITAL -> stringResource(R.string.place_type_hospital)
                        PlaceType.BANK -> stringResource(R.string.place_type_bank)
                        PlaceType.GYM -> stringResource(R.string.place_type_gym)
                        PlaceType.CINEMA -> stringResource(R.string.place_type_cinema)
                        PlaceType.OTHER -> stringResource(R.string.category_other)
                    },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = stringResource(R.string.txt_rating),
                            tint = Accent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = rating.toString(),
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = stringResource(R.string.txt_distance),
                            tint = Accent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = distance,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

