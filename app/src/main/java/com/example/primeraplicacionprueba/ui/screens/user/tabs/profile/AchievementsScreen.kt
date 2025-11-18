package com.example.primeraplicacionprueba.ui.screens.user.tabs.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.primeraplicacionprueba.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primeraplicacionprueba.model.Achievement
import com.example.primeraplicacionprueba.model.User
import com.example.primeraplicacionprueba.ui.theme.*
import com.example.primeraplicacionprueba.viewmodel.AchievementViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    user: User,
    achievementViewModel: AchievementViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val userAchievements = achievementViewModel.getUserAchievements(user)
    val unlockedAchievements = achievementViewModel.getUnlockedAchievements(user)
    val pendingAchievements = achievementViewModel.getPendingAchievements(user)
    val progressPercentage = achievementViewModel.getAchievementProgress(user)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.txt_my_achievements_title),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_back),
                            tint = TextDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BgLight)
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Progreso general
            if (userAchievements.isNotEmpty()) {
                item {
                    AchievementProgressCard(
                        unlockedCount = unlockedAchievements.size,
                        totalCount = userAchievements.size,
                        progressPercentage = progressPercentage
                    )
                }
            }

            // Logros Obtenidos
            if (unlockedAchievements.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.txt_achievements_obtained),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.height(400.dp)
                    ) {
                        items(unlockedAchievements) { achievement ->
                            AchievementCard(achievement = achievement)
                        }
                    }
                }
            }

            // Logros Pendientes
            if (pendingAchievements.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.txt_achievements_pending),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.height(400.dp)
                    ) {
                        items(pendingAchievements) { achievement ->
                            AchievementCard(achievement = achievement)
                        }
                    }
                }
            }

            // Estado vacío
            if (userAchievements.isEmpty()) {
                item {
                    EmptyAchievementsState()
                }
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (achievement.isUnlocked) 1f else 0.6f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icono con gradiente
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        if (achievement.isUnlocked) {
                            Brush.linearGradient(achievement.colorGradient)
                        } else {
                            Brush.linearGradient(
                                listOf(TextSecondary, TextSecondary)
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = achievement.icon,
                    contentDescription = achievement.title,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Título
            Text(
                text = achievement.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                textAlign = TextAlign.Center
            )

            // Descripción
            Text(
                text = achievement.description,
                fontSize = 12.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            // Barra de progreso (solo para logros no desbloqueados)
            if (!achievement.isUnlocked) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(BgLight)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(achievement.progress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Tertiary)
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementProgressCard(
    unlockedCount: Int,
    totalCount: Int,
    progressPercentage: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.txt_achievement_progress),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            
            Text(
                text = stringResource(R.string.txt_achievements_progress, unlockedCount, totalCount, stringResource(R.string.txt_achievements_unlocked)),
                fontSize = 14.sp,
                color = TextMuted
            )
            
            LinearProgressIndicator(
                progress = { progressPercentage },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Secondary,
                trackColor = BgLight
            )
            
            Text(
                text = stringResource(R.string.txt_percentage, (progressPercentage * 100).toInt()),
                fontSize = 12.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EmptyAchievementsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = stringResource(R.string.txt_no_achievements_yet),
            fontSize = 16.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = stringResource(R.string.txt_explore_to_earn_badge),
            fontSize = 14.sp,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}



