package ru.power.selector.route

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import ru.power.selector.ui.screen.PowerScreen
import ru.power.selector.ui.screen.SplashScreen
import ru.power.selector.ui.view_model.PowerViewModel
import ru.power.selector.ui.view_model.SplashViewModel
import ru.power.selector.ui.view_model.ViewModelFactory

object Destination{
    const val Splash = "Splash"
    const val Power = "Power"
    const val Back = "Back"
}

@Composable
fun navigator(
    navHostController: NavHostController = rememberNavController(),
    screens: String = Destination.Splash,
    modifier : Modifier = Modifier.fillMaxSize(),
    viewModelFactory: ViewModelFactory) : NavHostController {
    NavHost(
        navController = navHostController,
        startDestination = screens,
        modifier = modifier
    ) {
        composable(
            route = Destination.Splash
        ) {
            SplashScreen(
                viewModelFactory.create(SplashViewModel::class.java),
                navHostController,
                modifier
            )
        }
        composable(
            route = Destination.Power
        ) {
            PowerScreen(
                viewModelFactory.create(PowerViewModel::class.java),
                navHostController,
                modifier
            )
        }
        composable(route = Destination.Back) {
            navHostController.navigateUp()
        }
    }
    return navHostController
}
/*

@Stable
class NavController(
    val navController: NavHostController,
) {
    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != navController.currentDestination?.route) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToSnackDetail(snackId: Long, origin: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.SNACK_DETAIL_ROUTE}/$snackId?origin=$origin")
        }
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}
*/
