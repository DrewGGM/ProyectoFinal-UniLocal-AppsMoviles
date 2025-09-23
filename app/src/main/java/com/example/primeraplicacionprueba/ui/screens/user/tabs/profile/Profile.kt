package com.example.primeraplicacionprueba.ui.screens.user.tabs.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.primeraplicacionprueba.model.StatData
import com.example.primeraplicacionprueba.model.MenuItemData
import com.example.primeraplicacionprueba.ui.theme.*

@Composable
fun Profile(
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .verticalScroll(rememberScrollState())
    ) {
        // Header con gradiente
        ProfileHeader()

        // Estadísticas
        StatsGrid()

        // Mi Contenido
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(stringResource(R.string.txt_my_content))
        ContentSection(onNavigateToEditProfile = onNavigateToEditProfile)

        // Opciones de la Cuenta
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(stringResource(R.string.txt_account_options))
        AccountOptionsSection(onNavigateToLogin = onNavigateToLogin)

        // Espaciado para el bottom navigation
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ProfileHeader() {
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
                        .background(Color.White),
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
                    text = stringResource(R.string.txt_user_name_sample),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight
                )

                // Rol
                Text(
                    text = stringResource(R.string.txt_role_explorer),
                    fontSize = 16.sp,
                    color = TextLight.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun StatsGrid() {
    val stats = listOf(
        StatData("45", stringResource(R.string.txt_stat_reputation)),
        StatData("7", stringResource(R.string.txt_stat_places)),
        StatData("3", stringResource(R.string.txt_stat_reviews)),
        StatData("12", stringResource(R.string.txt_stat_badges))
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                color = TextMuted,
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
        color = TextDark,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}

@Composable
fun ContentSection(
    onNavigateToEditProfile: () -> Unit = {}
) {
    val editProfileText = stringResource(R.string.txt_edit_profile)
    val items = listOf(
        MenuItemData(stringResource(R.string.txt_my_places), Icons.Default.LocationOn),
        MenuItemData(stringResource(R.string.txt_my_favorites), Icons.Default.Star),
        MenuItemData(stringResource(R.string.txt_my_achievements), Icons.Default.EmojiEvents),
        MenuItemData(editProfileText, Icons.Default.Edit)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                MenuItem(
                    icon = item.icon,
                    text = item.text,
                    onClick = {
                        if (item.text == editProfileText) {
                            onNavigateToEditProfile()
                        }
                    },
                    showDivider = index < items.size - 1
                )
            }
        }
    }
}

@Composable
fun AccountOptionsSection(
    onNavigateToLogin: () -> Unit = {}
) {
    val logoutText = stringResource(R.string.txt_logout)
    val items = listOf(
        MenuItemData(stringResource(R.string.txt_delete_account), Icons.Default.DeleteForever),
        MenuItemData(logoutText, Icons.Default.Logout)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                MenuItem(
                    icon = item.icon,
                    text = item.text,
                    onClick = {
                        if (item.text == logoutText) {
                            onNavigateToLogin()
                        }
                    },
                    showDivider = index < items.size - 1,
                    tintColor = if (item.text == logoutText) Color(0xFFC0392B) else Secondary
                )
            }
        }
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    showDivider: Boolean = true,
    tintColor: Color = Secondary
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = tintColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 15.sp,
                color = TextDark,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = BgLight,
                thickness = 1.dp
            )
        }
    }
}
