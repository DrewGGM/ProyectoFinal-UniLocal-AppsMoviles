package com.example.primeraplicacionprueba.ui.screens.user.tabs.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.MenuItemData
import com.example.primeraplicacionprueba.model.StatData
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.theme.BgLight
import com.example.primeraplicacionprueba.ui.theme.ErrorRed
import com.example.primeraplicacionprueba.ui.theme.Primary
import com.example.primeraplicacionprueba.ui.theme.Secondary
import com.example.primeraplicacionprueba.ui.theme.TextDark
import com.example.primeraplicacionprueba.ui.theme.TextLight
import com.example.primeraplicacionprueba.ui.theme.TextMuted
import com.example.primeraplicacionprueba.viewmodel.UsersViewModel

@Composable
fun Profile(
    user: User,
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToAchievements: () -> Unit = {},
    onNavigateToMyPlaces: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .verticalScroll(rememberScrollState())
    ) {
        // Header con gradiente
        ProfileHeader(user = user)

        Spacer(modifier = Modifier.height(5.dp))

        // Estadísticas
        StatsGrid(user = user)

        // Mi Contenido
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(stringResource(R.string.txt_my_content))
        ContentSection(
            onNavigateToEditProfile = onNavigateToEditProfile,
            onNavigateToAchievements = onNavigateToAchievements,
            onNavigateToMyPlaces = onNavigateToMyPlaces,
            onNavigateToFavorites = onNavigateToFavorites,
        )

        // Opciones de la Cuenta
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(stringResource(R.string.txt_account_options))
        AccountOptionsSection(onNavigateToLogin = onNavigateToLogin)

        // Espaciado para el bottom navigation
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ProfileHeader(user: User?) {
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
                    if (user?.imageUrl != null && user.imageUrl!!.isNotEmpty()) {
                        AsyncImage(
                            model = user.imageUrl,
                            contentDescription = stringResource(R.string.txt_avatar),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.txt_avatar),
                            modifier = Modifier.size(60.dp),
                            tint = Primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre
                Text(
                    text = user?.nombre ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight
                )

                // Rol
                Text(
                    text = user?.rol?.name ?: "",
                    fontSize = 16.sp,
                    color = TextLight.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun StatsGrid(user: User?) {
    val stats = listOf(
        StatData(user?.placesCreated.toString(), stringResource(R.string.txt_stat_places)),
        StatData(user?.placesVisited.toString(), stringResource(R.string.txt_stat_visited)),
        StatData(user?.reviewsWritten.toString(), stringResource(R.string.txt_stat_reviews)),
        StatData(user?.favoritesAdded.toString(), stringResource(R.string.txt_stat_favorites))
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
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToAchievements: () -> Unit = {},
    onNavigateToMyPlaces: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {}
) {
    val editProfileText = stringResource(R.string.txt_edit_profile)
    val achievementsText = stringResource(R.string.txt_my_achievements)
    val myPlacesText = stringResource(R.string.txt_my_places)
    val favorites = stringResource(R.string.txt_my_favorites)
    val items = listOf(
        MenuItemData(myPlacesText, Icons.Default.LocationOn),
        MenuItemData(favorites, Icons.Default.Star),
        MenuItemData(achievementsText, Icons.Default.EmojiEvents),
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
                        when (item.text) {
                            editProfileText -> onNavigateToEditProfile()
                            achievementsText -> onNavigateToAchievements()
                            favorites  -> onNavigateToFavorites()
                            myPlacesText -> onNavigateToMyPlaces()
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
        MenuItemData(logoutText, Icons.AutoMirrored.Filled.Logout)
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
                    tintColor = if (item.text == logoutText) ErrorRed else Secondary
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
