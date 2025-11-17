package com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import coil.compose.AsyncImage
import com.example.primeraplicacionprueba.R
import androidx.compose.ui.res.stringResource
import com.example.primeraplicacionprueba.ui.theme.*
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.ui.components.MultipleImageUploader


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceStepFour(
    viewModel: PlacesViewModel,
    onNavigateToHome: () -> Unit = {},
    onNavigateToPrevious: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val state by viewModel.createPlaceState.collectAsState()

    var selectedImages by remember(state.images) { mutableStateOf(state.images) }
    var imageUrl by remember { mutableStateOf("") }
    var urlError by remember { mutableStateOf("") }
    
    val urlErrorInvalid = stringResource(R.string.txt_url_error_invalid)
    val urlErrorEmpty = stringResource(R.string.txt_url_error_empty)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(48.dp))
            Text(
                text = stringResource(R.string.txt_add_new_place),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { onNavigateToHome() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.txt_close),
                    tint = TextDark
                )
            }
        }

            HorizontalDivider(color = BorderLight, thickness = 1.dp)

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Barra de progreso - 100%
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Primary,
                                Secondary
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.txt_step_four_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = stringResource(R.string.txt_gallery_description),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            // Sección para agregar URLs
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.txt_or_add_urls),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = {
                            imageUrl = it
                            if (urlError.isNotEmpty()) urlError = ""
                        },
                        placeholder = { Text(stringResource(R.string.txt_image_url_placeholder)) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        isError = urlError.isNotEmpty(),
                        supportingText = if (urlError.isNotEmpty()) {
                            { Text(urlError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderLight,
                            focusedBorderColor = Secondary,
                            unfocusedContainerColor = BgLight,
                            focusedContainerColor = BgLight
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    Button(
                        onClick = {
                            if (imageUrl.isNotBlank()) {
                                if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                                    selectedImages = selectedImages + imageUrl
                                    imageUrl = ""
                                    urlError = ""
                                } else {
                                    urlError = urlErrorInvalid
                                }
                            } else {
                                urlError = urlErrorEmpty
                            }
                        },
                        modifier = Modifier.height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Secondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.txt_add_url),
                            tint = Color.White
                        )
                    }
                }
            }

            // Cloudinary Image Uploader
            MultipleImageUploader(
                imageUrls = selectedImages,
                onImagesUploaded = { urls ->
                    selectedImages = urls
                },
                maxImages = 10,
                folder = "places"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { onNavigateToPrevious() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        brush = Brush.horizontalGradient(
                            colors = listOf(BorderLight, BorderLight)
                        )
                    )
                ) {
                    Text(
                        text = stringResource(R.string.txt_previous),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = {
                        // Guardar imágenes en el ViewModel antes de enviar
                        viewModel.updateCreateState(state.copy(images = selectedImages))
                        onSubmit()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Primary,
                                        AccentOrange
                                    )
                                ),
                                shape = RoundedCornerShape(28.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.txt_submit_place),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}