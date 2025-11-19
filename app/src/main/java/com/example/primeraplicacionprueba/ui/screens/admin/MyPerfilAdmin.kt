package com.example.primeraplicacionprueba.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.PlaceStatus
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.theme.BgLight
import com.example.primeraplicacionprueba.ui.theme.Primary
import com.example.primeraplicacionprueba.ui.theme.Secondary
import com.example.primeraplicacionprueba.ui.theme.TextLight
import com.example.primeraplicacionprueba.ui.theme.TextMuted
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.ui.screens.LocalMainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPerfilAdmin(
    user: User,
    placesViewModel: PlacesViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit = {}
) {
    val mainViewModel = LocalMainViewModel.current
    val reportViewModel = mainViewModel.reportViewModel
    val reports by reportViewModel.reports.collectAsState()

    val places by placesViewModel.places.collectAsState()

    // Estadísticas reales del administrador
    val approvedCount = places.count { it.placeStatus == PlaceStatus.APPROVED }
    val rejectedCount = places.count { it.placeStatus == PlaceStatus.REJECTED }
    val pendingCount = places.count { it.placeStatus == PlaceStatus.PENDING }
    val reportsHandled = reportViewModel.getReportsHandledByAdmin(user.id)
    val reputationPoints = (approvedCount * 2) + (rejectedCount * 1) + (reportsHandled * 5) // Cálculo basado en actividad

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.txt_my_profile),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_go_back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header con gradiente (mismo estilo que Profile)
            AdminProfileHeader(user = user)

            Spacer(modifier = Modifier.height(5.dp))

            // Estadísticas (mismo estilo que Profile)
            AdminStatsGrid(
                approvedCount = approvedCount,
                rejectedCount = rejectedCount,
                pendingCount = pendingCount,
                reportsHandled = reportsHandled
            )

            // Opciones de la Cuenta
            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(stringResource(R.string.txt_account_options))
            AccountOptionsSection(onNavigateToLogin = onNavigateToLogin)

            // Espaciado para el bottom navigation
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun AdminProfileHeader(user: User) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        // Fondo con gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Primary, Secondary)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.txt_avatar),
                        modifier = Modifier.size(60.dp),
                        tint = Primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre
                Text(
                    text = user.nombre,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Rol
                val roleText = when (user.rol) {
                    com.example.primeraplicacionprueba.model.Role.ADMIN -> stringResource(R.string.txt_role_admin)
                    com.example.primeraplicacionprueba.model.Role.USER -> stringResource(R.string.txt_role_user)
                }
                Text(
                    text = roleText,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun AdminStatsGrid(
    approvedCount: Int,
    rejectedCount: Int,
    pendingCount: Int,
    reportsHandled: Int
) {
    val stats = listOf(
        StatData(approvedCount.toString(), stringResource(R.string.txt_approved_places)),
        StatData(rejectedCount.toString(), stringResource(R.string.txt_rejected_places)),
        StatData(pendingCount.toString(), stringResource(R.string.txt_pending_places)),
        StatData(reportsHandled.toString(), stringResource(R.string.txt_reports_handled))
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-50).dp)
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stats.take(2).forEach { stat ->
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = stat.value,
                    label = stat.label
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stats.takeLast(2).forEach { stat ->
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = stat.value,
                    label = stat.label
                )
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
fun AccountOptionsSection(
    onNavigateToLogin: () -> Unit = {}
) {
    val logoutText = stringResource(R.string.txt_logout)
    val items = listOf(
        MenuItemData(logoutText, Icons.AutoMirrored.Filled.Logout)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                MenuItem(
                    icon = item.icon,
                    text = item.text,
                    onClick = {
                        when (item.text) {
                            logoutText -> onNavigateToLogin()
                        }
                    }
                )
                if (index < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (text == stringResource(R.string.txt_logout)) MaterialTheme.colorScheme.error else Primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (text == stringResource(R.string.txt_logout)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
    }
}

data class StatData(
    val value: String,
    val label: String
)

data class MenuItemData(
    val text: String,
    val icon: ImageVector
)
