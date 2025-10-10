package com.example.primeraplicacionprueba.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceStatus

@Composable
fun HomeAdmin(
    placesViewModel: PlacesViewModel
) {
    val places by placesViewModel.places.collectAsState()
    val pendingCount = places.count { it.placeStatus == PlaceStatus.PENDING }
    val approvedCount = places.count { it.placeStatus == PlaceStatus.APPROVED }
    val rejectedCount = places.count { it.placeStatus == PlaceStatus.REJECTED }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.txt_moderation_panel),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                
                // User icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF333333)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Usuario",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Statistics Cards
        item {
            StatisticsCards(pendingCount, approvedCount, rejectedCount)
        }

        // Review Queue Section
        item {
            ReviewQueueSection(placesViewModel)
        }

        // Moderation History Section
        item {
            ModerationHistorySection(placesViewModel)
        }
        
    }
}

@Composable
fun StatisticsCards(
    pendingCount: Int,
    approvedCount: Int,
    rejectedCount: Int
) {
    val reportsCount = 0 // Por simplicidad, no tenemos lugares reportados
    val problemsCount = 0 // Por simplicidad, no tenemos lugares problemáticos
    
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                number = pendingCount.toString(),
                label = stringResource(R.string.txt_pending),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                number = approvedCount.toString(),
                label = stringResource(R.string.txt_approved_today),
                modifier = Modifier.weight(1f)
            )
        }
        
        // Second row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                number = reportsCount.toString(),
                label = stringResource(R.string.txt_reports),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                number = problemsCount.toString(),
                label = stringResource(R.string.txt_problems),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatCard(
    number: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE53E3E)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
fun ReviewQueueSection(placesViewModel: PlacesViewModel) {
    val places by placesViewModel.places.collectAsState()
    val pendingPlaces = places.filter { it.placeStatus == PlaceStatus.PENDING }
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.txt_review_queue),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            
            Button(
                onClick = { /* Filter action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_filters),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Filtros",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Review items - mostrar lugares pendientes reales
        if (pendingPlaces.isNotEmpty()) {
            pendingPlaces.forEach { place ->
                ReviewItem(
                    placeName = place.title,
                    createdBy = "Usuario ${place.ownerId}",
                    timeAgo = "Hace 5 min", // Por simplicidad, tiempo fijo
                    onApprove = { placesViewModel.approvePlace(place.id) },
                    onReject = { placesViewModel.rejectPlace(place.id) }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            // Mostrar mensaje si no hay lugares pendientes
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "No hay lugares pendientes de revisión",
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ReviewItem(
    placeName: String,
    createdBy: String,
    timeAgo: String,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = placeName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.txt_created_by, createdBy) + " • $timeAgo",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Approve button
                IconButton(
                    onClick ={ onApprove    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.txt_approve),
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                // Reject button
                IconButton(
                    onClick = onReject,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE53E3E))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.txt_reject),
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ModerationHistorySection(placesViewModel: PlacesViewModel) {
    val places by placesViewModel.places.collectAsState()
    val approvedPlaces = places.filter { it.placeStatus == PlaceStatus.APPROVED }
    val rejectedPlaces = places.filter { it.placeStatus == PlaceStatus.REJECTED }
    
    Column {
        Text(
            text = stringResource(R.string.txt_my_moderation_history),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Mostrar lugares aprobados recientemente
        approvedPlaces.forEach { place ->
            HistoryItem(
                placeName = place.title,
                action = stringResource(R.string.txt_authorized_by_you),
                timeAgo = "Recién aprobado",
                status = stringResource(R.string.txt_approved),
                statusColor = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // Mostrar lugares rechazados recientemente
        rejectedPlaces.forEach { place ->
            HistoryItem(
                placeName = place.title,
                action = stringResource(R.string.txt_rejected_by_you),
                timeAgo = "Recién rechazado",
                status = stringResource(R.string.txt_rejected),
                statusColor = Color(0xFFFF6B6B)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // Si no hay historial, mostrar mensaje
        if (approvedPlaces.isEmpty() && rejectedPlaces.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "No hay historial de moderación aún",
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun HistoryItem(
    placeName: String,
    action: String,
    timeAgo: String,
    status: String,
    statusColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = placeName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$action • $timeAgo",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
            
            // Status badge
            Surface(
                color = statusColor,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = status,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}