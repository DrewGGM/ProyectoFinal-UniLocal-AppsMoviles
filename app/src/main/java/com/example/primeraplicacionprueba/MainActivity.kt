package com.example.primeraplicacionprueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.primeraplicacionprueba.ui.screens.Navigation
import com.example.primeraplicacionprueba.ui.theme.PrimerAplicacionPruebaTheme
import com.example.primeraplicacionprueba.utils.CloudinaryHelper
import com.example.primeraplicacionprueba.utils.SharedPrefsUtil
import com.example.primeraplicacionprueba.viewmodel.*
import kotlin.getValue


class MainActivity : ComponentActivity() {

    private val usersViewModel: UsersViewModel by viewModels()
    private val reviewsViewModel: ReviewViewModel by viewModels()
    private val placesViewModel: PlacesViewModel by viewModels()
    private val achievementViewModel: AchievementViewModel by viewModels()
    private val commentsViewModel: CommentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        CloudinaryHelper.init(
            context = this,
            cloudName = getString(R.string.cloudinary_cloud_name),
            apiKey = getString(R.string.cloudinary_api_key),
            apiSecret = getString(R.string.cloudinary_api_secret)
        )

        usersViewModel.restoreUserFromPreferences(this)
        placesViewModel.setReviewViewModel(reviewsViewModel)

        val mainViewModel = MainViewModel(
            placesViewModel = placesViewModel,
            reviewsViewModel = reviewsViewModel,
            usersViewModel = usersViewModel,
            achievementViewModel = achievementViewModel,
            commentsViewModel = commentsViewModel
        )

        setContent (
           content = {
               PrimerAplicacionPruebaTheme {
                   Navigation(
                       mainViewModel = mainViewModel
                   )
               }

           }
        )
    }
}
