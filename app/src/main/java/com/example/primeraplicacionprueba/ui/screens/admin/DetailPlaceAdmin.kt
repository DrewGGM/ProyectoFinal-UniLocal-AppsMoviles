package com.example.primeraplicacionprueba.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailPlaceAdmin(
    id: String,
    placesViewModel: PlacesViewModel,
    onNavigateBack: () -> Unit
) {
    val places by placesViewModel.places.collectAsState()
    val place = places.find { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (place != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(10.dp),
                        onClick = { placesViewModel.rejectPlace(place.id); onNavigateBack() }
                    ) {
                        Text(stringResource(R.string.txt_reject), color = Color.White)
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(10.dp),
                        onClick = { placesViewModel.approvePlace(place.id); onNavigateBack() }
                    ) {
                        Text(stringResource(R.string.txt_approve), color = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        if (place == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.txt_place_not_found))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF4F4F4))
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // HEADER tipo banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                Color(0xFFB2E6D4),
                                RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = place.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.txt_created_by, place.ownerId),
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                item {
                    // Descripción
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(10.dp, bottom = 10.dp, end =  10.dp, top = 10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(25.dp)) {
                            Text(stringResource(R.string.txt_description), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = place.description,
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }

                item {
                    // Información y contacto
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(10.dp, bottom = 10.dp, end =  10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

                    ) {
                        Column(Modifier.padding(25.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(stringResource(R.string.txt_information_contact), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

                            InfoRow(Icons.Filled.Category, stringResource(R.string.txt_category_label, place.type))
                            InfoRow(Icons.Filled.Call, stringResource(R.string.txt_phone_label, place.phones.firstOrNull() ?: stringResource(R.string.txt_not_available_short)))
                            InfoRow(Icons.Filled.Public, stringResource(R.string.txt_web_label, place.website ?: stringResource(R.string.txt_not_available_short)))
                            InfoRow(Icons.Filled.Schedule, stringResource(R.string.txt_open_today, "7:00 AM - 8:00 PM"))
                        }
                    }
                }

                item {
                    // Ubicación
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(10.dp, bottom = 10.dp, end =  10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(25.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(stringResource(R.string.txt_location), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                color = Color(0xFFE3F2FD),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.mapa),
                                    contentDescription = null,
                                )
                            }
                            Text(place.adress, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF4CAF50))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.DarkGray)
    }
}
