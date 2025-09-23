package com.example.primeraplicacionprueba.ui.screens.user.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.primeraplicacionprueba.ui.screens.user.nav.RouteTab
import com.example.primeraplicacionprueba.R

@Composable
fun BottomBarUser(
    navController: NavHostController

) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar() {
        Destination.entries.forEachIndexed { index, destination ->
            val isSelected = currentDestination?.route ==  destination.route::class.qualifiedName
            NavigationBarItem(
                label = {
                    Text(
                        text = stringResource(destination.label)
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(destination.route)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                },

                )
        }


    }
}


enum class Destination(
    val route: RouteTab,
    val label:Int,
    val icon: ImageVector
){
    HOME(RouteTab.Home,R.string.txt_menuhome,Icons.Default.Home),
    MAP(RouteTab.Map,R.string.txt_menumap,Icons.Default.Map),
    PROFILE(RouteTab.Profile,R.string.txt_menuprofile,Icons.Default.AccountCircle),
}