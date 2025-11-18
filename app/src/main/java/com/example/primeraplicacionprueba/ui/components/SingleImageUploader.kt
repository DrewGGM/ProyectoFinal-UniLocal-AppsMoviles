package com.example.primeraplicacionprueba.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.theme.Secondary
import com.example.primeraplicacionprueba.ui.theme.TextMuted
import com.example.primeraplicacionprueba.utils.CloudinaryHelper
import kotlinx.coroutines.launch

@Composable
fun SingleImageUploader(
    imageUrl: String? = null,
    onImageUploaded: (String) -> Unit,
    folder: String = "unilocal/profiles",
    modifier: Modifier = Modifier
) {
    var currentImageUrl by remember { mutableStateOf(imageUrl) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isUploading = true
            uploadError = null

            scope.launch {
                try {
                    val url = CloudinaryHelper.uploadImage(it, folder)
                    currentImageUrl = url
                    onImageUploaded(url)
                    isUploading = false
                } catch (e: Exception) {
                    uploadError = e.message ?: "Error al subir imagen"
                    isUploading = false
                }
            }
        }
    }

    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(3.dp, Secondary, CircleShape)
            .background(Color(0xFFE0E0E0))
            .clickable(enabled = !isUploading) {
                imagePickerLauncher.launch("image/*")
            },
        contentAlignment = Alignment.Center
    ) {
        if (isUploading) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = Secondary
            )
        } else if (currentImageUrl != null && currentImageUrl!!.isNotEmpty()) {
            AsyncImage(
                model = currentImageUrl,
                contentDescription = stringResource(R.string.cd_profile_photo),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = stringResource(R.string.cd_change_photo),
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.cd_profile_photo),
                modifier = Modifier.size(60.dp),
                tint = TextMuted
            )
        }
    }

    if (uploadError != null) {
        Text(
            text = uploadError!!,
            color = MaterialTheme.colorScheme.error,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

