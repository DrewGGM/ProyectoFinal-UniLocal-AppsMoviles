package com.example.primeraplicacionprueba.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.primeraplicacionprueba.utils.CloudinaryHelper
import kotlinx.coroutines.launch

@Composable
fun MultipleImageUploader(
    imageUrls: List<String> = emptyList(),
    onImagesUploaded: (List<String>) -> Unit,
    maxImages: Int = 5,
    folder: String = "places",
    modifier: Modifier = Modifier
) {
    var uploadedImages by remember { mutableStateOf(imageUrls.toMutableList()) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isUploading = true
            uploadError = null

            scope.launch {
                try {
                    val url = CloudinaryHelper.uploadImage(it, folder)
                    uploadedImages.add(url)
                    onImagesUploaded(uploadedImages)
                    isUploading = false
                } catch (e: Exception) {
                    uploadError = e.message ?: "Upload failed"
                    isUploading = false
                }
            }
        }
    }

    Column(modifier = modifier) {
        Text(
            text = "Fotos del lugar (${uploadedImages.size}/$maxImages)",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uploadedImages.forEach { url ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(url),
                        contentDescription = "Uploaded image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = {
                            uploadedImages.remove(url)
                            onImagesUploaded(uploadedImages)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(24.dp)
                            .background(
                                Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (uploadedImages.size < maxImages) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 2.dp,
                            color = if (uploadError != null) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(enabled = !isUploading) {
                            imagePickerLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add image",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        if (uploadError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = uploadError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
