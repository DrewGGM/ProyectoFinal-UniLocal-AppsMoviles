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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceStatus
import com.example.primeraplicacionprueba.ui.theme.AdminBackground
import com.example.primeraplicacionprueba.ui.theme.AdminPrimary
import com.example.primeraplicacionprueba.ui.theme.AdminSuccess
import com.example.primeraplicacionprueba.ui.theme.AdminError
import com.example.primeraplicacionprueba.ui.theme.AdminWarning
import com.example.primeraplicacionprueba.ui.theme.AdminTextDark
import com.example.primeraplicacionprueba.ui.theme.AdminTextMuted
import com.example.primeraplicacionprueba.ui.theme.AdminTextLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdmin(
    placesViewModel: PlacesViewModel,
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val places by placesViewModel.places.collectAsState()
    val pendingCount = places.count { it.placeStatus == PlaceStatus.PENDING }
    val approvedCount = places.count { it.placeStatus == PlaceStatus.APPROVED }
    val rejectedCount = places.count { it.placeStatus == PlaceStatus.REJECTED }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.txt_moderation_panel_title),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.txt_profile),
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
                .background(AdminBackground)
                .padding(padding)
                .padding(horizontal = 23.dp)
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            // Statistics Cards
            item {
                StatisticsCards(pendingCount, approvedCount, rejectedCount)
            }

            // Review Queue Section
            item {
                ReviewQueueSection(placesViewModel, onItemClick = { id -> onNavigateToDetail(id) })
            }

            // Moderation History Section
            item {
                ModerationHistorySection(placesViewModel)
            }
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
            AdminStatCard(
                value = pendingCount.toString(),
                label = stringResource(R.string.txt_pending),
                modifier = Modifier.weight(1f)
            )
            AdminStatCard(
                value = approvedCount.toString(),
                label = stringResource(R.string.txt_approved_today),
                modifier = Modifier.weight(1f)
            )
        }
        
        // Second row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdminStatCard(
                value = reportsCount.toString(),
                label = stringResource(R.string.txt_reports),
                modifier = Modifier.weight(1f)
            )
            AdminStatCard(
                value = problemsCount.toString(),
                label = stringResource(R.string.txt_problems),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AdminStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AdminPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = AdminTextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ReviewQueueSection(placesViewModel: PlacesViewModel, onItemClick: (String) -> Unit = {}) {
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
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AdminTextDark
            )
            Text(
                text = stringResource(R.string.txt_pending_count, pendingPlaces.size, stringResource(R.string.txt_pending)),
                fontSize = 14.sp,
                color = AdminTextMuted
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Review items - mostrar lugares pendientes reales
        if (pendingPlaces.isNotEmpty()) {
            pendingPlaces.forEach { place ->
                ReviewItem(
                    placeName = place.title,
                    createdBy = stringResource(R.string.txt_user_prefix, place.ownerId),
                    timeAgo = stringResource(R.string.txt_time_ago_minutes),
                    onApprove = { placesViewModel.approvePlace(place.id) },
                    onReject = { placesViewModel.rejectPlace(place.id) },
                    onClick = { onItemClick(place.id) }
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
                    text = stringResource(R.string.txt_no_pending_places),
                    modifier = Modifier.padding(16.dp),
                    color = AdminTextMuted,
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
    onReject: () -> Unit,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                    color = AdminTextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = createdBy,
                    fontSize = 14.sp,
                    color = AdminTextMuted
                )
                Text(
                    text = timeAgo,
                    fontSize = 12.sp,
                    color = AdminTextLight
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = AdminSuccess),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_approve),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = AdminError),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_reject),
                        color = Color.White,
                        fontSize = 12.sp
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
    
    // Strings del resources
    val filterAll = stringResource(R.string.txt_filter_all)
    val filterApproved = stringResource(R.string.txt_filter_approved)
    val filterRejected = stringResource(R.string.txt_filter_rejected)
    
    // Estado para el filtro del historial
    var selectedFilter by remember { mutableStateOf(filterAll) }
    
    // Mostrar todos los lugares aprobados y rechazados según el filtro
    val allModeratedPlaces = when (selectedFilter) {
        filterApproved -> approvedPlaces
        filterRejected -> rejectedPlaces
        else -> (approvedPlaces + rejectedPlaces)
    }.sortedByDescending { 
        // Ordenar por fecha de creación (simulado por ID para este ejemplo)
        it.id 
    }
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.txt_moderation_history),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AdminTextDark
            )
            Text(
                text = stringResource(R.string.txt_moderated_places_count, allModeratedPlaces.size),
                fontSize = 14.sp,
                color = AdminTextMuted
            )
        }
        
        // Filtros del historial
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(filterAll, filterApproved, filterRejected).forEach { filter ->
                FilterChip(
                    onClick = { selectedFilter = filter },
                    label = { 
                        Text(
                            text = filter,
                            fontSize = 12.sp
                        ) 
                    },
                    selected = selectedFilter == filter,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AdminPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        if (allModeratedPlaces.isNotEmpty()) {
            allModeratedPlaces.forEach { place ->
                HistoryItem(
                    placeName = place.title,
                    status = when (place.placeStatus) {
                        PlaceStatus.APPROVED -> stringResource(R.string.txt_approved)
                        PlaceStatus.REJECTED -> stringResource(R.string.txt_rejected)
                        else -> stringResource(R.string.txt_pending)
                    },
                    timeAgo = stringResource(R.string.txt_time_ago_hours)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text(
                text = stringResource(R.string.txt_no_moderation_history),
                fontSize = 14.sp,
                color = AdminTextMuted,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun HistoryItem(
    placeName: String,
    status: String,
    timeAgo: String
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
                    fontWeight = FontWeight.Medium,
                    color = AdminTextDark
                )
                Text(
                    text = timeAgo,
                    fontSize = 12.sp,
                    color = AdminTextLight
                )
            }
            
            // Status badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when (status) {
                            stringResource(R.string.txt_approved) -> AdminSuccess
                            stringResource(R.string.txt_rejected) -> AdminError
                            else -> AdminWarning
                        }
                    )
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