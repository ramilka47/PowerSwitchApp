package ru.power.selector.domain.store

import ru.power.selector.ui.data.AlarmTimer
import ru.power.selector.ui.data.Switch
import ru.power.selector.ui.data.WeekId

interface IAlarmTimerGetter {

    fun getAll() : List<AlarmTimer>

    fun get(id : WeekId) : List<AlarmTimer>

    fun get(switch: Switch) : List<AlarmTimer>

    fun get(id : WeekId, switch: Switch) : AlarmTimer?
}