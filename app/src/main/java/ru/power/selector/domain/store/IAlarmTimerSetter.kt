package ru.power.selector.domain.store

import ru.power.selector.ui.data.AlarmTimer

interface IAlarmTimerSetter {

    fun setAll(timers : List<AlarmTimer>)

    fun set(timer : AlarmTimer)

    fun clear()
}