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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.theme.*
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.viewmodel.UsersViewModel

@Composable
fun Home(
    onNavigateToCreatePlace: () -> Unit = {},
    onNavigateToPlace: () -> Unit = {},
    placesViewModel: PlacesViewModel,
    user: User
) {

    val places by placesViewModel.places.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BgLight)
        ) {
            // Header
            item {
                HeaderSection(
                    userName = user.nombre,
                    onSearchClick = { }
                )
            }

            // Sección de Categorías
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.txt_categories),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
                CategoriesSection()
            }

            // Sección de Lugares Populares
            item {
                Text(
                    text = stringResource(R.string.txt_popular_places),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }

            // Lista de lugares (placeholder)
            items(places) {
                PlaceCard(
                    name = it.title ,
                    category = it.type ,
                    rating = it.rating ,
                    distance = it.getDistanceFromUser(),
                    imageUrl = it.imagenes[0],
                    onClick = { onNavigateToPlace() }
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

@Composable
fun HeaderSection(
    userName: String,
    onSearchClick: () -> Unit
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

        // Barra búsqueda
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 24.dp)
                .clickable { onSearchClick() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.txt_search),
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.txt_search_hint),
                    color = TextSecondary,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun CategoriesSection() {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CategoryItem(stringResource(R.string.category_restaurant), Icons.Filled.Restaurant, Secondary)
        CategoryItem(stringResource(R.string.category_cafe), Icons.Filled.Coffee, Accent)
        CategoryItem(stringResource(R.string.category_fastfood), Icons.Filled.Fastfood, Primary)
        CategoryItem(stringResource(R.string.category_museum), Icons.Filled.Museum, Tertiary)
        CategoryItem(stringResource(R.string.category_hotel), Icons.Filled.Hotel, Color(0xFF81C784))
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
            color = TextMuted,
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.toString(),
                    fontSize = 14.sp,
                    color = TextMuted
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

