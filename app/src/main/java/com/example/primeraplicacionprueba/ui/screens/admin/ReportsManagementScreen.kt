package com.example.primeraplicacionprueba.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Report
import com.example.primeraplicacionprueba.model.ReportReason
import com.example.primeraplicacionprueba.model.ReportStatus
import com.example.primeraplicacionprueba.ui.screens.LocalMainViewModel
import com.example.primeraplicacionprueba.ui.theme.AdminSuccess
import com.example.primeraplicacionprueba.ui.theme.AdminError
import com.example.primeraplicacionprueba.ui.theme.AdminWarning
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsManagementScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPlaceDetail: (String) -> Unit
) {
    val mainViewModel = LocalMainViewModel.current
    val reportViewModel = mainViewModel.reportViewModel
    val placesViewModel = mainViewModel.placesViewModel
    val usersViewModel = mainViewModel.usersViewModel
    val currentUser by usersViewModel.currentUser.collectAsState()

    val reports by reportViewModel.reports.collectAsState()
    val places by placesViewModel.places.collectAsState()
    val users by usersViewModel.users.collectAsState()

    // Filtros
    var selectedFilter by remember { mutableStateOf(ReportStatus.PENDING) }

    val filteredReports = reports.filter { it.status == selectedFilter }
        .sortedByDescending { it.createdDate }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.txt_reports_management),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            // Filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    onClick = { selectedFilter = ReportStatus.PENDING },
                    label = {
                        Text(
                            text = stringResource(R.string.txt_pending),
                            fontSize = 14.sp
                        )
                    },
                    selected = selectedFilter == ReportStatus.PENDING,
                    leadingIcon = {
                        Badge {
                            Text(
                                reports.count { it.status == ReportStatus.PENDING }.toString()
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AdminWarning,
                        selectedLabelColor = Color.White
                    )
                )

                FilterChip(
                    onClick = { selectedFilter = ReportStatus.RESOLVED },
                    label = {
                        Text(
                            text = stringResource(R.string.txt_resolved),
                            fontSize = 14.sp
                        )
                    },
                    selected = selectedFilter == ReportStatus.RESOLVED,
                    leadingIcon = {
                        Badge {
                            Text(
                                reports.count { it.status == ReportStatus.RESOLVED }.toString()
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AdminSuccess,
                        selectedLabelColor = Color.White
                    )
                )

                FilterChip(
                    onClick = { selectedFilter = ReportStatus.DISMISSED },
                    label = {
                        Text(
                            text = stringResource(R.string.txt_dismissed),
                            fontSize = 14.sp
                        )
                    },
                    selected = selectedFilter == ReportStatus.DISMISSED,
                    leadingIcon = {
                        Badge {
                            Text(
                                reports.count { it.status == ReportStatus.DISMISSED }.toString()
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AdminError,
                        selectedLabelColor = Color.White
                    )
                )
            }

            Divider()

            // Lista de reportes
            if (filteredReports.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Text(
                            text = when (selectedFilter) {
                                ReportStatus.PENDING -> stringResource(R.string.txt_no_pending_reports)
                                ReportStatus.RESOLVED -> stringResource(R.string.txt_no_resolved_reports)
                                ReportStatus.DISMISSED -> stringResource(R.string.txt_no_dismissed_reports)
                            },
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredReports) { report ->
                        val place = places.find { it.id == report.placeId }
                        val reporter = users.find { it.id == report.reporterId }

                        ReportCard(
                            report = report,
                            placeName = place?.title ?: stringResource(R.string.txt_place_not_found),
                            reporterName = reporter?.nombre ?: stringResource(R.string.txt_unknown_user),
                            onResolveClick = {
                                currentUser?.let { user ->
                                    reportViewModel.resolveReport(report.id, user.id)
                                }
                            },
                            onDismissClick = {
                                currentUser?.let { user ->
                                    reportViewModel.dismissReport(report.id, user.id)
                                }
                            },
                            onViewPlaceClick = {
                                place?.let { onNavigateToPlaceDetail(it.id) }
                            },
                            onDeletePlaceClick = {
                                place?.let { placesViewModel.deletePlace(it.id) }
                            },
                            isPending = selectedFilter == ReportStatus.PENDING
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReportCard(
    report: Report,
    placeName: String,
    reporterName: String,
    onResolveClick: () -> Unit,
    onDismissClick: () -> Unit,
    onViewPlaceClick: () -> Unit,
    onDeletePlaceClick: () -> Unit,
    isPending: Boolean
) {
    var showActionsDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showActionsDialog) {
        AlertDialog(
            onDismissRequest = { showActionsDialog = false },
            title = { Text(stringResource(R.string.txt_confirm_action)) },
            text = { Text(stringResource(R.string.txt_report_action_confirm)) },
            dismissButton = {
                TextButton(onClick = {
                    showActionsDialog = false
                    onDismissClick()
                }) {
                    Text(stringResource(R.string.txt_dismiss_report))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showActionsDialog = false
                    onResolveClick()
                }) {
                    Text(stringResource(R.string.txt_resolve_report))
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.txt_delete_place)) },
            text = { Text(stringResource(R.string.txt_delete_place_confirm)) },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.txt_cancel))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeletePlaceClick()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.txt_delete))
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = placeName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(R.string.txt_reported_by, reporterName),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Badge de razón
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = getReasonLabel(report.reason),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Descripción
            Text(
                text = report.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Fecha
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDate(report.createdDate.toDate()),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Acciones
            if (isPending) {
                Divider()
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onViewPlaceClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.txt_view_place))
                        }

                        Button(
                            onClick = { showActionsDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.txt_handle))
                        }
                    }

                    Button(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.txt_delete_place))
                    }
                }
            }
        }
    }
}

@Composable
private fun getReasonLabel(reason: ReportReason): String {
    return when (reason) {
        ReportReason.INAPPROPRIATE_CONTENT -> stringResource(R.string.txt_inappropriate_content)
        ReportReason.WRONG_INFORMATION -> stringResource(R.string.txt_wrong_information)
        ReportReason.DUPLICATE -> stringResource(R.string.txt_duplicate)
        ReportReason.SPAM -> stringResource(R.string.txt_spam)
        ReportReason.CLOSED_PERMANENTLY -> stringResource(R.string.txt_closed_permanently)
        ReportReason.OTHER -> stringResource(R.string.txt_other)
    }
}

private fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(date)
}
