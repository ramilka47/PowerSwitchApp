package ru.power.selector.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ru.power.selector.ui.view_model.PowerViewModel

@Composable
fun PowerScreen(powerViewModel: PowerViewModel, navHostController: NavHostController, modifier: Modifier = Modifier.fillMaxSize()){
    powerViewModel.onCreate()
    observeList(powerViewModel)
}

@Composable
private fun observeList(powerViewModel: PowerViewModel){
    val data = remember { powerViewModel.data }


}