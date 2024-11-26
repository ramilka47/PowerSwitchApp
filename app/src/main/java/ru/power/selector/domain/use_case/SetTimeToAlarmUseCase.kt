package ru.power.selector.domain.use_case

import kotlinx.coroutines.CoroutineScope
import ru.power.selector.domain.store.IAlarmTimerSetter
import ru.power.selector.ui.data.AlarmTimer

class SetTimeToAlarmUseCase(private val iAlarmTimerSetter: IAlarmTimerSetter) : UseCase<SetTimeToAlarmUseCase.Param, Unit> {

    override suspend fun execute(request: Param, scope: CoroutineScope) {
        iAlarmTimerSetter.set(request.timer)
    }

    class Param(val timer : AlarmTimer)
}