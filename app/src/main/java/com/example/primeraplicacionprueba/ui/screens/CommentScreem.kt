package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Review
import com.example.primeraplicacionprueba.viewmodel.ReviewViewModel
import com.example.primeraplicacionprueba.ui.components.MultiImageUploader
import com.google.firebase.Timestamp
import java.time.LocalDateTime

@Composable
fun CommentScreen(
    placeId: String,
    userId: String,
    username: String,
    reviewViewModel: ReviewViewModel,
    onNavigateBack: () -> Unit = {}
) {
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var selectedPhotos by remember { mutableStateOf<List<String>>(emptyList()) }

    fun saveComment() {
        if (rating > 0 && comment.isNotBlank()) {
            val newReview = Review(
                id = "", // Firebase will auto-generate
                userID = userId,
                username = username,
                placeID = placeId,
                rating = rating,
                comment = comment,
                date = Timestamp.now(),
                imageUrls = selectedPhotos
            )
            reviewViewModel.create(newReview)
            onNavigateBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Fondo oscuro como en la imagen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // espacio para el botón
        ) {
            // Header con fondo blanco como en la imagen
            Surface(
                modifier = Modifier.fillMaxWidth()
                    .height(150.dp)
                    .padding(bottom = 20.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.txt_go_back),
                            tint = Color(0xFF333333)
                        )
                    }

                    Text(
                        text = stringResource(R.string.txt_write_comment_title),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.cd_close),
                            tint = Color(0xFF333333)
                        )
                    }
                }
            }

            // Main Content
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 50.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    //  Rating Section
                    RatingSection(
                        rating = rating,
                        onRatingChanged = { rating = it }
                    )

                    //  Comment Section
                    CommentSection(
                        comment = comment,
                        onCommentChanged = { comment = it }
                    )

                    //  Photo Section
                    PhotoSection(
                        selectedPhotos = selectedPhotos,
                        onPhotosChanged = { selectedPhotos = it }
                    )
                }
            }
        }

        // 🚀 Send Button
        Button(
            onClick = { saveComment() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(18.dp, bottom = 50.dp, end = 18.dp)
                .height(65.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFF6B35), Color(0xFFFFB800))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.txt_send_comment),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RatingSection(
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.txt_rate_this_place),
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(5) { index ->
                Icon(
                    imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = null,
                    tint = if (index < rating) Color(0xFFFFB800) else Color(0xFFBDBDBD),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onRatingChanged(index + 1) }
                )
            }
        }
    }
}

@Composable
fun CommentSection(
    comment: String,
    onCommentChanged: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.txt_your_experience),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            BasicTextField(
                value = comment,
                onValueChange = onCommentChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(120.dp),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color(0xFF333333)
                ),
                decorationBox = { innerTextField ->
                    if (comment.isEmpty()) {
                        Text(
                            text = stringResource(R.string.txt_share_opinion_placeholder),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun PhotoSection(
    selectedPhotos: List<String>,
    onPhotosChanged: (List<String>) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(15.dp))

        MultiImageUploader(
            imageUrls = selectedPhotos,
            onImagesChanged = onPhotosChanged,
            folder = "unilocal/reviews",
            maxImages = 5
        )
    }
}
