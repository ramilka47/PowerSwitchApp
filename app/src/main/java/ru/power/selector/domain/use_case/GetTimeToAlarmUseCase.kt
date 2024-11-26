package ru.power.selector.domain.use_case

import kotlinx.coroutines.CoroutineScope
import ru.power.selector.domain.store.IAlarmTimerGetter
import ru.power.selector.ui.data.AlarmCalendar
import ru.power.selector.ui.data.Switch
import ru.power.selector.ui.data.WeekId

class GetTimeToAlarmUseCase(private val iAlarmTimerGetter: IAlarmTimerGetter)
    : UseCase<GetTimeToAlarmUseCase.Param, AlarmCalendar> {

    override suspend fun execute(request: Param, scope: CoroutineScope): AlarmCalendar {
        return AlarmCalendar(
            with(iAlarmTimerGetter) {
                with(request) {
                    when {
                        id != null && switch != null ->
                            get(id, switch)?.let {
                                listOf(it)
                            } ?: listOf()

                        id != null -> get(id)
                        switch != null -> get(switch)
                        else -> getAll()
                    }
                }
            }
        )
    }

    class Param(val id : WeekId? = null, val switch: Switch? = null)
}