package ru.power.selector.domain.use_case

import kotlinx.coroutines.CoroutineScope
import ru.power.selector.domain.SWITCH
import ru.power.selector.domain.WEEK_LIST
import ru.power.selector.domain.store.IAlarmTimerGetter
import ru.power.selector.ui.data.AlarmCalendar
import ru.power.selector.ui.data.AlarmTimer
import ru.power.selector.ui.data.Switch
import ru.power.selector.ui.data.WeekId

class GetTimeToAlarmUseCase(private val iAlarmTimerGetter: IAlarmTimerGetter)
    : UseCase<GetTimeToAlarmUseCase.Param, AlarmCalendar> {

    override suspend fun execute(request: Param, scope: CoroutineScope): AlarmCalendar {
        val data = with(iAlarmTimerGetter) {
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

        return AlarmCalendar(
            createDefault().apply {
                keys.forEach { alarmTimer ->
                    data.find { it.id == alarmTimer.id && it.switch == alarmTimer.switch }?.let {
                        set(alarmTimer, it)
                    }
                }
            }.values.toList()
        )
    }

    private fun createDefault() : MutableMap<AlarmTimer, AlarmTimer> {
        val result = mutableMapOf<AlarmTimer, AlarmTimer>()
            SWITCH.forEach {switch->
                WEEK_LIST.forEach {day->
                    result.put(AlarmTimer(day, switch), AlarmTimer(day, switch))
                }
            }
        return result
    }

    class Param(val id : WeekId? = null, val switch: Switch? = null)
}