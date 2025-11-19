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
import com.example.primeraplicacionprueba.viewmodel.ReportViewModel
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceStatus
import com.example.primeraplicacionprueba.model.ReportStatus
import com.example.primeraplicacionprueba.ui.theme.AdminBackground
import com.example.primeraplicacionprueba.ui.theme.AdminPrimary
import com.example.primeraplicacionprueba.ui.theme.AdminSuccess
import com.example.primeraplicacionprueba.ui.theme.AdminError
import com.example.primeraplicacionprueba.ui.theme.AdminWarning
import com.example.primeraplicacionprueba.ui.theme.AdminTextDark
import com.example.primeraplicacionprueba.ui.theme.AdminTextMuted
import com.example.primeraplicacionprueba.ui.theme.AdminTextLight
import com.example.primeraplicacionprueba.ui.screens.LocalMainViewModel
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdmin(
    placesViewModel: PlacesViewModel,
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToReports: () -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val reportViewModel = mainViewModel.reportViewModel
    val reports by reportViewModel.reports.collectAsState()

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
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.txt_profile),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 23.dp)
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            // Statistics Cards
            item {
                val reportsCount = reportViewModel.getPendingReportsCount()
                val problemsCount = reportViewModel.getProblematicPlacesCount()
                StatisticsCards(
                    pendingCount = pendingCount,
                    approvedCount = approvedCount,
                    rejectedCount = rejectedCount,
                    reportsCount = reportsCount,
                    problemsCount = problemsCount,
                    onReportsClick = onNavigateToReports
                )
            }

            // Reports Section - Nueva sección de reportes
            item {
                ReportsSection(
                    reportViewModel = reportViewModel,
                    onViewAllReports = onNavigateToReports
                )
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
    rejectedCount: Int,
    reportsCount: Int,
    problemsCount: Int,
    onReportsClick: () -> Unit = {}
) {

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
                modifier = Modifier.weight(1f),
                onClick = onReportsClick
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
    label: String,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = if (onClick != null) {
            modifier.clickable(onClick = onClick)
        } else {
            modifier
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.txt_pending_count, pendingPlaces.size, stringResource(R.string.txt_pending)),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Review items - mostrar lugares pendientes reales
        if (pendingPlaces.isNotEmpty()) {
            pendingPlaces.forEach { place ->
                ReviewItem(
                    place = place,
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = stringResource(R.string.txt_no_pending_places),
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ReviewItem(
    place: Place,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onClick: () -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val usersViewModel = mainViewModel.usersViewModel
    val users by usersViewModel.users.collectAsState()
    val owner = users.find { it.id == place.ownerId }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Información del lugar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = place.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Nombre del usuario
                    if (owner != null) {
                        Text(
                            text = owner.nombre,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // ID del usuario
                    Text(
                        text = stringResource(R.string.txt_user_prefix, place.ownerId.take(8) + "..."),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = stringResource(R.string.txt_time_ago_minutes),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AdminError
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_reject),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    onClick = onApprove,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AdminSuccess
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_approve),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
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
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.txt_moderated_places_count, allModeratedPlaces.size),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = timeAgo,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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

@Composable
fun ReportsSection(
    reportViewModel: ReportViewModel,
    onViewAllReports: () -> Unit
) {
    val reports by reportViewModel.reports.collectAsState()
    val pendingReports = reports.filter { it.status == ReportStatus.PENDING }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.txt_pending_reports),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            TextButton(onClick = onViewAllReports) {
                Text(
                    text = stringResource(R.string.txt_view_all),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (pendingReports.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AdminSuccess,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(R.string.txt_no_pending_reports),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            Text(
                text = stringResource(R.string.txt_reports_summary, pendingReports.size),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = onViewAllReports,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AdminWarning
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.txt_manage_reports))
            }
        }
    }
}