package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.model.Review
import com.example.primeraplicacionprueba.model.ReviewReply
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.viewmodel.ReviewViewModel
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCommentScreen(
    placeId: String,
    placesViewModel: PlacesViewModel,
    reviewViewModel: ReviewViewModel,
    onNavigateBack: () -> Unit
) {
    val place = placesViewModel.findById(placeId)
    val reviews by reviewViewModel.reviews.collectAsState()
    val placeReviews = reviews.filter { it.placeID == placeId }
    
    // Obtener usuario actual para las respuestas
    val mainViewModel = LocalMainViewModel.current
    val currentUser by mainViewModel.usersViewModel.currentUser.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Comentarios de '${place?.title ?: "Mi Lugar"}'",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (placeReviews.isNotEmpty()) {
                items(placeReviews) { review ->
                    CommentCard(
                        review = review,
                        onReply = { replyText ->
                            // Crear respuesta usando ReviewViewModel
                            val newReply = ReviewReply(
                                id = "", // Firebase will auto-generate
                                reviewId = review.id,
                                userID = currentUser?.id ?: "unknown_user",
                                username = place?.title ?: "Mi Lugar", // Usar nombre del lugar
                                replyText = replyText,
                                date = com.google.firebase.Timestamp.now()
                            )
                            reviewViewModel.addReplyToReview(review.id, newReply)
                        }
                    )
                }
            } else {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = "No hay comentarios para este lugar",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommentCard(
    review: Review,
    onReply: (String) -> Unit
) {
    var replyText by remember { mutableStateOf("") }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header del comentario
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                            .background(Color(0xFFE3F2FD)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Avatar",
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    // Username
                    Text(
                        text = review.username,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                
                // Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = if (index < review.rating) Color(0xFFFFB800) else Color(0xFFE0E0E0),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            // Comentario
            Text(
                text = review.comment,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )
            
            // Timestamp
            Text(
                text = "2min",
                fontSize = 12.sp,
                color = Color.Gray
            )
            
            // Mostrar respuestas existentes
            // TODO: Load replies from Firebase using review.replyIds
            if (review.replyIds.isNotEmpty()) {
                Text(
                    text = "${review.replyIds.size} respuesta(s)",
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                // Note: Replies need to be loaded from Firebase separately
                // Use reviewViewModel.loadRepliesForReview(review.id) { replies -> ... }
            }
            
            // Campo de respuesta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = replyText,
                    onValueChange = { replyText = it },
                    placeholder = { Text("Escribe tu respuesta...", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                
                IconButton(
                    onClick = { 
                        if (replyText.isNotBlank()) {
                            onReply(replyText)
                            replyText = ""
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE53E3E), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Enviar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


