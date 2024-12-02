package ru.power.selector.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.power.selector.route.Destination
import ru.power.selector.ui.view_model.SplashViewModel

@Composable
fun SplashScreen(viewModel : SplashViewModel, navHostController: NavHostController, modifier: Modifier = Modifier.fillMaxSize()) {
    /*Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(Color.Red)) {
        Text("Hello world")
    }*/
    loading(viewModel, navHostController, modifier)
    viewModel.onCreate()
}

@Composable
private fun loading(viewModel: SplashViewModel, navHostController: NavHostController, modifier: Modifier){
    val loading = viewModel.loading.observeAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier) {
            when(loading.value){
                true->{
                    showLoading(Modifier.size(50.dp).align(Alignment.CenterHorizontally))
                }
                false->{
                    hideLoading()
                    LaunchedEffect(Unit) {
                        navHostController.navigate(Destination.Power){
                            popUpTo(navHostController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                            //navHostController.clearBackStack(Destination.Splash)
                        }
                    }
                }
                else->{
                    hideLoading()
                }
            }
    }
}

@Composable
private fun showLoading(modifier: Modifier){
    CircularProgressIndicator(modifier = modifier, color = Color.Magenta)
}

@Composable
private fun hideLoading(){
}