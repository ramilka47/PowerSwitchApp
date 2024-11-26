package ru.power.selector.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.power.selector.di.AppComponent
import ru.power.selector.di.Initializer
import ru.power.selector.domain.use_case.DeviceToSleepUseCase
import ru.power.selector.domain.use_case.execute
import javax.inject.Inject

class PowerOffReceiver : BroadcastReceiver() {

    private lateinit var appComponent: AppComponent
    private val scope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var deviceToSleepUseCase: DeviceToSleepUseCase

    override fun onReceive(context: Context, intent: Intent) {
        appComponent = Initializer.init(context)
        appComponent.inject(this)

        scope.launch {
            deviceToSleepUseCase.execute()
        }
    }
}