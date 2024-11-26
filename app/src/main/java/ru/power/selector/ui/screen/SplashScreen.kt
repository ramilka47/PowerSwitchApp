package ru.power.selector.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ru.power.selector.route.Destination
import ru.power.selector.ui.view_model.SplashViewModel

@Composable
fun SplashScreen(viewModel : SplashViewModel, navHostController: NavHostController, modifier: Modifier = Modifier.fillMaxSize()){
    viewModel.onCreate()
    loading(viewModel, navHostController)
}

@Composable
private fun loading(viewModel: SplashViewModel, navHostController: NavHostController){
    val loading = remember { viewModel.loading }
    when(loading.value){
        true->{ showLoading() }
        false->{
            hideLoading()
            navHostController.clearBackStack(Destination.Splash)
            navHostController.navigate(Destination.Power)
        }
        else->{ hideLoading() }
    }
}

@Composable
private fun showLoading(){

}

@Composable
private fun hideLoading(){

}