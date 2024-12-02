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
import ru.power.selector.domain.use_case.DeviceBootCompleteUseCase
import ru.power.selector.domain.use_case.SetAllTimerBySwitchUseCase
import ru.power.selector.domain.use_case.execute
import ru.power.selector.ui.data.Switch
import javax.inject.Inject

class SetTimerReceiver : BroadcastReceiver() {

    private lateinit var appComponent: AppComponent
    private val scope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var setAllTimerBySwitchUseCase: SetAllTimerBySwitchUseCase
    @Inject
    lateinit var deviceBootCompleteUseCase : DeviceBootCompleteUseCase

    override fun onReceive(p0: Context, p1: Intent?) {
        Log.d(this::class.java.name, "onReceive")

        p1?.let {
            appComponent = Initializer.init(p0)
            appComponent.inject(this)

            if ("intent.custom.action.SET_ALARM_TIME" == it.action) {
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
                }
            }
        }
    }
}