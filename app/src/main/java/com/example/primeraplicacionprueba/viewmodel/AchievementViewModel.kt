package com.example.primeraplicacionprueba.viewmodel

import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.primeraplicacionprueba.model.Achievement
import com.example.primeraplicacionprueba.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AchievementViewModel : ViewModel() {

    private val _achievements = MutableStateFlow(emptyList<Achievement>())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    init {
        loadAllAchievements()
    }

    fun getUserAchievements(user: User): List<Achievement> {
        return _achievements.value.map { achievement ->
            when (achievement.id) {
                "first_place" -> achievement.copy(
                    isUnlocked = user.getTotalPlacesCreated() >= 1,
                    progress = (user.getTotalPlacesCreated() / 1f).coerceAtMost(1f)
                )
                "explorer" -> achievement.copy(
                    isUnlocked = user.getTotalPlacesVisited() >= 5,
                    progress = (user.getTotalPlacesVisited() / 5f).coerceAtMost(1f)
                )
                "local_guide" -> achievement.copy(
                    isUnlocked = user.getTotalPlacesCreated() >= 10,
                    progress = (user.getTotalPlacesCreated() / 10f).coerceAtMost(1f)
                )
                "reviewer" -> achievement.copy(
                    isUnlocked = user.getTotalReviewsWritten() >= 5,
                    progress = (user.getTotalReviewsWritten() / 5f).coerceAtMost(1f)
                )
                "golden_commentator" -> achievement.copy(
                    isUnlocked = user.getTotalReviewsWritten() >= 50,
                    progress = (user.getTotalReviewsWritten() / 50f).coerceAtMost(1f)
                )
                "collector" -> achievement.copy(
                    isUnlocked = user.getTotalFavoritesAdded() >= 10,
                    progress = (user.getTotalFavoritesAdded() / 10f).coerceAtMost(1f)
                )
                "veteran" -> achievement.copy(
                    isUnlocked = user.getDaysSinceJoin() >= 30,
                    progress = (user.getDaysSinceJoin() / 30f).coerceAtMost(1f)
                )
                "super_user" -> achievement.copy(
                    isUnlocked = user.getTotalPlacesCreated() >= 5 && user.getTotalReviewsWritten() >= 10,
                    progress = ((user.getTotalPlacesCreated() + user.getTotalReviewsWritten()) / 15f).coerceAtMost(1f)
                )
                else -> achievement
            }
        }
    }

    fun getUnlockedAchievements(user: User): List<Achievement> {
        return getUserAchievements(user).filter { it.isUnlocked }
    }

    fun getPendingAchievements(user: User): List<Achievement> {
        return getUserAchievements(user).filter { !it.isUnlocked }
    }

    fun getAchievementProgress(user: User): Float {
        val userAchievements = getUserAchievements(user)
        val unlockedCount = userAchievements.count { it.isUnlocked }
        return if (userAchievements.isNotEmpty()) {
            unlockedCount.toFloat() / userAchievements.size
        } else {
            0f
        }
    }

    private fun loadAllAchievements() {
        _achievements.value = getBaseAchievements()
    }
    
    private fun getBaseAchievements(): List<Achievement> {
        return listOf(
            Achievement(
                id = "first_place",
                title = "Primer Lugar",
                description = "Crea tu primer lugar en la plataforma",
                icon = Icons.Default.EmojiEvents,
                isUnlocked = false,
                progress = 0f,
                colorGradient = listOf(
                    Color(0xFFFF6B6B),
                    Color(0xFFFFE066)
                )
            ),
            Achievement(
                id = "explorer",
                title = "Explorador",
                description = "Visita 5 lugares diferentes",
                icon = Icons.Default.LocationOn,
                isUnlocked = false,
                progress = 0f,
                colorGradient = listOf(
                    Color(0xFF4ECDC4),
                    Color(0xFFA8E6CF)
                )
            ),
            Achievement(
                id = "local_guide",
                title = "Guía Local",
                description = "Crea 10 lugares en la plataforma",
                icon = Icons.Default.Rocket,
                isUnlocked = false,
                progress = 0f,
                colorGradient = listOf(
                    Color(0xFFFFE066),
                    Color(0xFFFFB347)
                )
            ),
            Achievement(
                id = "reviewer",
                title = "Reseñador",
                description = "Escribe 5 reseñas",
                icon = Icons.AutoMirrored.Filled.Comment,
                isUnlocked = false,
                progress = 0f,
                colorGradient = listOf(
                    Color(0xFFA8E6CF),
                    Color(0xFF4ECDC4)
                )
            ),
            Achievement(
                id = "golden_commentator",
                title = "Comentarista Dorado",
                description = "Escribe 50 reseñas",
                icon = Icons.Default.Star,
                isUnlocked = false,
                progress = 0f,
                colorGradient = listOf(
                    Color(0xFFFFB347),
                    Color(0xFFFFE066)
                )
            ),
            Achievement(
                id = "collector",
                title = "Coleccionista",
                description = "Agrega 10 lugares a favoritos",
                icon = Icons.Default.Favorite,
                isUnlocked = false,
                progress = 0f,
                colorGradient = listOf(
                    Color(0xFFFF6B6B),
                    Color(0xFFA8E6CF)
                )
            ),
            Achievement(
                id = "veteran",
                title = "Veterano",
                description = "Lleva 30 días en la plataforma",
                icon = Icons.Default.Schedule,
                isUnlocked = false,
                progress = 0f,
                colorGradient = listOf(
                    Color(0xFF4ECDC4),
                    Color(0xFFFF6B6B)
                )
            ),
            Achievement(
                id = "super_user",
                title = "Super Usuario",
                description = "Crea 5 lugares y escribe 10 reseñas",
                icon = Icons.Default.Verified,
                isUnlocked = false,
                progress = 0f,
                colorGradient = listOf(
                    Color(0xFFFFE066),
                    Color(0xFFFF6B6B)
                )
            )
        )
    }
}
