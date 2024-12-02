package ru.power.selector.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.power.selector.di.AppComponent
import ru.power.selector.di.Initializer
import ru.power.selector.domain.SWITCH
import ru.power.selector.domain.use_case.DeviceBootCompleteUseCase
import ru.power.selector.domain.use_case.SetAllTimeToAlarmUseCase
import ru.power.selector.domain.use_case.SetAllTimerBySwitchUseCase
import ru.power.selector.domain.use_case.execute
import ru.power.selector.ui.data.AlarmTimer
import ru.power.selector.ui.data.Switch
import ru.power.selector.ui.data.WeekId
import javax.inject.Inject

class SetTimerReceiver : BroadcastReceiver() {

    private lateinit var appComponent: AppComponent
    private val scope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var setAllTimerBySwitchUseCase: SetAllTimerBySwitchUseCase
    @Inject
    lateinit var deviceBootCompleteUseCase : DeviceBootCompleteUseCase
    @Inject
    lateinit var setAllTimerUseCase: SetAllTimeToAlarmUseCase

    override fun onReceive(p0: Context, p1: Intent?) {
        Log.d(this::class.java.name, "onReceive")

        p1?.let {
            appComponent = Initializer.init(p0)
            appComponent.inject(this)

            when(it.action){
                "intent.custom.action.SET_ALARM_TIME"->{
                    val onSwitch = it.getStringExtra("on")
                    if (onSwitch == "on") {
                        val wakeUpHour = it.getIntExtra("wakeUpHour", Int.MAX_VALUE)
                        val wakeUpMin = it.getIntExtra("wakeUpMin", Int.MAX_VALUE)

                        Log.d(this::class.java.name, "wakeUp $wakeUpHour && $wakeUpMin")

                        scope.launch {
                            setAllTimerBySwitchUseCase.execute(
                                SetAllTimerBySwitchUseCase.Param(
                                    Switch.On,
                                    wakeUpHour,
                                    wakeUpMin
                                )
                            )
                            deviceBootCompleteUseCase.execute()
                        }
                    }

                    val offSwitch = it.getStringExtra("off")
                    if (offSwitch == "off") {
                        val sleepUpHour = it.getIntExtra("sleep" +
                                "Hour", Int.MAX_VALUE)
                        val sleepUpMin = it.getIntExtra("sleepMin", Int.MAX_VALUE)

                        Log.d(this::class.java.name, "sleep $sleepUpHour && $sleepUpMin")

                        scope.launch {
                            setAllTimerBySwitchUseCase.execute(
                                SetAllTimerBySwitchUseCase.Param(
                                    Switch.Off,
                                    sleepUpHour,
                                    sleepUpMin
                                )
                            )
                        }
                    } else { }
                }
                "intent.custom.action.DELETE_WEEK_DAYS"->{
                    val weekList = it.getStringArrayExtra("weekList")
                    Log.d(this::class.java.name, weekList.contentToString())

                    val weekIdList = weekList?.map {
                        WEEK_STRING_LIST.keys.find { key -> it == key }?.let { WEEK_STRING_LIST[it] }
                    }?.filter { it != null }

                    val result = mutableListOf<AlarmTimer>().apply {
                        weekIdList?.forEach {
                            it?.let { weekId ->
                                SWITCH.forEach { switch ->
                                    add(AlarmTimer(weekId, switch))
                                }
                            }
                        }
                    }

                    Log.d(this::class.java.name, result.toString())

                    scope.launch {
                        setAllTimerUseCase.execute(SetAllTimeToAlarmUseCase.Param(result))
                        deviceBootCompleteUseCase.execute()
                    }
                }
                else->{}
            }
        }
    }

    companion object {
        private val WEEK_STRING_LIST = mapOf(
            "Monthly" to WeekId.Monthly,
            "Tuesday" to WeekId.Tuesday,
            "Wednesday" to WeekId.Wednesday,
            "Thursday" to WeekId.Thursday,
            "Friday" to WeekId.Friday,
            "Saturday" to WeekId.Saturday,
            "Sunday" to WeekId.Sunday
        )
    }
}