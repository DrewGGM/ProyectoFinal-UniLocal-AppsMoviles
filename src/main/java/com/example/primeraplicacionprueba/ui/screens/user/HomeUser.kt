package com.example.primeraplicacionprueba.ui.screens.user
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.ui.screens.user.bottonbas.BottomBarUser
import com.example.primeraplicacionprueba.ui.screens.user.nav.ContentUser


@Composable
fun HomeUser() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarUser()
        },
        bottomBar = {
            BottomBarUser(
                navController = navController
            )
        }
    ) { padding ->
        ContentUser(
            navController=navController,
            padding = padding
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUser(){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.txt_titule_home),

                )
        }
    )
}
