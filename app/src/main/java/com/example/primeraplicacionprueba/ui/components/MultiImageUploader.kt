package com.example.primeraplicacionprueba.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.theme.SuccessGreen
import com.example.primeraplicacionprueba.utils.CloudinaryHelper
import kotlinx.coroutines.launch

@Composable
fun MultiImageUploader(
    imageUrls: List<String> = emptyList(),
    onImagesChanged: (List<String>) -> Unit,
    folder: String = "unilocal/reviews",
    maxImages: Int = 5,
    modifier: Modifier = Modifier
) {
    var currentImageUrls by remember { mutableStateOf(imageUrls) }
    var uploadingCount by remember { mutableStateOf(0) }
    var uploadError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val remainingSlots = maxImages - currentImageUrls.size
            val urisToUpload = uris.take(remainingSlots)

            uploadingCount = urisToUpload.size
            uploadError = null

            scope.launch {
                try {
                    val newUrls = mutableListOf<String>()
                    urisToUpload.forEach { uri ->
                        val url = CloudinaryHelper.uploadImage(uri, folder)
                        newUrls.add(url)
                    }

                    val updatedUrls = currentImageUrls + newUrls
                    currentImageUrls = updatedUrls
                    onImagesChanged(updatedUrls)
                    uploadingCount = 0
                } catch (e: Exception) {
                    uploadError = e.message ?: "Error al subir imágenes"
                    uploadingCount = 0
                }
            }
        }
    }

    Column(modifier = modifier) {
        // Add Photos Button
        if (currentImageUrls.size < maxImages) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(
                        width = 2.dp,
                        color = SuccessGreen.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(enabled = uploadingCount == 0) {
                        imagePickerLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (uploadingCount > 0) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            color = SuccessGreen
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.txt_uploading_images, uploadingCount),
                            fontSize = 12.sp,
                            color = SuccessGreen
                        )
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.txt_add_photos_count, currentImageUrls.size, maxImages),
                            fontSize = 14.sp,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Error message
        if (uploadError != null) {
            Text(
                text = uploadError!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Display selected images
        if (currentImageUrls.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentImageUrls.size) { index ->
                    Box(
                        modifier = Modifier.size(100.dp)
                    ) {
                        // Image
                        AsyncImage(
                            model = currentImageUrls[index],
                            contentDescription = stringResource(R.string.cd_image_index, index + 1),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentScale = ContentScale.Crop
                        )

                        // Remove button
                        IconButton(
                            onClick = {
                                val updatedUrls = currentImageUrls.filterIndexed { i, _ -> i != index }
                                currentImageUrls = updatedUrls
                                onImagesChanged(updatedUrls)
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.cd_remove),
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
