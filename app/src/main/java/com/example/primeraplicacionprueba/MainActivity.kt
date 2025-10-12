package com.example.primeraplicacionprueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.primeraplicacionprueba.ui.screens.Navigation
import com.example.primeraplicacionprueba.ui.theme.PrimerAplicacionPruebaTheme
import com.example.primeraplicacionprueba.utils.SharedPrefsUtil
import com.example.primeraplicacionprueba.viewmodel.*
import kotlin.getValue


class MainActivity : ComponentActivity() {

    private val usersViewModel: UsersViewModel by viewModels()
    private val reviewsViewModel: ReviewViewModel by viewModels()
    private val placesViewModel: PlacesViewModel by viewModels()
    private val achievementViewModel: AchievementViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        placesViewModel.setReviewViewModel(reviewsViewModel)
        val mainViewModel = MainViewModel(
            placesViewModel = placesViewModel,
            reviewsViewModel = reviewsViewModel,
            usersViewModel = usersViewModel,
            achievementViewModel = achievementViewModel
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



















// Text(
//                           modifier = Modifier.background(Color.Yellow),
//                           text = "Hola Todos",
//                           color = Color.Blue,
//                           fontSize = 35.sp
//                       )
//                       Spacer(
//                           modifier = Modifier.height(20.dp)
//                       )
//                       Text(
//                           text = "Hola Todos",
//                           fontSize = 35.sp
//                       )
//                       Text(
//                           text = "Hola Todos",
//                           fontSize = 35.sp
//                       )