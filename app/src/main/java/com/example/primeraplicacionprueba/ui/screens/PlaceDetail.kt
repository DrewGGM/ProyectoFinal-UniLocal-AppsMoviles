package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.theme.*
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.primeraplicacionprueba.viewmodel.ReviewViewModel
import com.example.primeraplicacionprueba.model.ReviewReply

@Composable
fun PlaceDetail(
    placesViewModel: PlacesViewModel,
    reviewViewModel: ReviewViewModel,
    id: String,
    onNavigateBack: () -> Unit = {},
    onNavigateToComment: (String) -> Unit = {}

) {
    val place = LocalMainViewModel.current.placesViewModel.findById(id)
    val reviews by reviewViewModel.reviews.collectAsState()
    val placeReviews = remember(reviews, id) { reviews.filter { it.placeID == id } }

    if (place == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.txt_place_not_found),
                fontSize = 18.sp,
                color = TextMuted
            )
        }
        return
    }

    val mainViewModel = LocalMainViewModel.current
    val usersViewModel = mainViewModel.usersViewModel
    LaunchedEffect(id) {
        usersViewModel.registerVisited(id)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(BgLight)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de regreso
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.txt_back),
                        tint = TextDark
                    )
                }
                
                // Título del lugar
                Text(
                    text = place.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                // Espacio para balancear el layout
                Spacer(modifier = Modifier.size(48.dp))
            }
            
            // Línea divisoria
            HorizontalDivider(
                color = BorderLight,
                thickness = 1.dp
            )
            
            // Contenido scrollable
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
            // Hero Gallery
            item {
                HeroGallery(images = place.imagenes)
            }

            // Detail Container
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-32).dp)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {
                    // Place Header
                    PlaceHeader(
                        title = place.title,
                        isOpen = place.isCurrentlyOpen(),
                        closeTime = place.getFormattedCloseTime(),
                        rating = remember(reviews, id) { placesViewModel.getAverageRatingForPlace(id) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Acerca de este lugar
                    SectionTitle(title = stringResource(R.string.txt_about_this_place))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = place.description,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Información y Contacto
                    SectionTitle(title = stringResource(R.string.txt_information_contact))
                    Spacer(modifier = Modifier.height(12.dp))
                    ContactGrid(
                        phones = place.phones,
                        address = place.adress,
                        website = place.website,
                        email = place.email,
                        socialMedia = place.socialMedia
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Horarios de atención
                    if (place.shedule.isNotEmpty()) {
                        SectionTitle(title = stringResource(R.string.txt_step_three_title))
                        Spacer(modifier = Modifier.height(12.dp))
                        ScheduleSection(schedules = place.shedule)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Servicios y Amenidades
                    if (place.amenities.isNotEmpty()) {
                        SectionTitle(title = stringResource(R.string.txt_amenities))
                        Spacer(modifier = Modifier.height(12.dp))
                        AmenitiesGrid(amenities = place.amenities)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Ubicación
                    SectionTitle(title = stringResource(R.string.txt_location))
                    Spacer(modifier = Modifier.height(12.dp))
                    MapPlaceholder()

                    Spacer(modifier = Modifier.height(24.dp))

                    // Comentarios
                    SectionTitle(title = stringResource(R.string.txt_comments, placeReviews.size))
                    Spacer(modifier = Modifier.height(12.dp))
                    // Mostrar todas las reseñas del lugar
                    if (placeReviews.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            placeReviews.forEach { review ->
                                ReviewCard(
                                    userName = review.username,
                                    rating = review.rating.toFloat(),
                                    comment = review.comment,
                                    avatarColor = if (review.rating >= 4) Tertiary else Primary,
                                    replies = review.replies
                                )
                            }
                        }
                    } else {
                        // Mostrar mensaje si no hay reseñas
                        Text(
                            text = stringResource(R.string.txt_no_reviews),
                            fontSize = 14.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(100.dp)) // Espacio para FABs
                }
            }
            }
        }

        // FAB Group
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val currentUser by usersViewModel.currentUser.collectAsState()
            val isFavorite = currentUser?.favorites?.any { it.id == place.id } == true
            FloatingActionButton(
                onClick = {
                    usersViewModel.toggleFavorite(place)
                },
                containerColor = Color.White,
                contentColor = if (isFavorite) Primary else TextDark,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.txt_favorites),
                    tint = if (isFavorite) Primary else TextDark
                )
            }
            FloatingActionButton(
                onClick = { /* Compartir */ },
                containerColor = Primary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.txt_share)
                )
            }
            FloatingActionButton(
                onClick = { onNavigateToComment(id) },
                containerColor = Primary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Comment,
                    contentDescription = stringResource(R.string.txt_comments)
                )
            }
        }
    }
}

