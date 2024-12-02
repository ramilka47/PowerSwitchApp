package ru.power.selector.domain.use_case

import kotlinx.coroutines.CoroutineScope
import ru.power.selector.domain.WEEK_LIST
import ru.power.selector.ui.data.AlarmTimer
import ru.power.selector.ui.data.Switch

class SetAllTimerBySwitchUseCase(private val setAllTimeToAlarmUseCase: SetAllTimeToAlarmUseCase) : UseCase<SetAllTimerBySwitchUseCase.Param, Unit> {

    override suspend fun execute(request: Param, scope: CoroutineScope) {
        setAll(WEEK_LIST.map { AlarmTimer(it, request.switch, request.hour, request.min) })
    }

    private suspend fun setAll(list : List<AlarmTimer>){
        setAllTimeToAlarmUseCase.execute(SetAllTimeToAlarmUseCase.Param(list))
    }

    class Param(val switch : Switch, val hour : Int, val min : Int)
}