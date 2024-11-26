package ru.power.selector.domain.use_case

import kotlinx.coroutines.CoroutineScope
import ru.power.selector.domain.store.IAlarmTimerSetter
import ru.power.selector.ui.data.AlarmTimer

class SetAllTimeToAlarmUseCase(private val iAlarmTimerSetter: IAlarmTimerSetter) : UseCase<SetAllTimeToAlarmUseCase.Param, Unit> {

    override suspend fun execute(request: Param, scope: CoroutineScope) {
        iAlarmTimerSetter.setAll(request.timers)
    }

    class Param(val timers : List<AlarmTimer>)
}