@Composable
fun HeroGallery(images: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        images.forEach { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.txt_place_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(400.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun PlaceHeader(
    title: String,
    isOpen: Boolean,
    closeTime: String,
    rating: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isOpen) stringResource(R.string.txt_open) + " • " + stringResource(R.string.txt_closes_at, closeTime) else stringResource(R.string.txt_closed),
                fontSize = 14.sp,
                color = TextMuted
            )
        }

        // Rating Chip
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Accent,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = rating.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(R.string.txt_rating),
                    tint = TextDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextDark
    )
}

@Composable
fun ContactGrid(
    phones: List<String>,
    address: String,
    website: String? = null,
    email: String? = null,
    socialMedia: String? = null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Mostrar todos los teléfonos
        phones.forEach { phone ->
            ContactItem(
                icon = Icons.Default.Phone,
                text = phone,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Si no hay teléfonos, mostrar string del resources
        if (phones.isEmpty()) {
            ContactItem(
                icon = Icons.Default.Phone,
                text = stringResource(R.string.txt_not_available),
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Website
        ContactItem(
            icon = Icons.Default.Language,
            text = website ?: stringResource(R.string.txt_not_available),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Email
        ContactItem(
            icon = Icons.Default.Email,
            text = email ?: stringResource(R.string.txt_not_available),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Redes sociales
        ContactItem(
            icon = Icons.Default.Public,
            text = socialMedia ?: stringResource(R.string.txt_not_available),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ContactItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = BgLight
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                fontSize = 13.sp,
                color = TextDark,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(BgLight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.txt_menumap),
            fontSize = 14.sp,
            color = TextSecondary,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun ReviewCard(
    userName: String,
    rating: Float,
    comment: String,
    avatarColor: Color,
    replies: List<ReviewReply> = emptyList()
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = BgLight,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.first().uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = userName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        val fullStars = rating.toInt()
                        val hasHalfStar = rating % 1 != 0f

                        repeat(fullStars) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Accent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        if (hasHalfStar) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                                contentDescription = null,
                                tint = Accent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comment,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )
            
            // Mostrar respuestas si existen
            if (replies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    replies.forEach { reply ->
                        ReplyCard(reply = reply)
                    }
                }
            }
        }
    }
}

@Composable
fun ReplyCard(reply: ReviewReply) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Avatar pequeño
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Secondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = reply.username.first().uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            
            Column {
                Text(
                    text = reply.username,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = reply.replyText,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun AmenitiesGrid(amenities: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        amenities.chunked(2).forEach { rowAmenities ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowAmenities.forEach { amenity ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = BgLight
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = amenity,
                                fontSize = 12.sp,
                                color = TextDark,
                                maxLines = 1
                            )
                        }
                    }
                }
                // Rellenar con espacio vacío si hay un número impar de amenidades
                if (rowAmenities.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ScheduleSection(schedules: List<com.example.primeraplicacionprueba.model.Shedule>) {
    val dayOrder = listOf(
        com.example.primeraplicacionprueba.model.Day.MONDAY,
        com.example.primeraplicacionprueba.model.Day.TUESDAY,
        com.example.primeraplicacionprueba.model.Day.WEDNESDAY,
        com.example.primeraplicacionprueba.model.Day.THURSDAY,
        com.example.primeraplicacionprueba.model.Day.FRIDAY,
        com.example.primeraplicacionprueba.model.Day.SATURDAY,
        com.example.primeraplicacionprueba.model.Day.SUNDAY,
    )

    val scheduleByDay = schedules.associateBy { it.day }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        dayOrder.forEach { day ->
            val entry = scheduleByDay[day]
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BgLight
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = day.name.lowercase().replaceFirstChar { it.titlecase() },
                        color = TextDark,
                        fontSize = 14.sp
                    )
                    Text(
                        text = entry?.let { stringResource(R.string.txt_schedule_format, it.open.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")), it.close.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))) }
                            ?: stringResource(id = R.string.txt_closed),
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}