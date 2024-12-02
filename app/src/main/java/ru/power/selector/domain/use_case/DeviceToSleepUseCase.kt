package ru.power.selector.domain.use_case

import android.content.Context
import android.os.PowerManager
import kotlinx.coroutines.CoroutineScope
import ru.power.selector.domain.IDeviceWakeupAppointment
import ru.power.selector.domain.store.IActionTimeStore
import ru.power.selector.domain.store.Store

class DeviceToSleepUseCase(
    private val context: Context,
    private val iActionTimeStore: IActionTimeStore,
    private val iDeviceWakeupAppointment: IDeviceWakeupAppointment
    ) : UseCase<Unit, Unit> {

    override suspend fun execute(unit : Unit, scope: CoroutineScope){
        iActionTimeStore.getNextWakeUpTime()?.let {
            iDeviceWakeupAppointment.cancelTime()
            iDeviceWakeupAppointment.setTime(it)

            try {
                val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                try {
                    val method = pm.javaClass.getMethod(
                        "shutdown",
                        Boolean::class.java,
                        String::class.java,
                        Boolean::class.java
                    )
                    method.invoke(pm, false, null, false)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}