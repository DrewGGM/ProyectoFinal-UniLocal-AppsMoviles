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
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceStatus
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.model.UserPlace
import com.example.primeraplicacionprueba.ui.theme.*
import com.example.primeraplicacionprueba.R




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPlacesScreen(
    places: List<UserPlace> = emptyList(),
    onNavigateBack: () -> Unit = {},
    onAddPlace: () -> Unit = {},
    onEditPlace: (String) -> Unit = {},
    onDeletePlace: (String) -> Unit = {},
    onViewComments: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.txt_my_places_title),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_back),
                            tint = TextDark
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAddPlace) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.txt_add_place),
                            tint = Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (places.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BgLight)
                    .padding(paddingValues),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(places) { userPlace ->
                    MyPlaceCard(
                        userPlace = userPlace,
                        onEdit = { onEditPlace(userPlace.place.id) },
                        onDelete = { onDeletePlace(userPlace.place.id) },
                        onViewComments = { onViewComments(userPlace.place.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MyPlaceCard(
    userPlace: UserPlace,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewComments: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen/Icono del lugar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Tertiary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getIconForPlaceType(userPlace.place.type),
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Información del lugar
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = userPlace.place.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Text(
                    text = "Creado el ${userPlace.createdDate}",
                    fontSize = 14.sp,
                    color = TextMuted
                )
                StatusBadge(status = userPlace.status)
            }

            // Acciones
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ActionButton(
                    icon = Icons.AutoMirrored.Filled.Comment,
                    tint = TextDark,
                    onClick = onViewComments
                )
                ActionButton(
                    icon = Icons.Default.Edit,
                    tint = Secondary,
                    onClick = onEdit
                )
                ActionButton(
                    icon = Icons.Default.Delete,
                    tint = ErrorRed,
                    onClick = onDelete
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: PlaceStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        PlaceStatus.APPROVED -> Triple(Tertiary, SuccessGreen, stringResource(R.string.txt_place_status_approved))
        PlaceStatus.PENDING -> Triple(Accent, WarningYellow, stringResource(R.string.txt_place_status_pending))
        PlaceStatus.REJECTED -> Triple(ErrorRed.copy(alpha = 0.2f), ErrorRed, stringResource(R.string.txt_place_status_rejected))
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(BgLight)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.StoreMallDirectory,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(R.string.txt_no_places_created_yet),
                fontSize = 16.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.txt_tap_plus_to_create_place),
                fontSize = 14.sp,
                color = TextMuted
            )
        }
    }
}

fun getIconForPlaceType(type: PlaceType): ImageVector {
    return when (type) {
        PlaceType.RESTAURANT -> Icons.Default.Restaurant
        PlaceType.CAFE -> Icons.Default.Coffee
        PlaceType.HOTEL -> Icons.Default.Hotel
        PlaceType.MUSEUM -> Icons.Default.Museum
        PlaceType.FAST_FOOD -> Icons.Default.Fastfood
        else -> Icons.Default.Place
    }
}