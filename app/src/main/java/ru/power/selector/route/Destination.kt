package ru.power.selector.route

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.power.selector.ui.screen.PowerScreen
import ru.power.selector.ui.screen.SplashScreen
import ru.power.selector.ui.view_model.PowerViewModel
import ru.power.selector.ui.view_model.SplashViewModel
import ru.power.selector.ui.view_model.ViewModelFactory

sealed class Destination {
    data object Splash : Destination()
    data object Power : Destination()
    data object Back : Destination()
}

@Composable
fun navigator(
    navHostController: NavHostController = rememberNavController(),
    screens: Destination = Destination.Splash,
    modifier : Modifier = Modifier.fillMaxSize(),
    viewModelFactory: ViewModelFactory) : NavHostController {
    NavHost(
        navController = navHostController,
        startDestination = screens,
        modifier = modifier
    ) {
        composable<Destination.Splash> { SplashScreen(viewModelFactory.create(SplashViewModel::class.java), navHostController, modifier) }
        composable<Destination.Power> { PowerScreen(viewModelFactory.create(PowerViewModel::class.java), navHostController, modifier) }
        composable<Destination.Back> {  }
    }
    return navHostController
}