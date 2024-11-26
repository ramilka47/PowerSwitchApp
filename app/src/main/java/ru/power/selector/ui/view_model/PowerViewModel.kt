package ru.power.selector.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.power.selector.domain.use_case.ClearAlarmTimersUseCase
import ru.power.selector.domain.use_case.GetTimeToAlarmUseCase
import ru.power.selector.domain.use_case.SetAllTimeToAlarmUseCase
import ru.power.selector.domain.use_case.SetTimeToAlarmUseCase
import ru.power.selector.domain.use_case.execute
import ru.power.selector.ui.data.AlarmCalendar
import ru.power.selector.ui.data.AlarmTimer

class PowerViewModel(
    private val clearAlarmTimersUseCase: ClearAlarmTimersUseCase,
    private val getTimeToAlarmUseCase: GetTimeToAlarmUseCase,
    private val setAllTimeToAlarmUseCase: SetAllTimeToAlarmUseCase,
    private val setTimeToAlarmUseCase: SetTimeToAlarmUseCase
) : ViewModel(){

    private val dataMutable = MutableLiveData<AlarmCalendar>()
    val data : LiveData<AlarmCalendar> = dataMutable

    init {
        //loadData()
    }

    fun onCreate(){
        loadData()
    }

    private fun loadData(){
        viewModelScope.launch {
            dataMutable.postValue(getTimeToAlarmUseCase.execute(GetTimeToAlarmUseCase.Param()))
        }
    }

    fun addTimer(alarmTimer: AlarmTimer){
        viewModelScope.launch {
            setTimeToAlarmUseCase.execute(SetTimeToAlarmUseCase.Param(alarmTimer))
        }
    }

    fun deleteTimer(alarmTimer: AlarmTimer){
        viewModelScope.launch {
            setTimeToAlarmUseCase.execute(SetTimeToAlarmUseCase.Param(AlarmTimer(alarmTimer.id, alarmTimer.switch, 0L)))
        }
    }

    fun clear(){
        viewModelScope.launch {
            clearAlarmTimersUseCase.execute()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}