package com.example.primeraplicacionprueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.primeraplicacionprueba.ui.screens.Navigation
import com.example.primeraplicacionprueba.ui.theme.PrimerAplicacionPruebaTheme
import com.example.primeraplicacionprueba.viewmodel.CommentsViewModel
import com.example.primeraplicacionprueba.viewmodel.MainViewModel
import com.example.primeraplicacionprueba.viewmodel.PlacesViewModel
import com.example.primeraplicacionprueba.viewmodel.ReviewViewModel
import com.example.primeraplicacionprueba.viewmodel.UsersViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContent (
           content = {
               PrimerAplicacionPruebaTheme {
                   Navigation()
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