package com.example.primeraplicacionprueba.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.model.PlaceStatus
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPerfilAdmin(
    user: User,
    placesViewModel: PlacesViewModel,
    onNavigateBack: () -> Unit
) {
    val places by placesViewModel.places.collectAsState()
    
    // Datos del usuario recibidos por parámetro
    val userName = user.nombre
    val userRole = user.rol.name
    
    // Estadísticas reales del administrador
    val approvedCount = places.count { it.placeStatus == PlaceStatus.APPROVED }
    val rejectedCount = places.count { it.placeStatus == PlaceStatus.REJECTED }
    val pendingCount = places.count { it.placeStatus == PlaceStatus.PENDING }
    val reportsHandled = 3 // Por simplicidad, mantengo fijo
    val reputationPoints = (approvedCount * 2) + (rejectedCount * 1) + (reportsHandled * 5) // Cálculo basado en actividad

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Mi Perfil",
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
                .background(Color.White)
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Banner del perfil con gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFE91E63), // Rosa-rojo
                                    Color(0xFF4DB6AC)  // Verde-azul
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar circular
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Avatar",
                                tint = Color(0xFFFF5722),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Nombre del usuario
                        Text(
                            text = userName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        // Rol del usuario
                        Text(
                            text = userRole,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            
            item {
                // Grid de estadísticas 2x2
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fila superior
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Tarjeta Aprobados
                        StatCard(
                            modifier = Modifier.weight(1f),
                            number = approvedCount.toString(),
                            label = "Aprobados",
                            color = Color(0xFFFF5722)
                        )
                        
                        // Tarjeta Rechazados
                        StatCard(
                            modifier = Modifier.weight(1f),
                            number = rejectedCount.toString(),
                            label = "Rechazados",
                            color = Color(0xFFFF5722)
                        )
                    }
                    
                    // Fila inferior
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Tarjeta Reportes manejados
                        StatCard(
                            modifier = Modifier.weight(1f),
                            number = reportsHandled.toString(),
                            label = "Reportes manejados",
                            color = Color(0xFFFF5722)
                        )
                        
                        // Tarjeta Puntos de reputación
                        StatCard(
                            modifier = Modifier.weight(1f),
                            number = reputationPoints.toString(),
                            label = "Puntos de reputación",
                            color = Color(0xFFFF5722)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = number,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}