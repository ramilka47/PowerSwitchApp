package ru.power.selector.domain.use_case

import kotlinx.coroutines.CoroutineScope
import ru.power.selector.domain.store.IAlarmTimerSetter

class ClearAlarmTimersUseCase(private val iAlarmTimerSetter: IAlarmTimerSetter) : UseCase<Unit, Unit> {

    override suspend fun execute(request: Unit, scope: CoroutineScope) {
        iAlarmTimerSetter.clear()
    }
}