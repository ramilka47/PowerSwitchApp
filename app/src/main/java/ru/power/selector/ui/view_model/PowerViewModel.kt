package ru.power.selector.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.power.selector.domain.SWITCH
import ru.power.selector.domain.WEEK_LIST
import ru.power.selector.domain.use_case.ClearAlarmTimersUseCase
import ru.power.selector.domain.use_case.DeviceBootCompleteUseCase
import ru.power.selector.domain.use_case.GetTimeToAlarmUseCase
import ru.power.selector.domain.use_case.SetAllTimeToAlarmUseCase
import ru.power.selector.domain.use_case.SetTimeToAlarmUseCase
import ru.power.selector.domain.use_case.execute
import ru.power.selector.ui.data.AlarmTimer
import ru.power.selector.ui.data.Switch
import ru.power.selector.ui.data.WeekId

class PowerViewModel(
    private val clearAlarmTimersUseCase: ClearAlarmTimersUseCase,
    private val getTimeToAlarmUseCase: GetTimeToAlarmUseCase,
    private val setAllTimeToAlarmUseCase: SetAllTimeToAlarmUseCase,
    private val setTimeToAlarmUseCase: SetTimeToAlarmUseCase,
    private val deviceBootCompleteUseCase : DeviceBootCompleteUseCase
) : ViewModel(){

    private val dataMutable = MutableLiveData<List<AlarmTimer>>()
    val data : LiveData<List<AlarmTimer>> = dataMutable

    private val clearStateMutable = MutableLiveData<Boolean>()
    val clearState : LiveData<Boolean> = clearStateMutable

    private val alarmWeekMutable = MutableLiveData<AlarmWeek?>()
    val alarmWeek : LiveData<AlarmWeek?> = alarmWeekMutable

    private var loadDataJob : Job? = null

    fun onCreate(){
        loadData()
    }

    fun onClickTime(weekId: WeekId, switch: Switch, open: Boolean){
        if (open){
            changeTime(alarmTimer = AlarmTimer(weekId, switch, 0, 0))
        } else {
            changeTime(alarmTimer = AlarmTimer(weekId, switch))
        }
    }

    fun onChangeTime(weekId: WeekId, switch: Switch, hour : Int, min : Int){
        changeTime(AlarmTimer(weekId, switch, hour, min))
    }

    private fun changeTime(alarmTimer: AlarmTimer){
        viewModelScope
            .launch {
                setTimeToAlarmUseCase.execute(SetTimeToAlarmUseCase.Param(alarmTimer))
                loadData()
            }
    }

    private fun loadData(){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            val list = getTimeToAlarmUseCase.execute(GetTimeToAlarmUseCase.Param())
            val exists = list.timers.filter { it.hour != Int.MAX_VALUE }.isNotEmpty()
            clearStateMutable.postValue(exists)

            if (exists){
                deviceBootCompleteUseCase.execute()
            }

            dataMutable.postValue(list.timers)
        }
    }

    fun addTimer(alarmTimer: AlarmTimer){
        viewModelScope.launch {
            setTimeToAlarmUseCase.execute(SetTimeToAlarmUseCase.Param(alarmTimer))
            loadData()
        }
    }

    fun deleteTimer(alarmTimer: AlarmTimer){
        viewModelScope.launch {
            setTimeToAlarmUseCase.execute(SetTimeToAlarmUseCase.Param(AlarmTimer(alarmTimer.id, alarmTimer.switch)))
            loadData()
        }
    }

    fun setSwitchOnAll(state : Boolean){
        if (state){
            alarmWeekMutable.postValue(
                AlarmWeek(0, 0, 0, 0)
            )
            val result = mutableListOf<AlarmTimer>()
            SWITCH.forEach { switch ->
            WEEK_LIST.forEach { weekId ->
                    result.add(AlarmTimer(weekId, switch, 0, 0))
                }
            }
            setAll(result)
        } else {
            val result = mutableListOf<AlarmTimer>()
            SWITCH.forEach { switch ->
            WEEK_LIST.forEach { weekId ->
                    result.add(AlarmTimer(weekId, switch))
                }
            }
            setAll(result)
            alarmWeekMutable.postValue(null)
        }
    }

    fun setTimeForAll(switch: Switch, hour : Int, min : Int){
        val week = alarmWeek.value
        alarmWeekMutable.postValue(
            AlarmWeek(
                wakeUpHour = if (switch == Switch.On) hour else week?.wakeUpHour?:0,
                wakeUpMin = if (switch == Switch.On) min else week?.wakeUpMin?:0,
                sleepHour = if (switch == Switch.Off) hour else week?.sleepHour?:0,
                sleepMin = if (switch == Switch.Off) min else week?.sleepMin?:0,
            )
        )
        setAll(WEEK_LIST.map { AlarmTimer(it, switch, hour, min) })
    }

    private fun setAll(list : List<AlarmTimer>){
        viewModelScope.launch {
            setAllTimeToAlarmUseCase.execute(SetAllTimeToAlarmUseCase.Param(list))
            loadData()
        }
    }

    fun clear(){
        viewModelScope.launch {
            clearAlarmTimersUseCase.execute()
            loadData()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}

class AlarmWeek(
    val wakeUpHour : Int,
    val wakeUpMin : Int,
    val sleepHour : Int,
    val sleepMin : Int)