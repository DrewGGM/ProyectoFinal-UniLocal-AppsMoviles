package com.example.primeraplicacionprueba.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Report
import com.example.primeraplicacionprueba.model.ReportReason
import com.example.primeraplicacionprueba.utils.RequestResult
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPlaceScreen(
    placeId: String,
    placeName: String,
    onNavigateBack: () -> Unit
) {
    val mainViewModel = LocalMainViewModel.current
    val reportViewModel = mainViewModel.reportViewModel
    val usersViewModel = mainViewModel.usersViewModel
    val currentUser by usersViewModel.currentUser.collectAsState()
    val reportResult by reportViewModel.reportResult.collectAsState()

    var selectedReason by remember { mutableStateOf<ReportReason?>(null) }
    var description by remember { mutableStateOf("") }
    var showReasonError by remember { mutableStateOf(false) }
    var showDescriptionError by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Limpiar el resultado cuando se abre la pantalla
    DisposableEffect(Unit) {
        reportViewModel.clearReportResult()
        onDispose {
            reportViewModel.clearReportResult()
        }
    }

    // Observar resultado del reporte
    LaunchedEffect(reportResult) {
        when (reportResult) {
            is RequestResult.Success -> {
                showSuccessDialog = true
            }
            is RequestResult.Failure -> {
                // Manejar error si es necesario
            }
            else -> {}
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onNavigateBack()
            },
            title = {
                Text(
                    stringResource(R.string.txt_report_sent),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(stringResource(R.string.txt_report_sent_description))
            },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    onNavigateBack()
                }) {
                    Text(stringResource(R.string.txt_ok))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.txt_report_place),
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Información del lugar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_reporting_place),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = placeName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Razón del reporte
            Text(
                text = stringResource(R.string.txt_report_reason),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            val reportReasons = listOf(
                ReportReason.INAPPROPRIATE_CONTENT to Pair(Icons.Default.Warning, stringResource(R.string.txt_inappropriate_content)),
                ReportReason.WRONG_INFORMATION to Pair(Icons.Default.Info, stringResource(R.string.txt_wrong_information)),
                ReportReason.DUPLICATE to Pair(Icons.Default.CopyAll, stringResource(R.string.txt_duplicate)),
                ReportReason.SPAM to Pair(Icons.Default.Block, stringResource(R.string.txt_spam)),
                ReportReason.CLOSED_PERMANENTLY to Pair(Icons.Default.Cancel, stringResource(R.string.txt_closed_permanently)),
                ReportReason.OTHER to Pair(Icons.Default.MoreHoriz, stringResource(R.string.txt_other))
            )

            reportReasons.forEach { (reason, iconAndLabel) ->
                val (icon, label) = iconAndLabel
                ReportReasonItem(
                    icon = icon,
                    label = label,
                    isSelected = selectedReason == reason,
                    onClick = {
                        selectedReason = reason
                        showReasonError = false
                    }
                )
            }

            if (showReasonError) {
                Text(
                    text = stringResource(R.string.txt_select_reason_error),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            // Descripción
            Text(
                text = stringResource(R.string.txt_description_optional),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    if (it.isNotEmpty()) showDescriptionError = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = {
                    Text(stringResource(R.string.txt_report_description_hint))
                },
                isError = showDescriptionError,
                supportingText = if (showDescriptionError) {
                    { Text(stringResource(R.string.txt_description_required_error)) }
                } else null,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón enviar
            Button(
                onClick = {
                    var hasError = false

                    if (selectedReason == null) {
                        showReasonError = true
                        hasError = true
                    }

                    if (description.isBlank()) {
                        showDescriptionError = true
                        hasError = true
                    }

                    if (!hasError && selectedReason != null) {
                        val user = currentUser
                        if (user != null) {
                            val report = Report(
                                placeId = placeId,
                                reporterId = user.id,
                                reason = selectedReason!!,
                                description = description,
                                createdDate = Timestamp.now()
                            )
                            reportViewModel.create(report)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                if (reportResult is RequestResult.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = stringResource(R.string.txt_send_report),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ReportReasonItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected)
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                fontSize = 16.sp,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
