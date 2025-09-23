package com.example.primeraplicacionprueba.ui.screens.user.tabs.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewPlace(
    onNavigateBack: () -> Unit = {},
    onNavigateToPrevious: () -> Unit = {},
    onNavigateToNext: () -> Unit = {}
) {
    var telefonoContacto by remember { mutableStateOf("") }
    var sitioWeb by remember { mutableStateOf("") }
    var redesSociales by remember { mutableStateOf("") }

    // Estados para validación
    var telefonoError by remember { mutableStateOf("") }
    var sitioWebError by remember { mutableStateOf("") }
    var redesSocialesError by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header con título y botón cerrar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flecha de regreso
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.txt_back),
                    tint = Color(0xFF2C3E50)
                )
            }

            // Título
            Text(
                text = stringResource(R.string.txt_add_new_place),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            // Botón cerrar
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.txt_close),
                    tint = Color(0xFF2C3E50)
                )
            }
        }

        // Línea divisoria
        Divider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp
        )

        // Contenido scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Barra de progreso
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFE0E0E0)),
                horizontalArrangement = Arrangement.Start
            ) {
                // Primera parte (completada) - color naranja
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFF6B6B),
                                    Color(0xFFFFB347)
                                )
                            )
                        )
                )

                // Segunda parte (actual) - color verde/azul
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF4ECDC4),
                                    Color(0xFF44A08D)
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Título del paso
            Text(
                text = stringResource(R.string.txt_step_2_location_contact),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )

            // Sección Ubicación
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.txt_location),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50)
                )

                // Placeholder para el mapa
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 2.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(Color(0xFFF8F9FA))
                        .clickable {
                            // TODO: Abrir selector de mapa
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Icono de pin de mapa simulado
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF4285F4),
                                            Color(0xFF1A73E8)
                                        )
                                    ),
                                    shape = RoundedCornerShape(30.dp, 30.dp, 30.dp, 0.dp)
                                )
                                .clip(RoundedCornerShape(30.dp, 30.dp, 30.dp, 0.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color.White, RoundedCornerShape(10.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.txt_tap_to_select_map),
                            fontSize = 14.sp,
                            color = Color(0xFF7F8C8D),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Campo Teléfono de Contacto
            Column {
                Text(
                    text = stringResource(R.string.txt_contact_phone),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50),
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
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFF4ECDC4),
                        unfocusedContainerColor = Color(0xFFF8F9FA),
                        focusedContainerColor = Color(0xFFF8F9FA)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Campo Sitio Web
            Column {
                Text(
                    text = stringResource(R.string.txt_website),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50),
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
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFF4ECDC4),
                        unfocusedContainerColor = Color(0xFFF8F9FA),
                        focusedContainerColor = Color(0xFFF8F9FA)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Campo Redes Sociales
            Column {
                Text(
                    text = stringResource(R.string.txt_social_networks),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = redesSociales,
                    onValueChange = {
                        redesSociales = it
                        if (redesSocialesError.isNotEmpty()) redesSocialesError = ""
                    },
                    placeholder = { Text(stringResource(R.string.txt_social_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = redesSocialesError.isNotEmpty(),
                    supportingText = if (redesSocialesError.isNotEmpty()) {
                        { Text(redesSocialesError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFF4ECDC4),
                        unfocusedContainerColor = Color(0xFFF8F9FA),
                        focusedContainerColor = Color(0xFFF8F9FA)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón Anterior
                OutlinedButton(
                    onClick = { onNavigateToPrevious() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFE0E0E0), Color(0xFFE0E0E0))
                        )
                    )
                ) {
                    Text(
                        text = stringResource(R.string.btn_previous),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF7F8C8D)
                    )
                }

                // Botón Siguiente
                Button(
                    onClick = { onNavigateToNext() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFF6B6B),
                                        Color(0xFFFFB347)
                                    )
                                ),
                                shape = RoundedCornerShape(28.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.btn_next),
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