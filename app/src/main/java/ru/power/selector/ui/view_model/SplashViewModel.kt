package ru.power.selector.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val loadingMutable = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = loadingMutable

    fun onCreate(){
        loading()
    }

    private fun loading(){
        viewModelScope.launch {
            loadingMutable.postValue(true)
            delay(3000)
            loadingMutable.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}