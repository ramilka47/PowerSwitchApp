package ru.power.selector.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.power.selector.di.AppComponent
import ru.power.selector.di.Initializer
import ru.power.selector.domain.use_case.DeviceBootCompleteUseCase
import ru.power.selector.domain.use_case.execute
import javax.inject.Inject

class PowerOnReceiver : BroadcastReceiver() {

    private lateinit var appComponent: AppComponent
    private val scope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var deviceBootCompleteUseCase: DeviceBootCompleteUseCase

    override fun onReceive(context: Context, intent: Intent) {
        appComponent = Initializer.init(context)
        appComponent.inject(this)

        scope.launch {
            Environment.getExternalStorageDirectory()
            deviceBootCompleteUseCase.execute()
        }
    }
}