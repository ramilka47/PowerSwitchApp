package ru.power.selector.domain.use_case

import kotlinx.coroutines.CoroutineScope
import ru.power.selector.domain.IDeviceSleepAppointment
import ru.power.selector.domain.IDeviceWakeupAppointment
import ru.power.selector.domain.store.IActionTimeStore

class DeviceBootCompleteUseCase(
    private val iStore: IActionTimeStore,
    private val iDeviceSleepAppointment: IDeviceSleepAppointment
) : UseCase<Unit, Unit> {

    override suspend fun execute(unit : Unit, scope: CoroutineScope) {
        iStore.getNextSleepTime()?.let {
            iDeviceSleepAppointment.setAlarm(it)
        }
    }
}