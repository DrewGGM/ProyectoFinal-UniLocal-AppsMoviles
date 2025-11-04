package com.example.primeraplicacionprueba.ui.screens.user.tabs.home.createplace

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.ui.theme.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.primeraplicacionprueba.R
import androidx.compose.ui.res.stringResource
import com.example.primeraplicacionprueba.ui.components.Map
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.mapbox.geojson.Point
import com.example.primeraplicacionprueba.model.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceStepTwo(
    viewModel: PlacesViewModel,
    onNavigateToHome: () -> Unit = {},
    onNavigateToPrevious: () -> Unit = {},
    onNavigateToNext: () -> Unit = {}
) {
    val state by viewModel.createPlaceState.collectAsState()

    var telefonoContacto by remember(state.phones) { mutableStateOf(state.phones.firstOrNull() ?: "") }
    var sitioWeb by remember(state.website) { mutableStateOf(state.website ?: "") }
    var redesSociales by remember(state.socialMedia) { mutableStateOf(state.socialMedia ?: "") }

    var telefonoError by remember { mutableStateOf("") }
    var sitioWebError by remember { mutableStateOf("") }
    var redesSocialesError by remember { mutableStateOf("") }
    var clickedPoint by rememberSaveable { mutableStateOf<Point?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Espacio vacío para balancear el layout
            Spacer(modifier = Modifier.size(48.dp))

            // Título
            Text(
                text = stringResource(R.string.txt_add_new_place),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            // Botón cerrar
            IconButton(onClick = { onNavigateToHome() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.txt_close),
                    tint = TextDark
                )
            }
        }

        // Línea divisoria
        HorizontalDivider(
            thickness = 1.dp,
            color = BorderLight
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BgLight)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Primary,
                                    Secondary
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.txt_step_two_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.txt_location),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 2.dp,
                            color = BorderLight,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(BgLight)
                        .clickable {
                            // TODO: Abrir selector de mapa
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Map(
                            modifierr = Modifier.fillMaxSize(),
                            activateClick = true,
                            onMapClickListener={ l ->
                                clickedPoint = l
                                viewModel.updateLocation(
                                    Location(
                                        latitude = l.latitude(),
                                        longitude = l.longitude()
                                    )
                                )
                            }
                        )

                        Text(
                            text = stringResource(R.string.txt_tap_to_select_map),
                            fontSize = 14.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_contact_phone),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = telefonoContacto,
                    onValueChange = {
                        telefonoContacto = it
                        if (telefonoError.isNotEmpty()) telefonoError = ""
                    },
                    placeholder = { Text(stringResource(R.string.txt_phone_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = telefonoError.isNotEmpty(),
                    supportingText = if (telefonoError.isNotEmpty()) {
                        { Text(telefonoError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderLight,
                        focusedBorderColor = Secondary,
                        unfocusedContainerColor = BgLight,
                        focusedContainerColor = BgLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_website),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = sitioWeb,
                    onValueChange = {
                        sitioWeb = it
                        if (sitioWebError.isNotEmpty()) sitioWebError = ""
                    },
                    placeholder = { Text(stringResource(R.string.txt_website_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    isError = sitioWebError.isNotEmpty(),
                    supportingText = if (sitioWebError.isNotEmpty()) {
                        { Text(sitioWebError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderLight,
                        focusedBorderColor = Secondary,
                        unfocusedContainerColor = BgLight,
                        focusedContainerColor = BgLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_social_media),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = redesSociales,
                    onValueChange = {
                        redesSociales = it
                        if (redesSocialesError.isNotEmpty()) redesSocialesError = ""
                    },
                    placeholder = { Text(stringResource(R.string.txt_social_media_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = redesSocialesError.isNotEmpty(),
                    supportingText = if (redesSocialesError.isNotEmpty()) {
                        { Text(redesSocialesError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderLight,
                        focusedBorderColor = Secondary,
                        unfocusedContainerColor = BgLight,
                        focusedContainerColor = BgLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                        // Validar al menos un dato de contacto (opcional pero recomendado)
                        // No bloqueamos si no hay datos, solo guardamos lo que hay
                        viewModel.updateContactInfo(
                            phone = telefonoContacto,
                            website = sitioWeb,
                            socialMedia = redesSociales,
                            address = "",  // Valor por defecto hasta que se implemente el mapa
                            city = "Armenia",     // Valor por defecto hasta que se implemente el mapa
                            neighborhood = ""  // Valor por defecto hasta que se implemente el mapa
                        )
                        // Asegurar que la ubicación se persista si se seleccionó en el mapa
                        if (clickedPoint != null && state.location == null) {
                            val p = clickedPoint!!
                            viewModel.updateLocation(
                                Location(
                                    latitude = p.latitude(),
                                    longitude = p.longitude()
                                    
                                )
                            )
                        }
                        onNavigateToNext()
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
                            text = stringResource(R.string.txt_next),
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